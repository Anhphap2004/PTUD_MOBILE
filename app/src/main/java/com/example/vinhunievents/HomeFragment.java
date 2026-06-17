package com.example.vinhunievents;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.User;

import java.util.List;

public class HomeFragment extends Fragment implements EventAdapter.OnEventClickListener {

    private RecyclerView rvEvents;
    private EventAdapter adapter;

    private TextView tvUserName, tvGpa, tvRl, tvRank;

    private User currentUser;
    private int userId;
    private boolean isAdmin = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        loadUserInfo(view);
        setupButtons(view);
        loadEvents();

        return view;
    }

    private void initViews(View view) {
        rvEvents = view.findViewById(R.id.rvEvents);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvGpa = view.findViewById(R.id.tvGpaValue);
        tvRl = view.findViewById(R.id.tvRlValue);
        tvRank = view.findViewById(R.id.tvRankValue);

        rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEvents.setNestedScrollingEnabled(false);
    }

    private void loadUserInfo(View view) {
        userId = getActivity().getIntent().getIntExtra("USER_ID", -1);

        if (userId == -1) return;

        currentUser = AppDatabase.getInstance(getActivity())
                .appDao()
                .getUserById(userId);

        if (currentUser == null) return;

        tvUserName.setText(currentUser.fullName);
        tvGpa.setText(String.valueOf(currentUser.gpa));
        tvRl.setText(String.valueOf(currentUser.rl));
        tvRank.setText("#" + currentUser.rank);

        isAdmin = "ADMIN".equals(currentUser.role);

        if (isAdmin) {
            showAdminFeatures(view);
            setupAdminButtons(view);
        }
    }

    private void showAdminFeatures(View view) {
        LinearLayout featureAddEvent = view.findViewById(R.id.featureAddEvent);
        LinearLayout featureAttendance = view.findViewById(R.id.featureAttendance);

        featureAddEvent.setVisibility(View.VISIBLE);
        featureAttendance.setVisibility(View.VISIBLE);
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.btnSchedule).setOnClickListener(v -> {
            Toast.makeText(
                    getActivity(),
                    "Tính năng Lịch học đang được phát triển",
                    Toast.LENGTH_SHORT
            ).show();
        });

        view.findViewById(R.id.btnViewAllEvents).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EventsActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }

    private void setupAdminButtons(View view) {
        view.findViewById(R.id.featureAddEvent).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEventActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.featureAttendance).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminSelectEventActivity.class);
            startActivity(intent);
        });
    }

    private void loadEvents() {
        List<Event> events = AppDatabase.getInstance(getActivity())
                .appDao()
                .getLatestEvents();

        adapter = new EventAdapter(events, isAdmin, this);
        rvEvents.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
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
                .setPositiveButton("Xóa", (dialog, which) -> deleteEvent(event))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteEvent(Event event) {
        AppDatabase.getInstance(getActivity())
                .appDao()
                .deleteEvent(event);

        Toast.makeText(
                getActivity(),
                "Đã xóa sự kiện",
                Toast.LENGTH_SHORT
        ).show();

        loadEvents();
    }
}