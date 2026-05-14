package com.example.vinhunievents;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Registration;
import com.example.vinhunievents.database.User;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<Registration> registrations;
    private OnAttendanceClickListener listener;

    public interface OnAttendanceClickListener {
        void onProcessAttendance(Registration registration, boolean present);
    }

    public AttendanceAdapter(List<Registration> registrations, OnAttendanceClickListener listener) {
        this.registrations = registrations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Registration reg = registrations.get(position);
        
        User user = AppDatabase.getInstance(holder.itemView.getContext()).appDao().getUserById(reg.userId);
        if (user != null) {
            holder.tvName.setText(user.fullName);
            holder.tvId.setText("MSSV: " + user.mssv);
        }

        holder.btnPresent.setOnClickListener(v -> listener.onProcessAttendance(reg, true));
        holder.btnAbsent.setOnClickListener(v -> listener.onProcessAttendance(reg, false));

        if (reg.isChecked) {
            holder.tvStatus.setVisibility(View.VISIBLE);
            if (reg.isAttended) {
                holder.tvStatus.setText("CÓ MẶT");
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                holder.btnPresent.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
                holder.btnPresent.setTextColor(Color.WHITE);
                holder.btnAbsent.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#33FF4B4B"))); // Dimmed
                holder.btnAbsent.setTextColor(Color.parseColor("#FF4B4B"));
            } else {
                holder.tvStatus.setText("VẮNG MẶT");
                holder.tvStatus.setTextColor(Color.parseColor("#FF4B4B"));
                holder.btnAbsent.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF4B4B")));
                holder.btnAbsent.setTextColor(Color.WHITE);
                holder.btnPresent.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#334CAF50"))); // Dimmed
                holder.btnPresent.setTextColor(Color.parseColor("#4CAF50"));
            }
        } else {
            holder.tvStatus.setVisibility(View.GONE);
            // Reset to default styles
            holder.btnPresent.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            holder.btnPresent.setTextColor(Color.WHITE);
            holder.btnAbsent.setBackgroundTintList(null); // Default tonal
            holder.btnAbsent.setTextColor(Color.parseColor("#FF4B4B"));
        }
    }

    @Override
    public int getItemCount() {
        return registrations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvStatus;
        View layoutActions;
        Button btnPresent, btnAbsent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvId = itemView.findViewById(R.id.tvStudentId);
            tvStatus = itemView.findViewById(R.id.tvAttendanceStatus);
            layoutActions = itemView.findViewById(R.id.layoutActionButtons);
            btnPresent = itemView.findViewById(R.id.btnPresent);
            btnAbsent = itemView.findViewById(R.id.btnAbsent);
        }
    }
}