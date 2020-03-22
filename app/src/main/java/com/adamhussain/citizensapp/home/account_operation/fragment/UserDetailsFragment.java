package com.adamhussain.citizensapp.home.account_operation.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.user.data.LoginDataSource;
import com.adamhussain.citizensapp.user.data.LoginRepository;
import com.adamhussain.citizensapp.user.data.model.LoggedInUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailsFragment extends Fragment {

    private LoginRepository loginRepository;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView address;

    public UserDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.address);

        loginRepository = LoginRepository.getInstance(new LoginDataSource());
        updateData(loginRepository.getUser());

        loginRepository.setUserUpdateListener(new LoginRepository.OnUserUpdateListener() {
            @Override
            public void onUserUpdated(LoggedInUser user) {
                updateData(user);
            }
        });


        return view;
    }

    private void updateData(LoggedInUser user) {
        if(user==null) return;
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
        address.setText(user.getAddress());
    }

}
