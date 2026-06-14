package com.example.vinhunievents;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Feedback;
import java.util.List;

public class AdminFeedbackActivity extends AppCompatActivity implements FeedbackAdapter.OnFeedbackClickListener {

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_feedback);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rv = findViewById(R.id.rvFeedbackList);
        loadFeedback();
    }

    private void loadFeedback() {
        List<Feedback> feedbackList = AppDatabase.getInstance(this).appDao().getAllFeedback();
        FeedbackAdapter adapter = new FeedbackAdapter(feedbackList, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    @Override
    public void onDeleteClick(Feedback feedback) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa phản hồi")
                .setMessage("Bạn có chắc chắn muốn xóa tin nhắn này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    AppDatabase.getInstance(this).appDao().deleteFeedback(feedback);
                    Toast.makeText(this, "Đã xóa phản hồi", Toast.LENGTH_SHORT).show();
                    loadFeedback();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}