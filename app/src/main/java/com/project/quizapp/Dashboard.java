package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.Question;
import com.project.quizapp.database.entities.User;
import com.project.quizapp.session.SessionManager;

public class Dashboard extends AppCompatActivity {

    TextView nameTextView;

    MaterialToolbar toolbar = null;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    CardView logoutCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        nameTextView = findViewById(R.id.name);
//        logoutCardView = findViewById(R.id.logout);

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


//        String user = getIntent().getStringExtra("user");
//        SessionManager sessionManager = new SessionManager(Dashboard.this);
        FirebaseDBHelper.getUser(new FirebaseDBHelper.UserQueryCallback() {
            @Override
            public void onSuccess(User user) {
                if(user != null) {
                   // nameTextView.setText(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
                }
                else
                {
                   // nameTextView.setText("USER NAME");
                }
            }

            @Override
            public void onFailure(String errMsg) {

            }
        });

//        logoutCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                sessionManager.setUserLogout(Dashboard.this);
//                FirebaseDBHelper.logout();
//                IntentManager.toLoginActivity(Dashboard.this);
//            }
//        });

    }
}