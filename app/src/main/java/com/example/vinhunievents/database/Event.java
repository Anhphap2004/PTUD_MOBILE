package com.example.vinhunievents.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String title;
    public String description;
    public String date;
    public String location;
    public String imagePath;
    public String category; // "Tin tức sự kiện", "Đoàn cơ sở", "Học tập"
    public String creatorName;
    public String timeLimit;
    public int maxCapacity;
}