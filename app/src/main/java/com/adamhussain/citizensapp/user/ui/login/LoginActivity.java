package com.adamhussain.citizensapp.user.ui.login;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.adamhussain.citizensapp.home.MainActivity;
import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.user.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jgabrielfreitas.core.BlurImageView;

public class LoginActivity extends AppCompatActivity {

    private static final int BLUR_FINAL_VALUE_LOGIN = 20;
    private static int BLUR_CURRENT_VALUE = 0;
    private LoginViewModel loginViewModel;
    private  BlurImageView bg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final Button forgotPassButton = findViewById(R.id.forgot_password);
        final LinearLayout emailCon = findViewById(R.id.emailCon);
        final LinearLayout passwordCon = findViewById(R.id.passwordCon);
        bg = findViewById(R.id.bg);
//        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeBlur();
            }
        },5000);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    emailEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser();
                }
                setResult(Activity.RESULT_OK);

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<View, String> p1 = Pair.create((View) emailEditText, "email");
                Pair<View, String> p2 = Pair.create((View) passwordEditText, "password");
                Pair<View, String> p3 = Pair.create((View) loginButton, "login");
                Pair<View, String> p4 = Pair.create((View) registerButton, "register");
                Pair<View, String> p5 = Pair.create((View) bg, "bgImage");
                Pair<View, String> p6 = Pair.create((View) emailCon, "emailCon");
                Pair<View, String> p7 = Pair.create((View) passwordCon, "passwordCon");
                ActivityOptionsCompat option = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(LoginActivity.this, p1, p2, p3, p4, p5, p6, p7);
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent, option.toBundle());
            }
        });

        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (
                        emailEditText.getText() != null
                                && Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()
                ) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Email sent successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Something went wrong : " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Password reset link sent to your email", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (loginViewModel.isLoggedIn()) {
            loginViewModel.fetchUser();
            updateUiWithUser();
        }
        ;
    }

    private void updateUiWithUser() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void initializeBlur(){
        new ValueAnimator();
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, BLUR_FINAL_VALUE_LOGIN);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                System.out.println(valueAnimator.getAnimatedValue());
                int value = (int) valueAnimator.getAnimatedValue();
                bg.setBlur(value);
                BLUR_CURRENT_VALUE = value;
            }
        });
        valueAnimator.setDuration(5000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                valueAnimator.start();

            }
        }, 100);
    }
}
