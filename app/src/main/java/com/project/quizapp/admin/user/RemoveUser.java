package com.project.quizapp.admin.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.quizapp.R;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class RemoveUser extends Fragment{

    public RemoveUser() {
        // Required empty public constructor
    }

    private Spinner spinner = null;
    private Button removeUserButton = null;
    private String selectedUser = null;
    private View view = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.admin_remove_user_fragment, container, false);

        spinner = view.findViewById(R.id.userListSpinner);
        removeUserButton = view.findViewById(R.id.removeUser);

        fetchDataAndUpdateList();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedUser = adapterView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        removeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedUser != null && (!selectedUser.equals("Please Select"))) {
                    FirebaseDBHelper.deleteUser(selectedUser, new FirebaseDBHelper.UserQueryCallback() {
                        @Override
                        public void onSuccess(User user) {
                            Toast.makeText(getContext(),"USER REMOVE SUCCESS",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String errMsg) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(),"Please Select",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void fetchDataAndUpdateList()
    {
        FirebaseDBHelper.getAllUsers(new FirebaseDBHelper.GetAllUsers() {
            @Override
            public void onSuccess(List<User> user) {
                ArrayList<String> usersArrayList= new ArrayList<>();

                usersArrayList.add("Please Select");

                for(int i = 0; i < user.size();i++)
                {
                    if(!user.get(i).getGoogleUser())
                    {
                        String userName = user.get(i).getEmail();
                        if (userName != null) {
                            usersArrayList.add(userName);
                        }
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,usersArrayList.toArray(new String[0]));
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

            }

            @Override
            public void onFailure(String errMsg) {

            }
        });

    }

}