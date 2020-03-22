package com.adamhussain.citizensapp.user.data;

import com.adamhussain.citizensapp.user.data.model.LoggedInUser;
import com.adamhussain.citizensapp.user.ui.login.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;
    private OnUserUpdateListener userUpdateListener;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        if(user==null){
            return dataSource.isLoggedIn();
        }else {
            return true;
        }
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        this.user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser getUser() {
        return user;
    }

    public void login(String username, String password, LoginViewModel loginViewModel) {
        // handle login
        dataSource.login(username, password,this,loginViewModel);

    }
    public void setResult(Result<LoggedInUser> result, LoginViewModel loginViewModel){
        if (result instanceof Result.Success) {
            System.out.println("Hell yeah");
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
            if(userUpdateListener!=null) userUpdateListener.onUserUpdated(getUser());
        }
        loginViewModel.setResult(result);
    }

    public void fetchUser(LoginViewModel loginViewModel) {
        dataSource.fetchUserFromDB(loginViewModel,this);
    }

    public void setUserUpdateListener(OnUserUpdateListener userUpdateListener) {
        this.userUpdateListener = userUpdateListener;
    }

    public interface OnUserUpdateListener{
        void onUserUpdated(LoggedInUser user);
    }
}
