package com.example.vinhunievents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;

public class AddEventActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDate, etLocation;
    private ImageView ivPreview;
    private TextView tvTitle;
    private Button btnSave;
    private int eventId = -1;
    private String selectedImageUri = "";

    private final ActivityResultLauncher<String[]> mGetContent =
            registerForActivityResult(
                    new ActivityResultContracts.OpenDocument(),
                    uri -> {

                        if (uri != null) {

                            final int takeFlags =
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;

                            try {
                                getContentResolver()
                                        .takePersistableUriPermission(uri, takeFlags);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            selectedImageUri = uri.toString();

                            Glide.with(this)
                                    .load(uri)
                                    .into(ivPreview);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        tvTitle = findViewById(R.id.tvScreenTitle);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        ivPreview = findViewById(R.id.ivEventPreview);
        btnSave = findViewById(R.id.btnSaveEvent);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnSelectImage).setOnClickListener(v -> mGetContent.launch(new String[]{"image/*"}));

        eventId = getIntent().getIntExtra("EDIT_EVENT_ID", -1);
        if (eventId != -1) {
            tvTitle.setText("Chỉnh sửa sự kiện");
            btnSave.setText("Cập nhật sự kiện");
            loadEventData();
        }

        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void loadEventData() {
        Event event = AppDatabase.getInstance(this).appDao().getEventById(eventId);
        if (event != null) {
            etTitle.setText(event.title);
            etDescription.setText(event.description);
            etDate.setText(event.date);
            etLocation.setText(event.location);
            if (event.imagePath != null && !event.imagePath.isEmpty()) {
                selectedImageUri = event.imagePath;
                Glide.with(this).load(Uri.parse(selectedImageUri)).into(ivPreview);
            }
        }
    }

    private void saveEvent() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String loc = etLocation.getText().toString().trim();

        if (title.isEmpty() || desc.isEmpty() || date.isEmpty() || loc.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event;
        if (eventId != -1) {
            event = AppDatabase.getInstance(this).appDao().getEventById(eventId);
        } else {
            event = new Event();
        }

        event.title = title;
        event.description = desc;
        event.date = date;
        event.location = loc;
        event.imagePath = selectedImageUri;

        if (eventId != -1) {
            AppDatabase.getInstance(this).appDao().updateEvent(event);
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        } else {
            AppDatabase.getInstance(this).appDao().insertEvent(event);
            Toast.makeText(this, "Thêm sự kiện thành công!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}