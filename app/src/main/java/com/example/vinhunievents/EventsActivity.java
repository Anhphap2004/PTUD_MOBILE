package com.example.vinhunievents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.User;
import java.util.List;

public class EventsActivity extends AppCompatActivity implements EventAdapter.OnEventClickListener {

    private RecyclerView rvEvents;
    private EventAdapter adapter;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        rvEvents = findViewById(R.id.rvEventsList); // Update layout ID
        if (rvEvents == null) {
            // Fallback if ID is different
            rvEvents = new RecyclerView(this); 
        }

        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            User user = AppDatabase.getInstance(this).appDao().getUserById(userId);
            if (user != null) {
                isAdmin = "ADMIN".equals(user.role);
            }
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        loadEvents();
    }

    private void loadEvents() {
        List<Event> events = AppDatabase.getInstance(this).appDao().getAllEvents();
        adapter = new EventAdapter(events, isAdmin, this);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(adapter);
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("EVENT_ID", event.id);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Event event) {
        Intent intent = new Intent(this, AddEventActivity.class);
        intent.putExtra("EDIT_EVENT_ID", event.id);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Event event) {
        AppDatabase.getInstance(this).appDao().deleteEvent(event);
        Toast.makeText(this, "Đã xóa sự kiện", Toast.LENGTH_SHORT).show();
        loadEvents();
    }
}