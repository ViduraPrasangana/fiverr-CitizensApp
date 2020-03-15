package com.adamhussain.citizensapp.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.user.data.LoginDataSource;
import com.adamhussain.citizensapp.user.data.LoginRepository;
import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private LoggedInUser loggedInUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView sam = findViewById(R.id.sam);
        LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
        loggedInUser = loginRepository.getUser();

       sam.setText(loggedInUser.getDisplayName());
    }

    private void logout(){

    }
}
