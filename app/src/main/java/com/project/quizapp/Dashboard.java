package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.Status;
import com.project.quizapp.database.entities.Question;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.session.SessionManager;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {

    private MaterialToolbar toolbar = null;
    private DrawerLayout drawerLayout = null;
    private ActionBarDrawerToggle actionBarDrawerToggle = null;
    private NavigationView navigationView = null;
    private MenuItem logoutNavMenuItem = null;

    private TextView userName = null;
    private TextView userEmail = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.AppBar);
        setSupportActionBar(toolbar);
        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.main);
        // Setup ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        // Sync the state
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView = findViewById(R.id.nav_view);

//        userName = findViewById(R.id.userName);
//        userEmail = findViewById(R.id.userEmail);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(Objects.equals(item.getTitle(), "Refresh"))
                {
                    Toast.makeText(Dashboard.this,"REFRESH",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Feedback"))
                {
                    Toast.makeText(Dashboard.this,"Feedback",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Select Language"))
                {
                    Toast.makeText(Dashboard.this,"Select Language",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Help and Support"))
                {
                    Toast.makeText(Dashboard.this,"Help and Support",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "About Us"))
                {
                    Toast.makeText(Dashboard.this,"About Us",Toast.LENGTH_SHORT).show();
                    return (true);
                }
                else if(Objects.equals(item.getTitle(), "Logout"))
                {
                    Toast.makeText(Dashboard.this,"Logout",Toast.LENGTH_SHORT).show();
                    FirebaseDBHelper.logout();
                    IntentManager.toLoginActivity(Dashboard.this);
                    return (true);
                }
                else {
                    return (false);
                }
            }
        });

//        if(FirebaseDBHelper.isUserLoggedIn())
//        {
//            FirebaseDBHelper.getUser(new FirebaseDBHelper.UserQueryCallback()
//            {
//                @Override
//                public void onSuccess(User user) {
//
//                        userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
//                        userEmail.setText(user.getEmail());
//                }
//
//                @Override
//                public void onFailure(String errMsg) {
//                    Toast.makeText(Dashboard.this, errMsg,Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//        else
//        {
//            userName.setText("USER");
//            userEmail.setText("NOT FOUND");
//        }

        FirebaseDBHelper.getQuestionByCategory("Logical-Reasoning/Number-Series", new FirebaseDBHelper.QuestionQueryCallback() {
            @Override
            public void onSuccess(Question question) {
                Log.d("QUESTION", question.toString());
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(Dashboard.this,errMsg,Toast.LENGTH_SHORT).show();
            }
        });
    }

}