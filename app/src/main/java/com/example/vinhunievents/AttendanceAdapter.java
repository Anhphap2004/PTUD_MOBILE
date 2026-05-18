package com.example.vinhunievents;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Registration;
import com.example.vinhunievents.database.User;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {


    private final List<Registration> registrations;
    private final OnAttendanceClickListener listener;

    public interface OnAttendanceClickListener {
        void onProcessAttendance(Registration registration, boolean present);
    }

    public AttendanceAdapter(List<Registration> registrations,
                             OnAttendanceClickListener listener) {

        this.registrations = registrations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_attendance, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        Registration reg = registrations.get(position);

        User user = AppDatabase
                .getInstance(holder.itemView.getContext())
                .appDao()
                .getUserById(reg.userId);

        if (user != null) {

            holder.tvName.setText(user.fullName.toUpperCase());

            holder.tvId.setText("MSSV: " + user.mssv);
        }

        holder.btnPresent.setOnClickListener(v -> {

            listener.onProcessAttendance(reg, true);

            holder.btnPresent.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(120);
        });

        holder.btnAbsent.setOnClickListener(v -> {

            listener.onProcessAttendance(reg, false);

            holder.btnAbsent.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(120);
        });

        updateAttendanceUI(holder, reg);
    }

    private void updateAttendanceUI(ViewHolder holder,
                                    Registration reg) {

        if (reg.isChecked) {

            holder.tvStatus.setVisibility(View.VISIBLE);

            if (reg.isAttended) {

                holder.tvStatus.setText("CÓ MẶT");
                holder.tvStatus.setTextColor(
                        Color.parseColor("#4CAF50")
                );

                holder.btnPresent.setBackgroundTintList(
                        ColorStateList.valueOf(
                                Color.parseColor("#4CAF50")
                        )
                );

                holder.btnPresent.setTextColor(Color.WHITE);

                holder.btnAbsent.setBackgroundTintList(
                        ColorStateList.valueOf(
                                Color.parseColor("#3D2323")
                        )
                );

                holder.btnAbsent.setTextColor(
                        Color.parseColor("#FF5A5A")
                );

            } else {

                holder.tvStatus.setText("VẮNG MẶT");

                holder.tvStatus.setTextColor(
                        Color.parseColor("#FF4B4B")
                );

                holder.btnAbsent.setBackgroundTintList(
                        ColorStateList.valueOf(
                                Color.parseColor("#FF4B4B")
                        )
                );

                holder.btnAbsent.setTextColor(Color.WHITE);

                holder.btnPresent.setBackgroundTintList(
                        ColorStateList.valueOf(
                                Color.parseColor("#2D5F35")
                        )
                );

                holder.btnPresent.setTextColor(
                        Color.parseColor("#A5D6A7")
                );
            }

        } else {

            holder.tvStatus.setVisibility(View.GONE);

            holder.btnPresent.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Color.parseColor("#4CAF50")
                    )
            );

            holder.btnPresent.setTextColor(Color.WHITE);

            holder.btnAbsent.setBackgroundTintList(
                    ColorStateList.valueOf(
                            Color.parseColor("#3D2323")
                    )
            );

            holder.btnAbsent.setTextColor(
                    Color.parseColor("#FF5A5A")
            );
        }
    }

    @Override
    public int getItemCount() {
        return registrations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvId;
        TextView tvStatus;

        MaterialButton btnPresent;
        MaterialButton btnAbsent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvStudentName);
            tvId = itemView.findViewById(R.id.tvStudentId);
            tvStatus = itemView.findViewById(R.id.tvAttendanceStatus);

            btnPresent = itemView.findViewById(R.id.btnPresent);
            btnAbsent = itemView.findViewById(R.id.btnAbsent);
        }
    }


}
