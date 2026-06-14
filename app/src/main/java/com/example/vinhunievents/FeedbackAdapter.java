package com.example.vinhunievents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.Feedback;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    private List<Feedback> feedbackList;
    private OnFeedbackClickListener listener;

    public interface OnFeedbackClickListener {
        void onDeleteClick(Feedback feedback);
    }

    public FeedbackAdapter(List<Feedback> feedbackList, OnFeedbackClickListener listener) {
        this.feedbackList = feedbackList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Feedback fb = feedbackList.get(position);
        holder.tvName.setText(fb.userName);
        holder.tvTime.setText(fb.timestamp);
        holder.tvContent.setText(fb.content);
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(fb);
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvContent;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFeedbackUserName);
            tvTime = itemView.findViewById(R.id.tvFeedbackTime);
            tvContent = itemView.findViewById(R.id.tvFeedbackContent);
            btnDelete = itemView.findViewById(R.id.btnDeleteFeedback);
        }
    }
}