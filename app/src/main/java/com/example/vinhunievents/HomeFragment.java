package com.example.vinhunievents;

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
import com.example.vinhunievents.database.User;
import java.util.List;

public class HomeFragment extends Fragment implements EventAdapter.OnEventClickListener {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private User currentUser;
    private boolean isAdmin = false;
    private TextView tvUserName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvEvents = view.findViewById(R.id.rvEvents);
        tvUserName = view.findViewById(R.id.tvUserName); // Need to add ID to layout

        int userId = getActivity().getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            currentUser = AppDatabase.getInstance(getActivity()).appDao().getUserById(userId);
            if (currentUser != null) {
                tvUserName.setText(currentUser.fullName);
                isAdmin = "ADMIN".equals(currentUser.role);
                // Update UI based on role
                if (isAdmin) {
                    view.findViewById(R.id.adminAddEvent).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.adminAttendance).setVisibility(View.VISIBLE);
                    
                    view.findViewById(R.id.adminAddEvent).setOnClickListener(v -> {
                        startActivity(new Intent(getActivity(), AddEventActivity.class));
                    });
                }
            }
        }

        view.findViewById(R.id.btnViewAllEvents).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EventsActivity.class));
        });

        loadEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents(); // Refresh data when returning from Add/Edit screen
    }

    private void loadEvents() {
        List<Event> events = AppDatabase.getInstance(getActivity()).appDao().getAllEvents();
        adapter = new EventAdapter(events, isAdmin, this);
        rvEvents.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEvents.setAdapter(adapter);
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("EVENT_ID", event.id);
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
        AppDatabase.getInstance(getActivity()).appDao().deleteEvent(event);
        Toast.makeText(getActivity(), "Đã xóa sự kiện", Toast.LENGTH_SHORT).show();
        loadEvents();
    }
}