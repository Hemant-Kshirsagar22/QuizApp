package com.project.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.Question;
import com.project.quizapp.database.entities.User;

import java.util.List;
import java.util.Objects;

public class GlobalDrawerLayoutAndBottomNevigation extends AppCompatActivity {
    private MaterialToolbar toolbar = null;
    private DrawerLayout drawerLayout = null;
    private ActionBarDrawerToggle actionBarDrawerToggle = null;
    private NavigationView navigationView = null;

    private TextView userName = null;
    private TextView userEmail = null;
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
        navigationView = findViewById(R.id.nav_view);

        // Accessing the header view inside NavigationView
        View headerView = navigationView.getHeaderView(0);

        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(Objects.equals(item.getTitle(), "Refresh"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this,"REFRESH",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Feedback"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this,"Feedback",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Select Language"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this,"Select Language",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Help and Support"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this,"Help and Support",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "About Us"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this,"About Us",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Logout"))
                {
                    Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this,"Logout",Toast.LENGTH_SHORT).show();
                    FirebaseDBHelper.logout(GlobalDrawerLayoutAndBottomNevigation.this);
                    IntentManager.toLoginActivity(GlobalDrawerLayoutAndBottomNevigation.this);
                    return (true);
                }
                else {
                    return (false);
                }
            }
        });


        navigationView = findViewById(R.id.nav_view);
        // Initialize Views
        navigationView = findViewById(R.id.nav_view);


        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);

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
                    Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this, errMsg,Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            userName.setText(R.string.defautl_user_name);
        }

        FirebaseDBHelper.getQuestionByCategory("Logical-Reasoning/Analogies", new FirebaseDBHelper.QuestionQueryCallback() {
            @Override
            public void onSuccess(List<Question> questions) {
                Log.d("QUESTION", questions.get(0).toString());
                Log.d("ARRAY_LEN", questions.size() + "");
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(GlobalDrawerLayoutAndBottomNevigation.this,errMsg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}