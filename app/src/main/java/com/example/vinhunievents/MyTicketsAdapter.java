package com.example.vinhunievents;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.R;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import com.example.vinhunievents.util.QRCodeGenerator;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class MyTicketsAdapter extends RecyclerView.Adapter<MyTicketsAdapter.TicketViewHolder> {

    private final List<Registration> registrations;
    private final Context context;

    public MyTicketsAdapter(List<Registration> registrations, Context context) {
        this.registrations = registrations;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Registration reg = registrations.get(position);
        Event event = AppDatabase.getInstance(context).appDao().getEventById(reg.eventId);
        if (event != null) {
            holder.bind(event, reg);
        }
    }

    @Override
    public int getItemCount() {
        return registrations.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvDate, tvLoc, tvStatus;
        private final ImageView ivQR;
        private final View statusIndicator;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvDate = itemView.findViewById(R.id.tvEventDate);
            tvLoc = itemView.findViewById(R.id.tvEventLocation);
            tvStatus = itemView.findViewById(R.id.tvAttendanceStatus);
            ivQR = itemView.findViewById(R.id.ivQRCode);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }

        public void bind(Event event, Registration reg) {
            tvTitle.setText(event.title);
            tvDate.setText("📅 " + event.date);
            tvLoc.setText("📍 " + event.location);

            if (reg.isAttended) {
                tvStatus.setText("ĐÃ ĐIỂM DANH");
                tvStatus.setTextColor(itemView.getContext().getColor(R.color.accent_green));
                statusIndicator.setBackgroundColor(itemView.getContext().getColor(R.color.accent_green));
            } else {
                tvStatus.setText("CHƯA ĐIỂM DANH");
                tvStatus.setTextColor(itemView.getContext().getColor(R.color.error));
                statusIndicator.setBackgroundColor(itemView.getContext().getColor(R.color.error));
            }

            Bitmap qr = QRCodeGenerator.generateRegistrationQR(event.id, reg.userId, reg.id);
            if (qr != null) ivQR.setImageBitmap(qr);
        }
    }
}