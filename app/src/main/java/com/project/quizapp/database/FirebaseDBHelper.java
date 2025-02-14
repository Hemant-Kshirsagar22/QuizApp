package com.project.quizapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FirebaseDBHelper {
    private static DatabaseReference rootRef = null;
    private static DatabaseReference userRef = null;
    private static DatabaseReference questionRef = null;

    private static FirebaseAuth firebaseAuth = null;
    private static String USER_COLLECTION_NAME = "Users";
    private static String QUETION_COLLECTION_NAME = "Questions";

    public static String ADMIN_USERNAME = "admin@gmail.com";

    public interface UserQueryCallback
    {
        void onSuccess(User user);
        void onFailure(String errMsg);
    }
    public interface GetAllUsers
    {
        void onSuccess(List<User> user);
        void onFailure(String errMsg);
    }


    public interface QuestionQueryCallback
    {
        void onSuccess(List<Question> question);
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

        createAdmin(new UserQueryCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d("ADMIN_CREATION", user.toString());
            }

            @Override
            public void onFailure(String errMsg) {
                Log.d("ADMIN_CREATETION", errMsg);
            }
        });
    }

    public static void insertUserWithGoogle(GoogleSignInAccount account, UserQueryCallback callback)
    {
        createAdmin(new UserQueryCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d("ADMIN_CREATION", user.toString());
            }

            @Override
            public void onFailure(String errMsg) {
                Log.d("ADMIN_CREATETION", errMsg);
            }
        });

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
                        user.setUserId(userId);
                        user.setGoogleUser(true);
                        Log.d("GOOGLE_LOGIN",user.toString());
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

    public static void getAllUsers(FirebaseDBHelper.GetAllUsers callback)
    {
        userRef = getUserRef();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> users = new ArrayList<User>();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    User user =(User) snap.getValue(User.class);
                    if(user.getEmail().equals(ADMIN_USERNAME))
                    {
                        continue;
                    }

                    users.add(user);
                }
                callback.onSuccess(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }

    public static void loginUser(String userName, String password, UserQueryCallback callback)
    {
        firebaseAuth = getFirebaseAuth();

        firebaseAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                    if(firebaseUser != null)
                    {
                        User user = new User();

                        user.setEmail(userName);
                        callback.onSuccess(user);
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

    public static void logout(Context context)
    {
        firebaseAuth = getFirebaseAuth();

        firebaseAuth.signOut();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN);
        googleSignInClient.signOut().addOnCompleteListener(task -> {
           if(task.isSuccessful())
           {
               Log.d("LOGOUT","GOOGLE LOGOUT SUCCESS");
           }
        });

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

    public static void deleteUser(String userName,UserQueryCallback callback)
    {
        firebaseAuth = getFirebaseAuth();
        userRef = getUserRef();


        userRef.orderByChild("email").equalTo(userName).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            private User user = null;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                         user = userSnapshot.getValue(User.class);
                    }
                    Log.d("DELETE_USER",user.toString());

                    if(user.getGoogleUser() == false || user.getGoogleUser() == null)
                    {
                        userRef.child(user.getUserId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                firebaseAuth = getFirebaseAuth();
                                firebaseAuth.signOut();
                                // removing user authentication
                                loginUser(user.getEmail(), user.getPassword(), new UserQueryCallback() {
                                    @Override
                                    public void onSuccess(User user) {
                                        firebaseAuth = getFirebaseAuth();
                                        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                firebaseAuth.signOut();
                                                loginUser(ADMIN_USERNAME, "123456", new UserQueryCallback() {
                                                    @Override
                                                    public void onSuccess(User user) {
                                                        callback.onSuccess(user);
                                                    }

                                                    @Override
                                                    public void onFailure(String errMsg) {

                                                    }
                                                });

                                            } else {
                                                callback.onFailure("FAILED TO DELETE USER");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(String errMsg) {

                                    }
                                });
                            }

                        });

                    }
                    else
                    {
                        // remove google user
                    }

                } else {
                    // Handle no user found
                    Log.d("UserInfo", "No user found with this email.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // remove firebase authentication

    }


    // for questions
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
               questionQueryCallback.onSuccess(questionArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               questionQueryCallback.onFailure(Status.MSG_FIREBASE_ERROR + error.getMessage());
            }
        });
    }

    private static void createAdmin(UserQueryCallback callback)
    {
        userRef = getUserRef();
        // admin user creation
        userRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the count of users
                Long[] userCount = {dataSnapshot.getChildrenCount()};

                if (userCount[0] == 0) {
                    User admin = new User("admin", "admin", "admin@gmail.com", "123456");

                    firebaseAuth = getFirebaseAuth();
                    firebaseAuth.createUserWithEmailAndPassword(admin.getEmail(),admin.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                if(firebaseUser != null) {
                                    String uid = firebaseUser.getUid();
                                    userRef.child(uid).setValue(admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            callback.onSuccess(admin);
                                        }
                                    });
                                }
                                else
                                {
                                    callback.onFailure("ERROR WHILE CREATEING ADMIN ");
                                }
                            }
                            else
                            {
                                Log.d("ADMIN_ERR", task.getException().getMessage());
                                callback.onFailure("ERROR WHILE CREATEING ADMIN ");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callback.onFailure(e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure("ADMIN CREATION FAILED");
            }
        });

        // admin creation end
    }
}
