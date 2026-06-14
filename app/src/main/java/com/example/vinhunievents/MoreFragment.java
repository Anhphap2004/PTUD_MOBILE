package com.example.vinhunievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoreFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        view.findViewById(R.id.btnContact).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ContactActivity.class);
            if (getActivity() != null) {
                int userId = getActivity().getIntent().getIntExtra("USER_ID", -1);
                intent.putExtra("USER_ID", userId);
            }
            startActivity(intent);
        });

        return view;
    }
}