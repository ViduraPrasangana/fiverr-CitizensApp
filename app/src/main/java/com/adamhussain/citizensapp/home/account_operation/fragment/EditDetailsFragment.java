package com.adamhussain.citizensapp.home.account_operation.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDetailsFragment extends Fragment {
    private ExpandableLayout questionLayout;
    private ExpandableLayout languagesLayout;
    private ExpandableLayout changePasswordLayout;

    private LinearLayout questionsExpBtn;
    private LinearLayout languagesExpBtn;
    private LinearLayout changePassExpBtn;

    private MaterialButton saveQuestionBtn;
    private MaterialButton changePasswordBtn;

    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText currentPass;
    private EditText newPass;
    private EditText newPassConfirm;

    private PowerSpinnerView question1;
    private PowerSpinnerView question2;
    private PowerSpinnerView question3;
    private PowerSpinnerView language;

    public EditDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_details, container, false);

        questionsExpBtn = view.findViewById(R.id.questions_exp_button);
        questionLayout = view.findViewById(R.id.exp_lay_questions);
        questionsExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleQuestions();
            }
        });
        languagesExpBtn = view.findViewById(R.id.languages_exp_button);
        languagesLayout = view.findViewById(R.id.exp_lay_languages);
        languagesExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLanguages();
            }
        });
        changePassExpBtn = view.findViewById(R.id.change_pass_exp_button);
        changePasswordLayout = view.findViewById(R.id.exp_lay_change_pass);
        changePassExpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleChangePass();
            }
        });

        answer1 = view.findViewById(R.id.answer_1);
        answer2 = view.findViewById(R.id.answer_2);
        answer3 = view.findViewById(R.id.answer_3);
        question1 = view.findViewById(R.id.question_1);
        question2 = view.findViewById(R.id.question_2);
        question3 = view.findViewById(R.id.question_3);
        retrieveData();

        language = view.findViewById(R.id.language);
        language.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, String o) {
                toast(o);
                changeLanguage(o);
            }
        });

        currentPass = view.findViewById(R.id.current_pass);
        newPass = view.findViewById(R.id.new_pass);
        newPassConfirm = view.findViewById(R.id.new_pass_confirm);

        saveQuestionBtn = view.findViewById(R.id.save_questions_btn);
        changePasswordBtn = view.findViewById(R.id.change_pass_btn);

        saveQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuestions();
            }
        });
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        return view;
    }

    private void toggleQuestions() {
        if (questionLayout.isExpanded()) {
            questionLayout.collapse();
        } else {
            questionLayout.expand();
            languagesLayout.collapse();
            changePasswordLayout.collapse();
        }
    }

    private void toggleLanguages() {
        if (languagesLayout.isExpanded()) {
            languagesLayout.collapse();
        } else {
            questionLayout.collapse();
            languagesLayout.expand();
            changePasswordLayout.collapse();
        }
    }

    private void toggleChangePass() {
        if (changePasswordLayout.isExpanded()) {
            changePasswordLayout.collapse();
        } else {
            questionLayout.collapse();
            languagesLayout.collapse();
            changePasswordLayout.expand();
        }
    }

    private void retrieveData() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference()
                .child("settings")
                .child(userId);

        reference.child("securityQuestions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                if (map == null) return;
                question1.setText(map.get("question_1"));
                question2.setText(map.get("question_2"));
                question3.setText(map.get("question_3"));
                answer1.setText(map.get("answer_1"));
                answer2.setText(map.get("answer_2"));
                answer3.setText(map.get("answer_3"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("language").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                language.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveQuestions() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String, String> data = new HashMap<>();
        data.put("question_1", question1.getText().toString());
        data.put("answer_1", answer1.getText().toString());
        data.put("question_2", question2.getText().toString());
        data.put("answer_2", answer2.getText().toString());
        data.put("question_3", question3.getText().toString());
        data.put("answer_3", answer3.getText().toString());
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("settings")
                .child(userId)
                .child("securityQuestions")
                .setValue(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast("Data save successful");
                        } else {
                            toast("Data save error : " + task.getException());
                        }
                    }
                });
    }

    private void changeLanguage(String language) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("settings")
                .child(userId)
                .child("language")
                .setValue(language);
    }

    private void changePassword() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, currentPass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (isValidPass()) {
                                FirebaseAuth
                                        .getInstance()
                                        .getCurrentUser()
                                        .updatePassword(newPass.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    toast("Password change successful");
                                                } else {
                                                    toast("Password change failed : " + task.getException());
                                                }
                                            }
                                        });
                            }
                        } else {
                            toast("Password change failed : " + task.getException());
                        }
                    }
                });


    }

    private boolean isValidPass() {
        if (newPass.getText() == null || newPass.getText().toString().equals("") || newPass.getText().length() <= 5) {
            newPass.setError("should be longer than 5 characters");
            return false;
        } else if (!newPass.getText().toString().equals(newPassConfirm.getText().toString())) {
            newPassConfirm.setError("Passwords should be match");
            return false;
        }
        return true;
    }

    private void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
