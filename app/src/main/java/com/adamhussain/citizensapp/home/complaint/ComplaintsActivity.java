package com.adamhussain.citizensapp.home.complaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.adamhussain.citizensapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ComplaintsActivity extends AppCompatActivity {

    private EditText topic;
    private EditText description;

    private MaterialButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);

        topic = findViewById(R.id.topic);
        description = findViewById(R.id.description);
        sendBtn = findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topic.getText()!=null
                        && !topic.getText().toString().equals("")
                        && description.getText()!=null
                        && !description.getText().toString().equals("")){
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    email = email.replace("@","_").replace(".","_");
                    HashMap<String,String> data = new HashMap<String,String>();
                    data.put("topic",topic.getText().toString());
                    data.put("description",description.getText().toString());
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("complaints")
                            .child(email)
                            .setValue(data)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        toast("Complaint sent successfully");
                                    }else{
                                        toast("Something went wrong : "+task.getException());
                                    }
                                }
                            });
                }
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
