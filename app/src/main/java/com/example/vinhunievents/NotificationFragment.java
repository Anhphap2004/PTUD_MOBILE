package com.example.vinhunievents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.Event;
import com.example.vinhunievents.database.Registration;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView rvNotifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        rvNotifications = view.findViewById(R.id.rvNotifications);
        loadSmartNotifications();
        return view;
    }

    private void loadSmartNotifications() {
        int userId = getActivity().getIntent().getIntExtra("USER_ID", -1);
        List<NotificationAdapter.NotificationItem> notifications = new ArrayList<>();

        if (userId != -1) {
            List<Registration> registrations = AppDatabase.getInstance(getActivity()).appDao().getUserRegistrations(userId);
            for (Registration reg : registrations) {
                Event event = AppDatabase.getInstance(getActivity()).appDao().getEventById(reg.eventId);
                if (event != null) {
                    if (reg.isChecked) {
                        String statusStr = reg.isAttended ? "CÓ MẶT" : "VẮNG MẶT";
                        notifications.add(new NotificationAdapter.NotificationItem(
                                "📍 Cập nhật điểm danh: " + event.title,
                                "Trạng thái của bạn: " + statusStr + ". Cảm ơn bạn đã tham gia.",
                                "Hệ thống"
                        ));
                    }
                    if (reg.isCertified) {
                        notifications.add(new NotificationAdapter.NotificationItem(
                                "🎓 Giấy chứng nhận: " + event.title,
                                "Bạn đã nhận được giấy chứng nhận tham gia sự kiện. Hãy vào trang cá nhân để xem.",
                                "Chứng nhận"
                        ));
                    }
                    notifications.add(new NotificationAdapter.NotificationItem(
                            "🔔 Nhắc hẹn: " + event.title,
                            "Bạn có hẹn tham gia vào ngày " + event.date + ". Đừng quên nhé!",
                            "Nhắc hẹn"
                    ));
                }
            }
        }

        if (notifications.isEmpty()) {
            notifications.add(new NotificationAdapter.NotificationItem(
                    "Chào mừng bạn!",
                    "Hãy đăng ký tham gia các sự kiện mới nhất.",
                    "Hệ thống"
            ));
        }

        NotificationAdapter adapter = new NotificationAdapter(notifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotifications.setAdapter(adapter);
    }
}