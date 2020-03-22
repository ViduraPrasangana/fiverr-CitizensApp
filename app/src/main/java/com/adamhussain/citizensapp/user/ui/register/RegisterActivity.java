package com.adamhussain.citizensapp.user.ui.register;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.adamhussain.citizensapp.home.MainActivity;
import com.adamhussain.citizensapp.R;
import com.jgabrielfreitas.core.BlurImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int BLUR_FINAL_VALUE_LOGIN = 20;
    private static int BLUR_CURRENT_VALUE = 0;
    private RegisterViewModel registerViewModel;
    private BlurImageView bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = ViewModelProviders.of(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText addressEditText = findViewById(R.id.address);
        final EditText firstNameEditText = findViewById(R.id.firstName);
        final EditText lastNameEditText = findViewById(R.id.lastName);
        final EditText passwordConfirmEditText = findViewById(R.id.passwordConfirm);
        final Button registerButton = findViewById(R.id.register);
        final Button loginButton = findViewById(R.id.login);
        final Button reset = findViewById(R.id.reset);
        bg = findViewById(R.id.bg);
        registerButton.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeBlur();
            }
        },5000);
        registerViewModel.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {


            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getEmailError() != null) {
                    emailEditText.setError(getString(registerFormState.getEmailError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getAddressError() != null) {
                    addressEditText.setError(getString(registerFormState.getAddressError()));
                }
                if (registerFormState.getFirstNameError() != null) {
                    firstNameEditText.setError(getString(registerFormState.getFirstNameError()));
                }
                if (registerFormState.getLastNameError() != null) {
                    lastNameEditText.setError(getString(registerFormState.getLastNameError()));
                }
                if (registerFormState.getPasswordConfirmError() != null) {
                    passwordConfirmEditText.setError(getString(registerFormState.getPasswordConfirmError()));
                }
                if(registerFormState.isDataValid()) registerButton.setEnabled(true);
            }
        });

        registerViewModel.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
//                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showRegisterFailed(registerResult.getError());
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
                if (registerResult.getSuccess() != null) {
                    updateUiWithUser();
                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful

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
                registerViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        passwordConfirmEditText.getText().toString(),
                        addressEditText.getText().toString(),
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString()
                );
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordConfirmEditText.addTextChangedListener(afterTextChangedListener);
        addressEditText.addTextChangedListener(afterTextChangedListener);
        firstNameEditText.addTextChangedListener(afterTextChangedListener);
        lastNameEditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerViewModel.register(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        addressEditText.getText().toString());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEditText.setText("");
                passwordConfirmEditText.setText("");
                passwordEditText.setText("");
                addressEditText.setText("");
                firstNameEditText.setText("");
                lastNameEditText.setText("");
            }
        });
    }

    private void updateUiWithUser() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showRegisterFailed(@StringRes Integer errorString) {
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
