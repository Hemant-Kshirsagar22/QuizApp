package com.project.quizapp.admin.user;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.quizapp.R;
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.User;

import java.util.List;

public class RemoveUser extends Fragment{

    public RemoveUser() {
        // Required empty public constructor
    }

    private Spinner spinner = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_remove_user_fragment, container, false);

        spinner = view.findViewById(R.id.userListSpinner);

        fetchDataAndUpdateList();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedUser = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(getContext(),selectedUser,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    private void fetchDataAndUpdateList()
    {
        FirebaseDBHelper.getAllUsers(new FirebaseDBHelper.GetAllUsers() {
            @Override
            public void onSuccess(List<User> user) {
                String[] users = new String[user.size() + 1];

                users[0] = "Please Select";
                for(int i = 0; i < user.size(); i++)
                {
                    String userName = user.get(i).getEmail();
                    users[i + 1] = userName;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,users);
                adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

            }

            @Override
            public void onFailure(String errMsg) {

            }
        });

    }

}