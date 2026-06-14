package com.example.vinhunievents.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String mssv;
    public String fullName;
    public String className;
    public String email;
    public String password;
    public String role; // "ADMIN" or "STUDENT"
    
    // For demo purposes
    public String phone;
    public String birthday;
    public String hometown;
    public String department;

    // Academic Stats
    public double gpa = 0.0;
    public int rl = 0;
    public int rank = 0;
}