package com.adamhussain.citizensapp.user.ui.register;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.user.data.RegisterRepository;
import com.adamhussain.citizensapp.user.data.Result;
import com.adamhussain.citizensapp.user.data.model.LoggedInUser;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();
    private RegisterRepository registerRepository;

    RegisterViewModel(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public void register(String email, String password, String firstName, String lastName, String
            address) {
        // can be launched in a separate asynchronous job
         registerRepository.register(new LoggedInUser(
                firstName,
                lastName,
                email,
                address
        ),password,this);


    }
    public void setResult(Result<LoggedInUser> result ){
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            registerResult.setValue(new RegisterResult(new RegisteredUserView(data.getDisplayName())));
        } else {
            registerResult.setValue(new RegisterResult(R.string.register_failed));
        }
    }

    public void loginDataChanged(String email, String password, String passwordConfirm, String address, String firstName, String lastName) {
        if (!isEmailValid(email)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_email, null,  null, null, null, null));
        } else if (!isFirstNameValid(firstName)) {
            registerFormState.setValue(new RegisterFormState(null, null,  null, null, R.string.invalid_first_name, null));
        } else if (!isLastNameValid(lastName)) {
            registerFormState.setValue(new RegisterFormState(null, null, null, null,  null, R.string.invalid_last_name));
        } else if (!isAddressValid(address)) {
            registerFormState.setValue(new RegisterFormState(null, null, null,  R.string.invalid_address, null, null));
        } else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password, null,  null, null, null));
        } else if (!isPasswordConfirmValid(password,passwordConfirm)) {
            registerFormState.setValue(new RegisterFormState(null,  null,R.string.invalid_confirm_password,  null, null, null));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    private boolean isPasswordConfirmValid(String password , String passwordConfirm) {

        return password.equals(passwordConfirm);
    }

    // A placeholder username validation check
    private boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();

    }

    private boolean isFirstNameValid(String firstName) {
        if (firstName == null || firstName.contains("@")) {
            return false;
        }

        return !firstName.trim().isEmpty();

    }

    private boolean isLastNameValid(String lastName) {
        if (lastName == null || lastName.contains("@")) {
            return false;
        }
        return !lastName.trim().isEmpty();

    }

    private boolean isAddressValid(String address) {
        if (address == null) {
            return false;
        }
        return address.trim().length() > 10;

    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
