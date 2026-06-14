package com.example.vinhunievents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Registration;
import java.util.List;

public class MyTicketsFragment extends Fragment {

    private RecyclerView rvTickets;
    private MyTicketsAdapter adapter;
    private FrameLayout emptyStateContainer;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);

        rvTickets = view.findViewById(R.id.rvTickets);
        emptyStateContainer = view.findViewById(R.id.emptyStateContainer);

        userId = getActivity().getIntent().getIntExtra("USER_ID", -1);

        rvTickets.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadTickets();

        return view;
    }

    private void loadTickets() {
        List<Registration> registrations = AppDatabase.getInstance(getActivity())
            .appDao()
            .getUserRegistrations(userId);

        if (registrations.isEmpty()) {
            emptyStateContainer.setVisibility(View.VISIBLE);
            rvTickets.setVisibility(View.GONE);
        } else {
            emptyStateContainer.setVisibility(View.GONE);
            rvTickets.setVisibility(View.VISIBLE);
            adapter = new MyTicketsAdapter(registrations, getActivity());
            rvTickets.setAdapter(adapter);
        }
    }
}