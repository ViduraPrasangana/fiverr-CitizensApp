package com.adamhussain.citizensapp.home.emergency_service.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.adamhussain.citizensapp.R;

import com.desmond.squarecamera.CameraActivity;
import com.google.android.material.button.MaterialButton;
import com.rengwuxian.materialedittext.MaterialEditText;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PoliceServiceFragment extends Fragment {

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private MaterialButton openCameraBtn;
    private MaterialButton sendBtn;
    private static final int REQUEST_CAMERA = 0;

    private Uri photoUri;

    private ImageView imageView;
    private MaterialEditText description;

    public PoliceServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_police_service, container, false);
        openCameraBtn = view.findViewById(R.id.openCamera);
        imageView = view.findViewById(R.id.image);
        sendBtn = view.findViewById(R.id.sendBtn);
        description = view.findViewById(R.id.description);

        openCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestForCameraPermission(view);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photoUri!=null){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Photo sent successfully",Toast.LENGTH_LONG).show();
                            imageView.setImageResource(R.drawable.empty_image);
                            description.setText("");
                        }
                    },2000);
                }else{
                    Toast.makeText(getContext(),"Take a photo before send",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void requestForCameraPermission(View view) {
        final String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(getContext(), permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Start CameraActivity
            Intent startCustomCameraIntent = new Intent(getContext(), CameraActivity.class);
            startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);

        }
    }

    // Start CameraActivity

    // Receive Uri of saved square photo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            photoUri = data.getData();
            imageView.setImageURI(photoUri);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
