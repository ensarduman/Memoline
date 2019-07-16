package com.ensarduman.memoline.View.EventListeners;

import android.view.View;

import com.ensarduman.memoline.Model.UserModel;
import com.ensarduman.memoline.R;
import com.ensarduman.memoline.Util.ErrorMessageHelper;
import com.ensarduman.memoline.Util.RegularExpressionHelper;
import com.ensarduman.memoline.View.ActivityBase;
import com.ensarduman.memoline.View.Runnables.CreateUserThread;
import com.ensarduman.memoline.View.UserCreateActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class BtnUserCreateOnClickListener implements View.OnClickListener {

    UserCreateActivity userCreateActivity;

    public BtnUserCreateOnClickListener(UserCreateActivity userCreateActivity) {
        this.userCreateActivity = userCreateActivity;
    }

    @Override
    public void onClick(View v) {
        boolean isValid = true;
        ErrorMessageHelper errorMessageHelper = new ErrorMessageHelper(userCreateActivity);
        List<String> errorMessages = new ArrayList<>();

        try {

            UserModel userModel = userCreateActivity.getInsertedUser();

            if(!RegularExpressionHelper.isEmailValid(userModel.getEmail()))
            {
                errorMessages.add(errorMessageHelper.GetFieldErrorInvalidValue(userCreateActivity.getString(R.string.user_email_hint)));
                isValid = false;
            }

            if(!RegularExpressionHelper.isPasswordValid(userCreateActivity.getInsertedPassword())) {
                errorMessages.add(errorMessageHelper.GetPasswordFormatError());
                isValid = false;
            }else{
                if (!userCreateActivity.getIsPasswordsMatch()) {
                    errorMessages.add(errorMessageHelper.GetPasswordMatchError());
                    isValid = false;
                }
            }


            if(userModel.getName().equals(new String("")))
            {
                errorMessages.add(errorMessageHelper.GetFieldErrorInvalidValue(userCreateActivity.getString(R.string.user_create_name)));
                isValid = false;
            }

            if(userModel.getSurname().equals(new String("")))
            {
                errorMessages.add(errorMessageHelper.GetFieldErrorInvalidValue(userCreateActivity.getString(R.string.user_create_surname)));
                isValid = false;
            }

            if(isValid) {
                userCreateActivity.StartLoading();
                StartCreateUserThread(userCreateActivity);
            }
            else
            {
                ((ActivityBase)userCreateActivity).ShowToastMessagesLong(errorMessages);
            }
        }
        finally
        {
        }
    }

    private void StartCreateUserThread(UserCreateActivity userCreateActivity){
        CreateUserThread createUserThread = new CreateUserThread(userCreateActivity);
        Thread thread = new Thread(createUserThread);
        thread.start();
    }
}
