package com.ensarduman.memoline.View.Runnables;

import android.os.Handler;
import android.os.Message;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.View.UserDetailActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class LogoutUserThread implements Runnable {

    UserDetailActivity userDetailActivity;

    public LogoutUserThread(UserDetailActivity userDetailActivity) {
        this.userDetailActivity = userDetailActivity;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what = 1;

        try {
            AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(userDetailActivity);
            authenticationBusiness.LogoutUser();

            //Dönüş değeri UserModel
            //Null ise activity tarafında false kabul edilir
            message.obj = true;
        }
        catch (Exception e){
            message.obj = null;
        }

        //Activity'nin kaldırılması durumu için try catch kullanıldı
        try {
            Handler handler = userDetailActivity.getHandler();
            handler.sendMessage(message);
        }
        finally {
        }
    }
}
