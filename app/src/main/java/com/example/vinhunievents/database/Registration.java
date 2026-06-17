package com.example.vinhunievents.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "registrations")
public class Registration {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int userId;
    public int eventId;
    public String registrationDate;
    public boolean isAttended;
    public boolean isChecked; // Admin processed
    public boolean isCertified; // Admin issued certificate

}