package com.adamhussain.citizensapp.home.emergency_service.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adamhussain.citizensapp.R;
import com.camerakit.CameraKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LiveCameraActivity extends AppCompatActivity {
    private CameraKitView cameraKitView;
    private FloatingActionButton recordBtn;

    private TextView status ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_camera);
        cameraKitView = findViewById(R.id.camera);
        recordBtn = findViewById(R.id.record);
        status = findViewById(R.id.status);

        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.getText().toString().equals("OFFLINE")){
                    status.setText("LIVE");
                    status.setBackgroundResource(R.color.holo_red_dark);
                    recordBtn.setImageResource(R.drawable.record_on);
                }else{
                    status.setText("OFFLINE");
                    status.setBackgroundResource(R.color.gray_dark);
                    recordBtn.setImageResource(R.drawable.record_off);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
