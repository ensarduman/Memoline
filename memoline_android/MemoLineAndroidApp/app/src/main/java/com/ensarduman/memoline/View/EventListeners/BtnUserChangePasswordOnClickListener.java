package com.ensarduman.memoline.View.EventListeners;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.View.MainActivity;
import com.ensarduman.memoline.View.Runnables.LoginUserThread;
import com.ensarduman.memoline.View.UserChangePassword;
import com.ensarduman.memoline.View.UserDetailActivity;
import com.ensarduman.memoline.View.UserLoginActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class BtnUserChangePasswordOnClickListener implements View.OnClickListener {

    UserDetailActivity userDetailActivity;

    public BtnUserChangePasswordOnClickListener(UserDetailActivity userDetailActivity) {
        this.userDetailActivity = userDetailActivity;
    }

    @Override
    public void onClick(View v) {

        try {
            GoToChangePassword();
        }
        catch (Exception e)
        {
            userDetailActivity.StopLoading();
        }
    }

    private void GoToChangePassword()
    {
        Intent intent = new Intent(userDetailActivity, UserChangePassword.class);
        Bundle b = new Bundle();
        intent.putExtras(b);
        userDetailActivity.startActivity(intent);
    }
}
