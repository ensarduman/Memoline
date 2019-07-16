package com.ensarduman.memoline.View.Runnables;

import android.os.Handler;
import android.os.Message;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.Business.SynchronizationBusiness;
import com.ensarduman.memoline.Util.EnumLoginType;
import com.ensarduman.memoline.View.UserLoginActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class LoginUserThread implements Runnable {

    UserLoginActivity userLoginActivity;

    public LoginUserThread(UserLoginActivity userLoginActivity) {
        this.userLoginActivity = userLoginActivity;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what = EnumLoginType.Basic.getValue();

        try {
            AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(userLoginActivity);
            String email = userLoginActivity.getInsertedEmail();
            String password = userLoginActivity.getInsertedPassword();

            //Dönüş değeri UserModel
            //Null ise activity tarafında false kabul edilir
            message.obj = authenticationBusiness.Login(email, password);

            SynchronizationBusiness synchronizationBusiness = new SynchronizationBusiness(userLoginActivity);
            synchronizationBusiness.GetUpdates();
        }
        catch (Exception e){
            message.obj = null;
        }

        //Activity'nin kaldırılması durumu için try catch kullanıldı
        try {
            Handler handler = userLoginActivity.getHandler();
            handler.sendMessage(message);
        }
        finally {
        }
    }
}
