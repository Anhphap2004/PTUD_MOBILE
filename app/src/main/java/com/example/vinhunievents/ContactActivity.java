package com.example.vinhunievents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Feedback;
import com.example.vinhunievents.database.User;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContactActivity extends AppCompatActivity {

    private EditText etContent;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        userId = getIntent().getIntExtra("USER_ID", -1);
        etContent = findViewById(R.id.etFeedbackContent);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.tvPhone).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0333013972"));
            startActivity(intent);
        });

        findViewById(R.id.tvEmail).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:it@vinhuni.edu.vn"));
            startActivity(intent);
        });

        findViewById(R.id.btnSendFeedback).setOnClickListener(v -> {
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                return;
            }

            saveFeedback(content);
            Toast.makeText(this, "Cảm ơn bạn đã gửi phản hồi. Chúng tôi sẽ xử lý sớm nhất!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void saveFeedback(String content) {
        Feedback fb = new Feedback();
        fb.userId = userId;
        fb.content = content;
        fb.timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        
        if (userId != -1) {
            User user = AppDatabase.getInstance(this).appDao().getUserById(userId);
            if (user != null) {
                fb.userName = user.fullName;
            }
        } else {
            fb.userName = "Khách ẩn danh";
        }

        AppDatabase.getInstance(this).appDao().insertFeedback(fb);
    }
}