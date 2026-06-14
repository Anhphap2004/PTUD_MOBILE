package com.example.vinhunievents;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.User;
import com.google.android.material.chip.ChipGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventsActivity extends AppCompatActivity implements EventAdapter.OnEventClickListener {

    private RecyclerView rvEvents;
    private int userId;
    private boolean isAdmin = false;
    private List<Event> allEvents = new ArrayList<>();
    private EditText etSearch;
    private ChipGroup chipGroupFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        rvEvents = findViewById(R.id.rvEventsList);
        etSearch = findViewById(R.id.etSearchEvents);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);

        userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId != -1) {
            User user = AppDatabase.getInstance(this).appDao().getUserById(userId);
            if (user != null) {
                isAdmin = "ADMIN".equals(user.role);
            }
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        setupSearchAndFilter();
        loadEvents();
    }

    private void setupSearchAndFilter() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        chipGroupFilter.setOnCheckedChangeListener((group, checkedId) -> applyFilters());
    }

    private void applyFilters() {
        String query = etSearch.getText().toString().toLowerCase().trim();
        int checkedId = chipGroupFilter.getCheckedChipId();
        String category = "";
        
        if (checkedId == R.id.chipNews) category = "Tin tức sự kiện";
        else if (checkedId == R.id.chipUnit) category = "Đoàn cơ sở";
        else if (checkedId == R.id.chipStudy) category = "Học tập";

        final String finalCategory = category;
        List<Event> filtered = allEvents.stream()
            .filter(e -> e.title.toLowerCase().contains(query))
            .filter(e -> finalCategory.isEmpty() || finalCategory.equals(e.category))
            .collect(Collectors.toList());
            
        updateAdapter(filtered);
    }

    private void loadEvents() {
        allEvents = AppDatabase.getInstance(this).appDao().getAllEvents();
        updateAdapter(allEvents);
    }

    private void updateAdapter(List<Event> list) {
        EventAdapter adapter = new EventAdapter(list, isAdmin, this);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(adapter);
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("EVENT_ID", event.id);
        intent.putExtra("USER_ID", userId);
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