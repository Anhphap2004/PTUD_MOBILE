package com.example.vinhunievents.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "feedback")
public class Feedback {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int userId;
    public String userName;
    public String content;
    public String timestamp;
}