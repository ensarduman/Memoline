package com.ensarduman.memoline.View.Runnables;

import android.os.Handler;
import android.os.Message;

import com.ensarduman.memoline.Business.AuthenticationBusiness;
import com.ensarduman.memoline.DTO.UserDTO;
import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.View.UserCreateActivity;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class CreateUserThread implements Runnable {

    UserCreateActivity userCreateActivity;

    public CreateUserThread(UserCreateActivity userCreateActivity) {
        this.userCreateActivity = userCreateActivity;
    }

    @Override
    public void run() {
        Message message = new Message();
        message.what = 1;

        try {
            AuthenticationBusiness authenticationBusiness = new AuthenticationBusiness(userCreateActivity);
            UserModel userModel = userCreateActivity.getInsertedUser();
            String password = userCreateActivity.getInsertedPassword();

            //Dönüş değeri UserModel
            //Null ise activity tarafında false kabul edilir

            int res = 0;

            boolean isEmailExists = authenticationBusiness.IsUserExists(userModel.getEmail());

            if(!isEmailExists) {
                UserModel createdUser = authenticationBusiness.CreateUser(new UserDTO(userModel), password);

                if(createdUser != null)
                {
                    res = 1; //Kullanıcı oluşturuldu
                }
                else
                {
                    res = 2; //Başarısız oldu
                }
            }
            else
            {
                res = 3; //Kullanıcı mevcut
            }

            message.obj = res;
        }catch (Exception e){
            message.obj = 2;
        }

        //Activity'nin kaldırılması durumu için try catch kullanıldı
        try {
            Handler handler = userCreateActivity.getHandler();
            handler.sendMessage(message);
        }
        finally {
        }
    }
}
