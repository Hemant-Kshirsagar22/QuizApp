package com.project.quizapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

    private static FirebaseAuth firebaseAuth = null;
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

    private static FirebaseAuth getFirebaseAuth()
    {
        if(firebaseAuth == null)
        {
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return(firebaseAuth);
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
        firebaseAuth = getFirebaseAuth();

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    if(firebaseUser != null)
                    {
                        String userId = firebaseUser.getUid();
                        user.setUserId(userId);


                        userRef = getUserRef();

                        // admin user creation
                        userRef.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               // Get the count of users
                               Long[] userCount = {dataSnapshot.getChildrenCount()};

                               if (userCount[0] == 0) {
                                   User admin = new User("admin", "admin", "admin@gmail.com", "123");
                                   user.setUserId(userCount[0].toString());
                                   userRef.child(userCount[0].toString()).setValue(admin);
                                   userCount[0] += 1;
                               }
                           }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                callback.onFailure("ADMIN CREATION FAILED");
                            }
                        });

                        // admin creation end

                        userRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    callback.onSuccess(user);
                                }
                                else
                                {
                                    callback.onFailure(Status.MSG_INSERT_FAILED);
                                }
                            }
                        });

                    }
                    else
                    {
                        callback.onFailure(task.getException().getMessage());
                    }
                }
                else
                {
                    callback.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public static void insertUserWithGoogle(GoogleSignInAccount account, UserQueryCallback callback)
    {
        firebaseAuth = getFirebaseAuth();
        Log.d("GOOGLE_SIGNIN", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        if(credential == null)
        {
            Log.d("CREDITIAL","NULL");
        }

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            private User user;
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser != null)
                    {
                        String username = firebaseUser.getDisplayName();

                        String[] name = username.split(" ");
                        Log.d("NAME LENGTH", name.length + "");
                        if(name.length >= 2) {
                            user = new User(name[0], name[1], firebaseUser.getEmail(), null);
                        }
                        else
                        {
                            user = new User(username,null, firebaseUser.getEmail(), null);
                        }
                        String userId = firebaseUser.getUid();

                        userRef = getUserRef();
                        userRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    callback.onSuccess(user);
                                }
                                else
                                {
                                    callback.onFailure(Status.MSG_INSERT_FAILED);
                                }
                            }
                        });
                    }
                    else
                    {
                        Log.d("GOOGLE_SIGNUP_ERR",task.getException().getMessage());
                        callback.onFailure("GOOGLE si " + task.getException().getMessage());
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("GOOGLE_SIGNUP_ERR",e.getMessage());
                callback.onFailure(e.getMessage());
            }
        });
    }

    public static void getUser(UserQueryCallback callback)
    {
        firebaseAuth = getFirebaseAuth();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null)
        {
            String userId = currentUser.getUid();

            userRef = getUserRef();

            userRef.child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        User user = task.getResult().getValue(User.class);

                        callback.onSuccess(user);
                    }
                    else
                    {
                        callback.onFailure("FIREBASE USER READ PROBLEM");
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure(Status.MSG_FIREBASE_ERROR + e.getMessage());
                }
            });
        }
        else
        {
            callback.onSuccess(null);
        }

    }

    public static void loginUser(String userName, String password, UserQueryCallback callback)
    {
        firebaseAuth = getFirebaseAuth();

        firebaseAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if(user != null)
                    {

                        callback.onSuccess(null);
                    }
                    else
                    {
                        callback.onFailure(Status.MSG_LOGIN_PASS_NOT_MATCH);
                    }
                }
                else
                {
                    callback.onFailure(Status.MSG_FIREBASE_ERROR + task.getException().getMessage());
                }
            }
        });
    }

    public static void logout()
    {
        firebaseAuth = getFirebaseAuth();

        firebaseAuth.signOut();
    }

    public static boolean isUserLoggedIn()
    {
        firebaseAuth = getFirebaseAuth();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser == null)
        {
            return false;
        }
        else
        {
            return true;
        }
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
//               questionQueryCallback.onSuccess((Question) questionArrayList.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
     //          questionQueryCallback.onFailure(Status.MSG_FIREBASE_ERROR + error.getMessage());
            }
        });
    }

}
