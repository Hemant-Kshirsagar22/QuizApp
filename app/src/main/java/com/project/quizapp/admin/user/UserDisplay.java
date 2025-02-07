package com.project.quizapp.admin.user;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.quizapp.R;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.User;

import java.util.List;

public class UserDisplay extends AppCompatActivity {


    RecyclerView recyclerView = null;
    List<User> users = null;
//    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_user_display);

        recyclerView = findViewById(R.id.userRecyclerView);

        FirebaseDBHelper.getAllUsers(new FirebaseDBHelper.GetAllUsers() {
            @Override
            public void onSuccess(List<User> users) {
                UserDisplay.this.users = users;
                recyclerView.setLayoutManager(new LinearLayoutManager(UserDisplay.this));
                recyclerView.setAdapter(new UserAdapter(UserDisplay.this,users));
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(UserDisplay.this,errMsg,Toast.LENGTH_SHORT).show();
            }
        });


    }

}

