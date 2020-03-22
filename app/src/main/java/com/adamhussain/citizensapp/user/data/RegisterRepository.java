package com.adamhussain.citizensapp.user.data;

import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.adamhussain.citizensapp.user.ui.register.RegisterViewModel;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }



    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        LoginRepository.getInstance(new LoginDataSource()).setLoggedInUser(user);
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void register(LoggedInUser user, String password, RegisterViewModel registerViewModel) {
        // handle login
        dataSource.register(user, password,this,registerViewModel);

    }
    public void setResult(Result<LoggedInUser> result,RegisterViewModel registerViewModel ){
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        registerViewModel.setResult(result);
    }
}
