package com.adamhussain.citizensapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.home.account_operation.AccountSettingsActivity;
import com.adamhussain.citizensapp.home.complaint.ComplaintsActivity;
import com.adamhussain.citizensapp.home.emergency_service.EmergencyActivity;
import com.adamhussain.citizensapp.home.general.GeneralActivity;
import com.adamhussain.citizensapp.home.payment.PaymentsActivity;
import com.adamhussain.citizensapp.user.data.LoginDataSource;
import com.adamhussain.citizensapp.user.data.LoginRepository;
import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.adamhussain.citizensapp.user.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LoggedInUser loggedInUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private LoginRepository loginRepository;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private LinearLayout logoutBtn;

    private  TextView greeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginRepository = LoginRepository.getInstance(new LoginDataSource());
        loggedInUser = loginRepository.getUser();

        authStateListener  = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

         greeting = findViewById(R.id.greeting);


        recyclerView = findViewById(R.id.recycler_view);
        List<MenuItem> menuItems =  Arrays.asList(

                new MenuItem(
                        "General",
                        GeneralActivity.class,
                        R.drawable.menu_bg_general,
                        ResourcesCompat.getColor(getResources(),R.color.white,null)
                ) ,
                new MenuItem(
                        "Accounts Settings",
                        AccountSettingsActivity.class,
                        R.drawable.menu_bg_account_settings,
                        ResourcesCompat.getColor(getResources(), R.color.white,null)
                ),
                new MenuItem(
                        "Emergency",
                        EmergencyActivity.class,
                        R.drawable.menu_bg_emergency,
                        ResourcesCompat.getColor(getResources(),R.color.white,null)
                ),
                new MenuItem(
                        "Complaints",
                        ComplaintsActivity.class,
                        R.drawable.menu_bg_complaint,
                        ResourcesCompat.getColor(getResources(),R.color.white,null)
                ),
                new MenuItem(
                        "Pay Bills",
                        PaymentsActivity.class,
                        R.drawable.menu_bg_paymets,
                        ResourcesCompat.getColor(getResources(),R.color.white,null)
                )
        );
        adapter = new MenuAdapter(menuItems,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


    }

    private void logout(){
        loginRepository.logout();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        greeting.setText(Greeting.getGreeting());
    }
}
