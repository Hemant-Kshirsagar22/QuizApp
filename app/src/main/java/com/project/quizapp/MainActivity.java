package com.project.quizapp;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    Button loginButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent loginIntent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(loginIntent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent registerIntent = new Intent(getApplicationContext(),RegistrationForm.class);
                startActivity(registerIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//       DatabaseHelper dbHelper = new DatabaseHelper(this);
//       SQLiteDatabase db = dbHelper.getWritableDatabase();
//       dbHelper.onUpgrade(db,1,1);

    }
}