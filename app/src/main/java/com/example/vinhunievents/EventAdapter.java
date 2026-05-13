package com.example.vinhunievents;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.vinhunievents.database.Event;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events;
    private boolean isAdmin;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(Event event);
        void onEditClick(Event event);
        void onDeleteClick(Event event);
    }

    public EventAdapter(List<Event> events, boolean isAdmin, OnEventClickListener listener) {
        this.events = events;
        this.isAdmin = isAdmin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.tvTitle.setText(event.title);
        holder.tvDateLoc.setText(event.date + " • " + event.location);

        if (event.imagePath != null && !event.imagePath.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(event.imagePath))
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        if (isAdmin) {
            holder.adminActions.setVisibility(View.VISIBLE);
        } else {
            holder.adminActions.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));
        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(event));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(event));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDateLoc;
        ImageView ivImage;
        View adminActions;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvDateLoc = itemView.findViewById(R.id.tvEventDateLoc);
            ivImage = itemView.findViewById(R.id.ivEventImage);
            adminActions = itemView.findViewById(R.id.adminActions);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}