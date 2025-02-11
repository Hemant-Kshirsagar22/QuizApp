package com.project.quizapp.admin.user;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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

class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_user_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String name = users.get(position).getFirstName() + " " + users.get(position).getLastName();
        holder.userNameTextView.setText(name);

        holder.userEmailTextView.setText(users.get(position).getEmail());
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.baseline_person_24);

        holder.userImageView.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView userImageView;
        TextView userNameTextView;
        TextView userEmailTextView;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.userIamge);
            userNameTextView = itemView.findViewById(R.id.userItemName);
            userEmailTextView = itemView.findViewById(R.id.emailItem);
        }
    }
}