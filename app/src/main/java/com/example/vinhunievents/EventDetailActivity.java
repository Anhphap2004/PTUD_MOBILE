package com.example.vinhunievents;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity {
    
    private int userId;
    private int eventId;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        userId = getIntent().getIntExtra("USER_ID", -1);
        eventId = getIntent().getIntExtra("EVENT_ID", -1);

        btnRegister = findViewById(R.id.registerButton);

        if (eventId != -1) {
            loadEventDetail(eventId);
            checkRegistrationStatus();
        }

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void checkRegistrationStatus() {
        if (userId == -1) {
            btnRegister.setEnabled(false);
            btnRegister.setText("Đăng nhập để đăng ký");
            return;
        }

        Registration reg = AppDatabase.getInstance(this).appDao().getRegistration(userId, eventId);
        if (reg != null) {
            btnRegister.setEnabled(false);
            btnRegister.setText("Đã đăng ký tham gia");
            btnRegister.setBackgroundColor(Color.parseColor("#2D3748"));
        }
    }

    private void handleRegistration() {
        if (userId == -1) return;

        Registration reg = new Registration();
        reg.userId = userId;
        reg.eventId = eventId;
        reg.registrationDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        reg.isAttended = false;
        reg.isChecked = false;

        AppDatabase.getInstance(this).appDao().registerForEvent(reg);
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        checkRegistrationStatus();
    }

    private void loadEventDetail(int id) {
        Event event = AppDatabase.getInstance(this).appDao().getEventById(id);
        if (event != null) {
            ((TextView)findViewById(R.id.tvDetailTitle)).setText(event.title);
            ((TextView)findViewById(R.id.tvDetailCategory)).setText(event.category != null ? event.category : "Sự kiện");
            ((TextView)findViewById(R.id.tvDetailDate)).setText("Thời gian: " + event.date + (event.timeLimit != null ? " (" + event.timeLimit + ")" : ""));
            ((TextView)findViewById(R.id.tvDetailLoc)).setText("Địa điểm: " + event.location);
            ((TextView)findViewById(R.id.tvDetailCreator)).setText("Người tạo: " + (event.creatorName != null ? event.creatorName : "Ban tổ chức"));
            ((TextView)findViewById(R.id.tvDetailCapacity)).setText("Số lượng: " + (event.maxCapacity > 0 ? event.maxCapacity : "Không giới hạn"));
            ((TextView)findViewById(R.id.tvDetailDesc)).setText(event.description);
            
            ImageView ivBanner = findViewById(R.id.ivDetailBanner);
            if (event.imagePath != null && !event.imagePath.isEmpty()) {
                Glide.with(this).load(Uri.parse(event.imagePath)).placeholder(android.R.drawable.ic_menu_gallery).into(ivBanner);
            } else {
                ivBanner.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }
}