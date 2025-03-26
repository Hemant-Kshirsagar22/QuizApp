package com.project.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.databinding.ActivityAlertBoxForTestStartBinding;
import com.project.quizapp.session.SessionManager;

import java.util.Objects;

public class GlobalDrawerLayoutAndBottomNavigation extends AppCompatActivity{
    private MaterialToolbar toolbar = null;
    private DrawerLayout drawerLayout = null;
    private ActionBarDrawerToggle actionBarDrawerToggle = null;
    private NavigationView navigationView = null;
    private BottomNavigationView bottomNavigationView = null;

    private TextView userName = null;
    private TextView userEmail = null;

    private SessionManager sessionManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_drawer_layout_and_bottom_nevigation); // Using your global drawer XML file

        //        EdgeToEdge.enable(this);
        toolbar = findViewById(R.id.AppBar);
        setSupportActionBar(toolbar);
        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        // Setup ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        // Sync the state
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);

        // Accessing the header view inside NavigationView
        View headerView = navigationView.getHeaderView(0);

        sessionManager = new SessionManager(this);

        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(Objects.equals(item.getTitle(), "Refresh"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"REFRESH",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Help and Support"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"Help and Support",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "About Us"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"About Us",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Updates"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"Updates",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Performance"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"Performance",Toast.LENGTH_SHORT).show();
                    IntentManager.toPerformanceView(GlobalDrawerLayoutAndBottomNavigation.this);
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "History"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"History",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Logout"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"Logout",Toast.LENGTH_SHORT).show();
                    FirebaseDBHelper.logout(GlobalDrawerLayoutAndBottomNavigation.this);
                    IntentManager.toLoginActivity(GlobalDrawerLayoutAndBottomNavigation.this);
                    return (true);
                }
                else {
                    return (false);
                }
            }
        });

        // for bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(Objects.equals(item.getTitle(), "Home"))
                {
                    String currentActivity = this.getClass().getSimpleName();
                    if(!currentActivity.equals("Dashboard")) {
                        Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this, "Home", Toast.LENGTH_SHORT).show();
                        IntentManager.toDashboardActivity(GlobalDrawerLayoutAndBottomNavigation.this);
                    }
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Test"))
                {
                    if(sessionManager.getTestPauseStatus())
                    {
                        // show alert to start the test
                        AlertDialog.Builder builder = new AlertDialog.Builder(GlobalDrawerLayoutAndBottomNavigation.this);
                        ActivityAlertBoxForTestStartBinding alertBoxForTestStartBinding = ActivityAlertBoxForTestStartBinding.inflate(LayoutInflater.from(GlobalDrawerLayoutAndBottomNavigation.this));
                        builder.setView(alertBoxForTestStartBinding.getRoot());
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        alertBoxForTestStartBinding.next.setOnClickListener(v -> {
                            dialog.dismiss();
                            String selectedCategory = sessionManager.getPausedStateQuestionCategory();
                            if(selectedCategory == null)
                            {
                                Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this, "CATEGORY IS EMPTY", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                IntentManager.toQuestionPanelView(getApplicationContext(), selectedCategory, true);
                            }
                        });

                        alertBoxForTestStartBinding.cancelTest.setOnClickListener(v -> {
                            dialog.cancel();
                        });
                    }
                    else
                    {
                        Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this,"NO TEST TO RESUME", Toast.LENGTH_SHORT).show();
                    }

                    return (true);
                }
                else
                {
                    return false;
                }
            }
        });

        if(FirebaseDBHelper.isUserLoggedIn())
        {
            FirebaseDBHelper.getUser(new FirebaseDBHelper.UserQueryCallback()
            {
                @Override
                public void onSuccess(User user) {

                    userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                    userEmail.setText(user.getEmail());
                }

                @Override
                public void onFailure(String errMsg) {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNavigation.this, errMsg,Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            userName.setText(R.string.defautl_user_name);
        }
    }
}