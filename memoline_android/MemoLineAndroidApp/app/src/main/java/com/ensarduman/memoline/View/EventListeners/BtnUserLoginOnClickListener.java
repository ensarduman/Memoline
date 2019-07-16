package com.ensarduman.memoline.View.EventListeners;

import android.view.View;

import com.ensarduman.memoline.View.Runnables.LoginUserThread;
import com.ensarduman.memoline.View.UserLoginActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class BtnUserLoginOnClickListener implements View.OnClickListener {

    UserLoginActivity userLoginActivity;

    public BtnUserLoginOnClickListener(UserLoginActivity userLoginActivity) {
        this.userLoginActivity = userLoginActivity;
    }

    @Override
    public void onClick(View v) {

        try {
            userLoginActivity.StartLoading();
            StartCreateUserThread(userLoginActivity);
        }
        catch (Exception e)
        {
            userLoginActivity.StopLoading();
        }
    }

    private void StartCreateUserThread(UserLoginActivity userCreateActivity){
        LoginUserThread loginUserThread = new LoginUserThread(userCreateActivity);
        Thread thread = new Thread(loginUserThread);
        thread.start();
    }
}
