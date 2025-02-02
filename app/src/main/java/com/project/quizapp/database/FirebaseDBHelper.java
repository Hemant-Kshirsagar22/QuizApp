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
import com.project.quizapp.database.entities.Question;
import com.project.quizapp.database.entities.User;

import java.util.ArrayList;

public class FirebaseDBHelper {
    private static DatabaseReference rootRef = null;
    private static DatabaseReference userRef = null;
    private static DatabaseReference questionRef = null;
    private static String USER_COLLECTION_NAME = "Users";
    private static String QUETION_COLLECTION_NAME = "Questions";

    public interface UserQueryCallback
    {
        void onSuccess(User user);
        void onFailure(String errMsg);
    }

    public interface QuestionQueryCallback
    {
        void onSuccess(Question question);
        void onFailure(String errMsg);
    }


    private FirebaseDBHelper() {}
    private static DatabaseReference getRootRef() {
        if(rootRef == null)
        {
            rootRef = FirebaseDatabase.getInstance().getReference();
        }
        return(rootRef);
    }

    private static DatabaseReference getUserRef() {
        if(userRef == null)
        {
            userRef = getRootRef().child(USER_COLLECTION_NAME);
        }
        return(userRef);
    }

    private static DatabaseReference getQuestionRef() {
        if(questionRef == null)
        {
            questionRef = getRootRef().child(QUETION_COLLECTION_NAME);
        }
        return(questionRef);
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
    }

    public static void getQuestionByCategory(String category, QuestionQueryCallback questionQueryCallback)
    {
        questionRef = getQuestionRef();

        questionRef.child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Question> questionArrayList = new ArrayList<Question>();
//                Log.d("QUESTION", snapshot.getValue(""))
                for(DataSnapshot snap : snapshot.getChildren()) {
                    Question q =(Question) snap.getValue(Question.class);
                    questionArrayList.add(q);
                }
               questionQueryCallback.onSuccess((Question) questionArrayList.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               questionQueryCallback.onFailure(Status.MSG_FIREBASE_ERROR + error.getMessage());
            }
        });
    }

}
