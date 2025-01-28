package com.project.quizapp.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseDBHelper {
    private static DatabaseReference rootRef = null;
    private static DatabaseReference userRef = null;
    private static String USER_COLLECTION_NAME = "Users";

    public interface UserQueryCallback
    {
        void onSuccess(User user);
        void onFailure(String errMsg);
    }

    private FirebaseDBHelper() {}
    public static DatabaseReference getRootRef() {
        if(rootRef == null)
        {
            rootRef = FirebaseDatabase.getInstance().getReference();
        }
        return rootRef;
    }

    public static DatabaseReference getUserRef() {
        if(userRef == null)
        {
            userRef = getRootRef().child(USER_COLLECTION_NAME);
        }
        return userRef;
    }

    public static void insertUser(User user, UserQueryCallback callback)
    {
        userRef = getUserRef();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the count of users
                Long[] userCount = {dataSnapshot.getChildrenCount()};
                Log.d("USERCNT","Number of users: " + userCount[0]);

                if(userCount[0] == 0)
                {
                    User admin = new User("admin", "admin", "admin@gmail.com", "123");
                    user.setUserId(userCount[0]);
                    userRef.child(userCount[0].toString()).setValue(admin);
                    userCount[0] += 1;
                }

                user.setUserId(userCount[0]);

                userRef.orderByChild("email").equalTo(user.getEmail()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() == 0)
                        {
                            userRef.child(userCount[0].toString()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    userCount[0] += 1;
                                    callback.onSuccess(null);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callback.onFailure(Status.MSG_INSERT_FAILED);
                                }
                            });
                        }
                        else
                        {
                            callback.onFailure(Status.MSG_USER_EXISTS);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("USERCNT","Error: " + databaseError.getMessage());
            }
        });
    }

    public static void getUserByUserName(String userName, UserQueryCallback callback)
    {
        userRef = getUserRef();

        userRef.orderByChild("email").equalTo(userName).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                if (snapshot.exists()) {

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        user = userSnapshot.getValue(User.class);
                    }

                }
                if(user != null) {
                    callback.onSuccess(user);
                }
                else
                {
                    callback.onFailure(Status.MSG_PASS_MISMATCH);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure("FIREBASE DATA GET ERROR");
            }
        });

    };
}
