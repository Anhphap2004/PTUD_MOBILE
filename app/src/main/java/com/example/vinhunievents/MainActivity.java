package com.example.vinhunievents;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.vinhunievents.database.AppDatabase;
import com.example.vinhunievents.database.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private int userId;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getIntExtra("USER_ID", -1);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        
        checkUserRole();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            
            if (id == R.id.nav_home) {
                if (isAdmin) {
                    selectedFragment = new AdminDashboardFragment();
                } else {
                    selectedFragment = new HomeFragment();
                }
            } else if (id == R.id.nav_notifications) {
                selectedFragment = new NotificationFragment();
            } else if (id == R.id.nav_tickets) {
                selectedFragment = new MyTicketsFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else if (id == R.id.nav_more) {
                selectedFragment = new MoreFragment();
            }
            
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void checkUserRole() {
        if (userId != -1) {
            new Thread(() -> {
                User user = AppDatabase.getInstance(this).appDao().getUserById(userId);
                runOnUiThread(() -> {
                    if (user != null && "ADMIN".equals(user.role)) {
                        isAdmin = true;
                        loadFragment(new AdminDashboardFragment());
                    } else {
                        isAdmin = false;
                        loadFragment(new HomeFragment());
                    }
                });
            }).start();
        } else {
            loadFragment(new HomeFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}