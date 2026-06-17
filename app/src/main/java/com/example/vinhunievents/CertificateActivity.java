package com.example.vinhunievents;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import com.example.vinhunievents.database.User;

public class CertificateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        int regId = getIntent().getIntExtra("REG_ID", -1);
        if (regId == -1) {
            finish();
            return;
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnDownloadCert).setOnClickListener(v -> {
            Toast.makeText(this, "Đang khởi tạo tải xuống chứng nhận...", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Đã lưu chứng nhận vào thư mục Downloads", Toast.LENGTH_LONG).show();
        });

        loadCertificateData(regId);
    }

    private void loadCertificateData(int regId) {
        AppDatabase db = AppDatabase.getInstance(this);
        Registration reg = db.appDao().getRegistrationById(regId);
        
        if (reg != null) {
            User user = db.appDao().getUserById(reg.userId);
            Event event = db.appDao().getEventById(reg.eventId);
            
            if (user != null && event != null) {
                ((TextView)findViewById(R.id.tvCertStudentName)).setText(user.fullName.toUpperCase());
                ((TextView)findViewById(R.id.tvCertMssv)).setText("MSSV: " + user.mssv);
                ((TextView)findViewById(R.id.tvCertEventTitle)).setText(event.title);
                ((TextView)findViewById(R.id.tvCertDate)).setText("Ngày " + event.date.replace("/", " tháng ") + " năm 2026");
            }
        }
    }
}