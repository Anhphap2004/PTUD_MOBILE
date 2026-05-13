package com.example.vinhunievents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.User;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvRole, tvMssv, tvClass, tvK, tvPhone, tvBirth, tvDept;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.tvProfileName); // Need ID
        tvRole = view.findViewById(R.id.tvProfileRole); // Need ID
        tvMssv = view.findViewById(R.id.tvMssvValue); // Need ID
        tvClass = view.findViewById(R.id.tvClassValue); // Need ID
        tvK = view.findViewById(R.id.tvKValue); // Need ID
        tvPhone = view.findViewById(R.id.tvPhoneValue); // Need ID
        tvBirth = view.findViewById(R.id.tvBirthValue); // Need ID
        tvDept = view.findViewById(R.id.tvDeptValue); // Need ID

        int userId = getActivity().getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            User user = AppDatabase.getInstance(getActivity()).appDao().getUserById(userId);
            if (user != null) {
                tvName.setText(user.fullName);
                tvRole.setText(user.role);
                tvMssv.setText(user.mssv);
                tvClass.setText(user.className);
                tvK.setText("K63"); // Static for now or add to User
                tvPhone.setText(user.phone);
                tvBirth.setText(user.birthday);
                tvDept.setText(user.department);
            }
        }

        return view;
    }
}