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
    public String imagePath; // Can be a local resource name or path
}