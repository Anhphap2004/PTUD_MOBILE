package com.example.vinhunievents;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import com.example.vinhunievents.database.User;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.List;

public class AdminDashboardFragment extends Fragment implements EventAdapter.OnEventClickListener {

    private TextView tvTotalEvents, tvTotalRegistrations, tvTotalAttended, tvAttendanceRate;
    private RecyclerView rvHotEvents;
    private int userId;

    private final ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            processScannedData(result.getContents());
        }
    });

    private void processScannedData(String data) {
        try {
            String[] parts = data.split(":");
            if (parts.length == 3) {
                int eventId = Integer.parseInt(parts[0]);
                int studentId = Integer.parseInt(parts[1]);
                int regId = Integer.parseInt(parts[2]);

                Registration reg = AppDatabase.getInstance(getActivity()).appDao().getRegistration(studentId, eventId);
                if (reg != null) {
                    if (reg.isAttended) {
                        Toast.makeText(getActivity(), "Sinh viên này đã điểm danh rồi!", Toast.LENGTH_SHORT).show();
                    } else {
                        reg.isAttended = true;
                        reg.isChecked = true;
                        AppDatabase.getInstance(getActivity()).appDao().updateRegistration(reg);
                        
                        User user = AppDatabase.getInstance(getActivity()).appDao().getUserById(studentId);
                        String name = user != null ? user.fullName : "Sinh viên";
                        
                        new AlertDialog.Builder(getActivity())
                            .setTitle("Điểm danh thành công")
                            .setMessage("Đã điểm danh cho: " + name)
                            .setPositiveButton("OK", null)
                            .show();
                        
                        loadDashboardData();
                    }
                } else {
                    Toast.makeText(getActivity(), "Không tìm thấy thông tin đăng ký!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Mã QR không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Lỗi khi quét mã: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        if (getActivity() != null && getActivity().getIntent() != null) {
            userId = getActivity().getIntent().getIntExtra("USER_ID", -1);
        }

        tvTotalEvents = view.findViewById(R.id.tvTotalEvents);
        tvTotalRegistrations = view.findViewById(R.id.tvTotalRegistrations);
        tvTotalAttended = view.findViewById(R.id.tvTotalAttended);
        tvAttendanceRate = view.findViewById(R.id.tvAttendanceRate);

        setupGreeting(view);
        
        rvHotEvents = view.findViewById(R.id.rvHotEvents);
        rvHotEvents.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupQuickActions(view);
        loadDashboardData();
        return view;
    }

    private void setupGreeting(View view) {
        TextView tvGreeting = view.findViewById(R.id.tvGreeting);
        TextView tvTodayDate = view.findViewById(R.id.tvTodayDate);

        if (tvTodayDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
            String currentDate = sdf.format(new Date());
            // Capitalize first letter of day
            currentDate = currentDate.substring(0, 1).toUpperCase() + currentDate.substring(1);
            tvTodayDate.setText(currentDate);
        }

        if (tvGreeting != null) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String greeting;

            if (hour >= 5 && hour < 12) {
                greeting = "Chào buổi sáng, Admin 👋";
            } else if (hour >= 12 && hour < 18) {
                greeting = "Chào buổi chiều, Admin 👋";
            } else if (hour >= 18 && hour < 22) {
                greeting = "Chào buổi tối, Admin 👋";
            } else {
                greeting = "Chúc ngủ ngon, Admin 🌙";
            }
            tvGreeting.setText(greeting);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }

    private void setupQuickActions(View view) {
        view.findViewById(R.id.btnAdminAddEvent).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddEventActivity.class));
        });

        view.findViewById(R.id.btnAdminAttendance).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AdminSelectEventActivity.class));
        });

        view.findViewById(R.id.btnAdminScanQR).setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Quét mã QR trên vé của sinh viên");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivityPortrait.class);
            qrCodeLauncher.launch(options);
        });

        view.findViewById(R.id.btnAdminContact).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AdminFeedbackActivity.class));
        });
    }

    private void loadDashboardData() {
        AppDatabase db = AppDatabase.getInstance(getActivity());
        List<Event> allEvents = db.appDao().getAllEvents();
        
        int totalEvents = allEvents.size();
        int totalRegistrations = 0;
        int totalAttended = 0;

        for (Event event : allEvents) {
            List<Registration> regs = db.appDao().getEventRegistrations(event.id);
            totalRegistrations += regs.size();
            for (Registration reg : regs) {
                if (reg.isAttended) totalAttended++;
            }
        }

        tvTotalEvents.setText(String.valueOf(totalEvents));
        tvTotalRegistrations.setText(String.valueOf(totalRegistrations));
        tvTotalAttended.setText(String.valueOf(totalAttended));

        if (totalRegistrations > 0) {
            int rate = (totalAttended * 100) / totalRegistrations;
            tvAttendanceRate.setText(rate + "%");
        } else {
            tvAttendanceRate.setText("0%");
        }

        // Show top 5 events by registrations
        List<Event> hotEvents = allEvents.stream()
            .sorted((e1, e2) -> Integer.compare(db.appDao().getRegistrationCount(e2.id), db.appDao().getRegistrationCount(e1.id)))
            .limit(5)
            .collect(Collectors.toList());

        EventAdapter adapter = new EventAdapter(hotEvents, true, this);
        rvHotEvents.setAdapter(adapter);
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("EVENT_ID", event.id);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Event event) {
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        intent.putExtra("EDIT_EVENT_ID", event.id);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Event event) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Xóa sự kiện")
                .setMessage("Bạn có chắc muốn xóa sự kiện này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    AppDatabase.getInstance(getActivity()).appDao().deleteEvent(event);
                    Toast.makeText(getActivity(), "Đã xóa sự kiện", Toast.LENGTH_SHORT).show();
                    loadDashboardData();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}