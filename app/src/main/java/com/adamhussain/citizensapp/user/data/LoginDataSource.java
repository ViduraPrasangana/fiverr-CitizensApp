package com.adamhussain.citizensapp.user.data;

import androidx.annotation.NonNull;

import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.adamhussain.citizensapp.user.ui.login.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private final String TAG = "LOGIN_DATA_SOURCE";

    public void login(String username, String password, final LoginRepository loginRepository, final LoginViewModel loginViewModel) {
        try {
            // TODO: handle loggedInUser authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               fetchUserFromDB(loginViewModel,loginRepository);
                            } else {
                                loginRepository.setResult(new Result.Error(new IOException("Sign with email is failed")),loginViewModel);

                            }
                        }
                    });

            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null)
                        loginRepository.setLoggedInUser(null);
                }
            };


        } catch (Exception e) {
            FirebaseAuth.getInstance().signOut();
        }
    }

    public void fetchUserFromDB(final LoginViewModel loginViewModel, final LoginRepository loginRepository){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(Objects
                        .requireNonNull(FirebaseAuth
                                .getInstance()
                                .getUid()
                        )
                )
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            loginRepository.setResult(new Result.Success<>(dataSnapshot.getValue(LoggedInUser.class)),loginViewModel);
                        }else {
                            loginRepository.setResult(new Result.Error(new IOException("User data is empty")),loginViewModel);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser()!=null;
    }
}
