package com.example.vinhunievents;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.ViewHolder> {

    private List<Registration> registrations;

    public RegistrationAdapter(List<Registration> registrations) {
        this.registrations = registrations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registration, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Registration reg = registrations.get(position);
        
        Event event = AppDatabase.getInstance(holder.itemView.getContext()).appDao().getEventById(reg.eventId);
        if (event != null) {
            holder.tvTitle.setText(event.title);
        } else {
            holder.tvTitle.setText("Sự kiện đã bị xóa");
        }
        
        holder.tvDate.setText("Ngày đăng ký: " + reg.registrationDate);

        if (reg.isAttended) {
            holder.tvStatus.setText("ĐÃ ĐIỂM DANH");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.accent_green));
        } else {
            holder.tvStatus.setText("CHỜ ĐIỂM DANH");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.accent_orange));
        }

        if (reg.isCertified) {
            holder.btnViewCert.setVisibility(View.VISIBLE);
            holder.btnViewCert.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CertificateActivity.class);
                intent.putExtra("REG_ID", reg.id);
                v.getContext().startActivity(intent);
            });
        } else {
            holder.btnViewCert.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return registrations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvStatus;
        MaterialButton btnViewCert;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvRegEventTitle);
            tvDate = itemView.findViewById(R.id.tvRegDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnViewCert = itemView.findViewById(R.id.btnViewCert);
        }
    }
}