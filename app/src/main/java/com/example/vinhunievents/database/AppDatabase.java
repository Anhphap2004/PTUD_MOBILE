package com.example.vinhunievents.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Event.class, Registration.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    
    public abstract AppDao appDao();
    
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "vinhuni_events_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Only for simplicity in school project
                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull androidx.sqlite.db.SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            // Initial data can be added here if needed via Executor
                        }
                    })
                    .build();
        }
        return instance;
    }
}