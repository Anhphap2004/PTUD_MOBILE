package com.example.vinhunievents;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import java.util.List;

public class AdminAttendanceActivity extends AppCompatActivity implements AttendanceAdapter.OnAttendanceClickListener {

    private RecyclerView rvStudents;
    private int eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance);

        eventId = getIntent().getIntExtra("EVENT_ID", -1);
        rvStudents = findViewById(R.id.rvStudentList);

        Event event = AppDatabase.getInstance(this).appDao().getEventById(eventId);
        if (event != null) {
            ((TextView)findViewById(R.id.tvAttendanceEventTitle)).setText(event.title);
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        loadStudentList();
    }

    private void loadStudentList() {
        List<Registration> registrations = AppDatabase.getInstance(this).appDao().getEventRegistrations(eventId);
        AttendanceAdapter adapter = new AttendanceAdapter(registrations, this);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(adapter);
    }

    @Override
    public void onProcessAttendance(Registration registration, boolean present) {
        registration.isChecked = true;
        registration.isAttended = present;
        // Reset certified if marked as absent
        if (!present) registration.isCertified = false;
        
        AppDatabase.getInstance(this).appDao().updateRegistration(registration);
        String msg = present ? "Đã đánh dấu CÓ MẶT" : "Đã đánh dấu VẮNG MẶT";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        loadStudentList();
    }

    @Override
    public void onIssueCertificate(Registration registration) {
        registration.isCertified = true;
        AppDatabase.getInstance(this).appDao().updateRegistration(registration);
        Toast.makeText(this, "Đã cấp giấy chứng nhận thành công!", Toast.LENGTH_SHORT).show();
        loadStudentList();
    }
}