package com.example.vinhunievents;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import java.util.stream.Collectors;
import java.util.List;

public class AdminDashboardFragment extends Fragment implements EventAdapter.OnEventClickListener {

    private TextView tvTotalEvents, tvTotalRegistrations, tvTotalAttended, tvAttendanceRate;
    private RecyclerView rvHotEvents;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        if (getActivity() != null && getActivity().getIntent() != null) {
            userId = getActivity().getIntent().getIntExtra("USER_ID", -1);
        }

        tvTotalEvents = view.findViewById(R.id.tvTotalEvents);
        tvTotalRegistrations = view.findViewById(R.id.tvTotalRegistrations);
        tvTotalAttended = view.findViewById(R.id.tvTotalAttended);
        tvAttendanceRate = view.findViewById(R.id.tvAttendanceRate);
        rvHotEvents = view.findViewById(R.id.rvHotEvents);

        setupQuickActions(view);

        rvHotEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadDashboardData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void setupQuickActions(View view) {
        view.findViewById(R.id.btnAdminAddEvent).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddEventActivity.class));
        });

        view.findViewById(R.id.btnAdminAttendance).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AdminSelectEventActivity.class));
        });

        view.findViewById(R.id.btnAdminContact).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AdminFeedbackActivity.class));
        });
    }

    private void loadDashboardData() {
        AppDatabase db = AppDatabase.getInstance(getActivity());
        List<Event> allEvents = db.appDao().getAllEvents();
        
        int totalEvents = allEvents.size();
        int totalRegistrations = 0;
        int totalAttended = 0;

        for (Event event : allEvents) {
            List<Registration> regs = db.appDao().getEventRegistrations(event.id);
            totalRegistrations += regs.size();
            for (Registration reg : regs) {
                if (reg.isAttended) totalAttended++;
            }
        }

        tvTotalEvents.setText(String.valueOf(totalEvents));
        tvTotalRegistrations.setText(String.valueOf(totalRegistrations));
        tvTotalAttended.setText(String.valueOf(totalAttended));

        if (totalRegistrations > 0) {
            int rate = (totalAttended * 100) / totalRegistrations;
            tvAttendanceRate.setText(rate + "%");
        } else {
            tvAttendanceRate.setText("0%");
        }

        // Show top 5 events by registrations
        List<Event> hotEvents = allEvents.stream()
            .sorted((e1, e2) -> Integer.compare(db.appDao().getRegistrationCount(e2.id), db.appDao().getRegistrationCount(e1.id)))
            .limit(5)
            .collect(Collectors.toList());

        EventAdapter adapter = new EventAdapter(hotEvents, true, this);
        rvHotEvents.setAdapter(adapter);
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("EVENT_ID", event.id);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Event event) {
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        intent.putExtra("EDIT_EVENT_ID", event.id);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Event event) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Xóa sự kiện")
                .setMessage("Bạn có chắc muốn xóa sự kiện này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    AppDatabase.getInstance(getActivity()).appDao().deleteEvent(event);
                    Toast.makeText(getActivity(), "Đã xóa sự kiện", Toast.LENGTH_SHORT).show();
                    loadDashboardData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}