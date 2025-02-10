package com.project.quizapp.admin.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.quizapp.R;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.User;

import java.util.List;

public class UserDisplay extends Fragment {

    RecyclerView recyclerView = null;

    public UserDisplay() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.admin_user_display_fragment, container, false);

        recyclerView = view.findViewById(R.id.userRecyclerView);

        FirebaseDBHelper.getAllUsers(new FirebaseDBHelper.GetAllUsers() {
            @Override
            public void onSuccess(List<User> users) {
                if(users.size() == 0)
                {
                    Toast.makeText(getContext(),"NO USER FOUND !!!",Toast.LENGTH_SHORT).show();
                }
                else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(new UserAdapter(getContext(), users));
                }
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(getContext(),errMsg,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}