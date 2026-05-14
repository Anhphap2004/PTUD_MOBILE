package com.example.vinhunievents;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import java.util.List;

public class AdminSelectEventActivity extends AppCompatActivity implements EventAdapter.OnEventClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_event);

        RecyclerView rv = findViewById(R.id.rvSelectEvent);
        List<Event> events = AppDatabase.getInstance(this).appDao().getAllEvents();
        
        EventAdapter adapter = new EventAdapter(events, false, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(this, AdminAttendanceActivity.class);
        intent.putExtra("EVENT_ID", event.id);
        startActivity(intent);
    }

    @Override public void onEditClick(Event event) {}
    @Override public void onDeleteClick(Event event) {}
}