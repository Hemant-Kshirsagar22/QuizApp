package com.project.quizapp.admin.user;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.quizapp.R;
import com.project.quizapp.database.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_user_item_view,parent,false));
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

    public class UserViewHolder extends RecyclerView.ViewHolder {

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

