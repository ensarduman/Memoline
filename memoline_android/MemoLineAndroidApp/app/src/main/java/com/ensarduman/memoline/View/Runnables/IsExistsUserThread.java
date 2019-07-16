package com.ensarduman.memoline.View.Runnables;

import android.os.Handler;
import android.os.Message;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.View.UserEmailActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class IsExistsUserThread implements Runnable {

    UserEmailActivity userEmailActivity;
    int messageWhat;

    public IsExistsUserThread(UserEmailActivity userEmailActivity, int messageWhat) {
        this.userEmailActivity = userEmailActivity;
        this.messageWhat = messageWhat;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what = messageWhat;

        try {
            AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(userEmailActivity);
            message.obj = authenticationBusiness.IsUserExists(userEmailActivity.getInsertedEmail());
        }catch (Exception e){
            message.obj = null;
        }

        //Activity'nin kaldırılması durumu için try catch kullanıldı
        try {
            Handler handler = userEmailActivity.getHandler();
            handler.sendMessage(message);
        }
        finally {
        }
    }
}
