package com.example.vinhunievents.database;

import android.content.Context;

public class DataInitializer {
    public static void initData(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        AppDao dao = db.appDao();

        // ADMIN
        if (dao.login("admin@vinhuni.edu.vn", "123456") == null) {
            User admin = new User();
            admin.email = "admin@vinhuni.edu.vn";
            admin.password = "123456";
            admin.fullName = "NGUYỄN BÁ PHÁP (ADMIN)";
            admin.mssv = "225748020110172";
            admin.role = "ADMIN";
            admin.className = "63K3";
            admin.department = "Công nghệ thông tin";
            admin.phone = "0333013972";
            admin.birthday = "30/09/2004";
            dao.insertUser(admin);
        }

        // STUDENT
        if (dao.login("nghia@vinhuni.edu.vn", "123456") == null) {
            User student = new User();
            student.email = "nghia@vinhuni.edu.vn";
            student.password = "123456";
            student.fullName = "Lê Trọng Nghĩa (SINH VIÊN)";
            student.mssv = "225748020110001";
            student.role = "STUDENT";
            student.className = "63K1";
            student.department = "Kỹ thuật phần mềm";
            student.phone = "0911111111";
            student.birthday = "01/01/2004";
            dao.insertUser(student);
        }
        
        if (dao.getAllEvents().isEmpty()) {
            Event e1 = new Event();
            e1.title = "Hội thảo kỹ năng mềm cho sinh viên";
            e1.description = "Sự kiện nhằm cung cấp các kỹ năng cần thiết cho tân sinh viên.";
            e1.date = "20/05/2026";
            e1.location = "Hội trường A";
            e1.category = "Học tập";
            e1.creatorName = "Đoàn Trường";
            e1.timeLimit = "14:00 - 17:00";
            e1.maxCapacity = 200;
            dao.insertEvent(e1);
            
            Event e2 = new Event();
            e2.title = "Ngày hội việc làm 2026";
            e2.description = "Cơ hội tiếp cận hàng trăm doanh nghiệp hàng đầu.";
            e2.date = "15/06/2026";
            e2.location = "Sân vận động";
            e2.category = "Tin tức sự kiện";
            e2.creatorName = "Trung tâm Hướng nghiệp";
            e2.timeLimit = "08:00 - 16:00";
            e2.maxCapacity = 500;
            dao.insertEvent(e2);
        }
    }
}