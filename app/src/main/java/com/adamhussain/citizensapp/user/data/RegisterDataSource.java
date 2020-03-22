package com.adamhussain.citizensapp.user.data;

import androidx.annotation.NonNull;

import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.adamhussain.citizensapp.user.ui.register.RegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class RegisterDataSource {

    public void register(final LoggedInUser user, String password, final RegisterRepository registerRepository, final RegisterViewModel registerViewModel) {

        try {
            // TODO: handle loggedInUser authentication
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        FirebaseDatabase.getInstance().getReference().child("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    user.setUserId(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
                                    registerRepository.setResult(new Result.Success<>(user),registerViewModel);
                                }else {
                                    registerRepository.setResult(new Result.Error(task.getException()),registerViewModel);
                                }
                            }
                        });
                    }else {
                        registerRepository.setResult(new Result.Error(task.getException()),registerViewModel);
                    }
                }
            });

        } catch (Exception e) {
            FirebaseAuth.getInstance().signOut();
            registerRepository.setResult(new Result.Error(e),registerViewModel);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
