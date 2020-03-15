package com.adamhussain.citizensapp.user.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer passwordConfirmError;
    @Nullable
    private Integer addressError;
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    private boolean isDataValid;

    public RegisterFormState(@Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer passwordConfirmError, @Nullable Integer addressError, @Nullable Integer firstNameError, @Nullable Integer lastNameError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.passwordConfirmError = passwordConfirmError;
        this.addressError = addressError;
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.isDataValid = false;
    }


    RegisterFormState(boolean isDataValid) {
        this.emailError = null;
        this.passwordError = null;
        this.passwordConfirmError = null;
        this.addressError = null;
        this.firstNameError = null;
        this.lastNameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getAddressError() {
        return addressError;
    }

    @Nullable
    public Integer getPasswordConfirmError() {
        return passwordConfirmError;
    }

    @Nullable
    public Integer getFirstNameError() {
        return firstNameError;
    }

    @Nullable
    public Integer getLastNameError() {
        return lastNameError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
