package com.example.vinhunievents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Registration;
import com.example.vinhunievents.database.User;
import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvRole, tvMssv, tvClass, tvK, tvPhone, tvBirth, tvDept;
    private RecyclerView rvHistory;
    private RegistrationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.tvProfileName);
        tvRole = view.findViewById(R.id.tvProfileRole);
        tvMssv = view.findViewById(R.id.tvMssvValue);
        tvClass = view.findViewById(R.id.tvClassValue);
        tvK = view.findViewById(R.id.tvKValue);
        tvPhone = view.findViewById(R.id.tvPhoneValue);
        tvBirth = view.findViewById(R.id.tvBirthValue);
        tvDept = view.findViewById(R.id.tvDeptValue);
        rvHistory = view.findViewById(R.id.rvRegistrationHistory);

        int userId = getActivity().getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            User user = AppDatabase.getInstance(getActivity()).appDao().getUserById(userId);
            if (user != null) {
                tvName.setText(user.fullName);
                tvRole.setText(user.role);
                tvMssv.setText(user.mssv);
                tvClass.setText(user.className);
                tvK.setText("K63");
                tvPhone.setText(user.phone);
                tvBirth.setText(user.birthday);
                tvDept.setText(user.department);
                
                loadHistory(userId);
            }
        }

        return view;
    }

    private void loadHistory(int userId) {
        List<Registration> registrations = AppDatabase.getInstance(getActivity()).appDao().getUserRegistrations(userId);
        adapter = new RegistrationAdapter(registrations);
        rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHistory.setAdapter(adapter);
    }
}