package com.project.quizapp.admin.user;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.quizapp.R;

public class UserMain extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_section_main);

        bottomNavigationView
                = findViewById(R.id.bottomNavigationView);

        bottomNavigationView
                .setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.displayUsers);
    }
//    FirstFragment firstFragment = new FirstFragment();
    UserDisplay userDisplay = new UserDisplay();
    RemoveUser removeUser = new RemoveUser();

    public boolean
    onNavigationItemSelected(@NonNull MenuItem item)
    {
       int id = item.getItemId();
       if(id == R.id.displayUsers)
       {
           getSupportFragmentManager()
                   .beginTransaction()
                   .replace(R.id.flFragment, userDisplay)
                   .commit();
           return true;
       }
       else if(id == R.id.removeUser)
       {
           getSupportFragmentManager()
                   .beginTransaction()
                   .replace(R.id.flFragment, removeUser)
                   .commit();
           return true;
       }
       return false;
    }
}
