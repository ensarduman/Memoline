package com.ensarduman.memoline.View.EventListeners;

import android.view.View;

import com.ensarduman.memoline.R;
import com.ensarduman.memoline.Util.ErrorMessageHelper;
import com.ensarduman.memoline.Util.RegularExpressionHelper;
import com.ensarduman.memoline.View.ActivityBase;
import com.ensarduman.memoline.View.Runnables.IsExistsUserThread;
import com.ensarduman.memoline.View.UserEmailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ensarduman on 11.03.2018.
 */

public class BtnUserEmailOnClickListener implements View.OnClickListener {

    UserEmailActivity userEmailActivity;
    int messageWhat;

    public BtnUserEmailOnClickListener(UserEmailActivity userEmailActivity, int messageWhat) {
        this.userEmailActivity = userEmailActivity;
        this.messageWhat = messageWhat;
    }

    @Override
    public void onClick(View v) {
        boolean isValid = true;
        ErrorMessageHelper errorMessageHelper = new ErrorMessageHelper(userEmailActivity);
        List<String> errorMessages = new ArrayList<>();

        try {

            if(!RegularExpressionHelper.isEmailValid(userEmailActivity.getInsertedEmail()))
            {
                errorMessages.add(errorMessageHelper.GetFieldErrorInvalidValue(userEmailActivity.getString(R.string.user_email_hint)));
                isValid = false;
            }

            if(isValid) {
                userEmailActivity.StartLoading();
                StartIsExistsUserThread(userEmailActivity);
            }else{
                ((ActivityBase)userEmailActivity).ShowToastMessagesLong(errorMessages);
            }
        }
        catch (Exception e)
        {
            userEmailActivity.StopLoading();
        }
    }

    private void StartIsExistsUserThread(UserEmailActivity mainActivity){
        IsExistsUserThread isExistsUserThread = new IsExistsUserThread(userEmailActivity, messageWhat);
        Thread thread = new Thread(isExistsUserThread);
        thread.start();
    }
}
