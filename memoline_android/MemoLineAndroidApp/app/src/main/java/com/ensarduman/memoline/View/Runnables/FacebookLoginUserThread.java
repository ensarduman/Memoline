package com.ensarduman.memoline.View.Runnables;

import android.os.Handler;
import android.os.Message;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.Business.SynchronizationBusiness;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.Util.EnumLoginType;
import com.ensarduman.memoline.View.UserEmailActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class FacebookLoginUserThread implements Runnable {

    UserEmailActivity userEmailActivity;
    String facebookUserId;
    String accessToken;
    EnumLoginType enumLoginType;

    public FacebookLoginUserThread(UserEmailActivity userEmailActivity, String facebookUserId, String accessToken) {
        this.userEmailActivity = userEmailActivity;
        this.facebookUserId = facebookUserId;
        this.accessToken = accessToken;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what = EnumLoginType.Facebook.getValue();

        try {
            AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(userEmailActivity);

            //Dönüş değeri UserModel
            //Null ise activity tarafında false kabul edilir
            UserModel userModel = authenticationBusiness.FacebookLogin(facebookUserId, accessToken);

            message.obj = userModel != null;

            SynchronizationBusiness synchronizationBusiness = new SynchronizationBusiness(userEmailActivity);
            synchronizationBusiness.GetUpdates();
        }
        catch (Exception e){
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
