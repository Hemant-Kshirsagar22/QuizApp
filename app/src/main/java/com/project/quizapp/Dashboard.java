package com.project.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.User;
import com.project.quizapp.session.SessionManager;

public class Dashboard extends AppCompatActivity {

    TextView nameTextView;

    CardView logoutCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        nameTextView = findViewById(R.id.name);
        logoutCardView = findViewById(R.id.logout);

//        String user = getIntent().getStringExtra("user");
        SessionManager sessionManager = new SessionManager(Dashboard.this);
        FirebaseDBHelper.getUserByUserName(sessionManager.getUserName(), new FirebaseDBHelper.UserQueryCallback() {
            @Override
            public void onSuccess(User user) {
                if(user != null) {
                    nameTextView.setText(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());
                }
                else
                {
                    nameTextView.setText("USER NAME");
                }
            }

            @Override
            public void onFailure(String errMsg) {

            }
        });



        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setUserLogout();
                Intent intent = new Intent(Dashboard.this,LoginPage.class);
                startActivity(intent);
            }
        });

    }
}