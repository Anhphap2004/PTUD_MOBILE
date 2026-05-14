package com.example.vinhunievents.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AppDao {
    // User
    @Insert
    long insertUser(User user);
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);
    
    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);

    // Events
    @Insert
    void insertEvent(Event event);
    
    @Query("SELECT * FROM events")
    List<Event> getAllEvents();
    
    @Query("SELECT * FROM events WHERE id = :eventId")
    Event getEventById(int eventId);
    
    @androidx.room.Update
    void updateEvent(Event event);
    
    @androidx.room.Delete
    void deleteEvent(Event event);

    // Registrations
    @Insert
    void registerForEvent(Registration registration);
    
    @Query("SELECT * FROM registrations WHERE userId = :userId AND eventId = :eventId LIMIT 1")
    Registration getRegistration(int userId, int eventId);
    
    @Query("SELECT * FROM registrations WHERE userId = :userId")
    List<Registration> getUserRegistrations(int userId);

    @Query("SELECT * FROM registrations WHERE eventId = :eventId")
    List<Registration> getEventRegistrations(int eventId);

    @androidx.room.Update
    void updateRegistration(Registration registration);
}