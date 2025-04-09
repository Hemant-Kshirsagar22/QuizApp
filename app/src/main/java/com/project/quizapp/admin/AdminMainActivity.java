package com.project.quizapp.admin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.project.quizapp.IntentManager;
import com.project.quizapp.R;
import com.project.quizapp.database.FirebaseDBHelper;

public class AdminMainActivity extends AppCompatActivity {
    CardView homeCardView;
    CardView userSectionCardView;
    CardView questionSectionCardView;
    CardView logoutCardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_layout);

        homeCardView = findViewById(R.id.userHome);
        userSectionCardView = findViewById(R.id.userSection);
        questionSectionCardView = findViewById(R.id.questionSection);
        logoutCardView = findViewById(R.id.admin_logout);

        homeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentManager.toDashboardActivity(AdminMainActivity.this, true);
                finish();
            }
        });

        userSectionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentManager.toAdminUserSection(AdminMainActivity.this);
            }
        });

        logoutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDBHelper.logout(AdminMainActivity.this);
                IntentManager.toLoginActivity(AdminMainActivity.this);
                finish();
            }
        });
    }
}
