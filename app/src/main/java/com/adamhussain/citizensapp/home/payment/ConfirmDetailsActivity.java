package com.adamhussain.citizensapp.home.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.user.data.LoginDataSource;
import com.adamhussain.citizensapp.user.data.LoginRepository;
import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ConfirmDetailsActivity extends AppCompatActivity {

    private MaterialEditText name;
    private MaterialEditText address;
    private MaterialEditText drivingLicenseNo;
    private MaterialEditText answer;

    private CardView questionContainer;

    private PowerSpinnerView questions;

    private List<String> questionData;
    private List<String> answerData;

    private MaterialButton confirmBtn;

    private PayItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);

        item = getIntent().getParcelableExtra("item");

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        drivingLicenseNo = findViewById(R.id.driving_license);
        answer = findViewById(R.id.answer);

        questionContainer = findViewById(R.id.question_container);

        confirmBtn = findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmData();
            }
        });

        questions = findViewById(R.id.question);
        loadData();

    }

    private void loadData() {
        LoggedInUser user = LoginRepository.getInstance(new LoginDataSource()).getUser();
        if (user == null) return;
        name.setText(user.getDisplayName());
        address.setText(user.getAddress());

        FirebaseDatabase.getInstance()
                .getReference()
                .child("settings")
                .child(user.getUserId())
                .child("securityQuestions")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();

                        if (map == null) {
                            questionContainer.setVisibility(View.GONE);
                            questionContainer.invalidate();
                            return;
                        } else {
                            questionContainer.setVisibility(View.VISIBLE);
                            questionContainer.invalidate();
                        }
                        questionData = new ArrayList<>();
                        answerData = new ArrayList<>();
                        if (map.get("question_1") != null && !(map.get("question_1").equals(""))) {
                            questionData.add(map.get("question_1"));
                            answerData.add(map.get("answer_1"));
                        }
                        if (map.get("question_2") != null && !(map.get("question_2").equals(""))) {
                            questionData.add(map.get("question_2"));
                            answerData.add(map.get("answer_2"));
                        }
                        if (map.get("question_3") != null && !(map.get("question_3").equals(""))) {
                            questionData.add(map.get("question_3"));
                            answerData.add(map.get("answer_3"));
                        }
                        questions.setItems(questionData);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void confirmData() {
        if (validateData()) {
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid details", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateData() {
        if (name.getText() == null || name.getText().toString().equals("")) return false;
        if (address.getText() == null || address.getText().toString().equals("")) return false;
        if (drivingLicenseNo.getText() == null || drivingLicenseNo.getText().toString().equals(""))
            return false;
        if (questions.getSelectedIndex() < 0) return false;
        if (questionContainer.getVisibility() != View.GONE
                && answer.getText() != null
                && !answer.getText().toString().equals("")
                && questions.getSelectedIndex() >= 0
        ) {
            if (answer.getText().toString().equals(answerData.get(questions.getSelectedIndex()))) {
            } else {
                return false;
            }
        }

        return true;
    }
}
