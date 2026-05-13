package com.example.vinhunievents;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;

public class EventDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        int eventId = getIntent().getIntExtra("EVENT_ID", -1);
        if (eventId != -1) {
            loadEventDetail(eventId);
        }

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void loadEventDetail(int id) {
        Event event = AppDatabase.getInstance(this).appDao().getEventById(id);
        if (event != null) {
            TextView tvTitle = findViewById(R.id.tvDetailTitle);
            TextView tvDate = findViewById(R.id.tvDetailDate);
            TextView tvLoc = findViewById(R.id.tvDetailLoc);
            TextView tvDesc = findViewById(R.id.tvDetailDesc);
            ImageView ivBanner = findViewById(R.id.ivDetailBanner);

            tvTitle.setText(event.title);
            tvDate.setText("Thời gian: " + event.date);
            tvLoc.setText("Địa điểm: " + event.location);
            tvDesc.setText(event.description);
            
            if (event.imagePath != null && !event.imagePath.isEmpty()) {
                Glide.with(this)
                        .load(Uri.parse(event.imagePath))
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(ivBanner);
            } else {
                ivBanner.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }
}