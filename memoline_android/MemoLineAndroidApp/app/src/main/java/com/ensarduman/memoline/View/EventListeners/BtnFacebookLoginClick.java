package com.ensarduman.memoline.View.EventListeners;

import com.ensarduman.memoline.View.Runnables.FacebookLoginUserThread;
import com.ensarduman.memoline.View.Runnables.LoginUserThread;
import com.ensarduman.memoline.View.UserEmailActivity;
import com.ensarduman.memoline.View.UserLoginActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

/**
 * Created by duman on 27/03/2018.
 */

public class BtnFacebookLoginClick implements FacebookCallback<LoginResult> {
    UserEmailActivity userEmailActivity;

    public BtnFacebookLoginClick(UserEmailActivity userEmailActivity) {
        this.userEmailActivity = userEmailActivity;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        userEmailActivity.StartLoading();
        String facebookUserId = loginResult.getAccessToken().getUserId();
        String accessToken = loginResult.getAccessToken().getToken();

        try {
            userEmailActivity.StartLoading();
            StartLoginUserThread(userEmailActivity, facebookUserId, accessToken);
        }
        catch (Exception e)
        {
            userEmailActivity.StopLoading();
        }

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    private void StartLoginUserThread(UserEmailActivity userEmailActivity, String facebookUserId, String accessToken){
        FacebookLoginUserThread loginUserThread = new FacebookLoginUserThread(userEmailActivity, facebookUserId, accessToken);
        Thread thread = new Thread(loginUserThread);
        thread.start();
    }
}
