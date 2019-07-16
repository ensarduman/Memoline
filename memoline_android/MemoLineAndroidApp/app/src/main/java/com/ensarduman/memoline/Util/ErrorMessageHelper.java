package com.ensarduman.memoline.Util;

import android.content.Context;

import com.ensarduman.memoline.R;

/**
 * Created by ensarduman on 18.03.2018.
 */

public class ErrorMessageHelper {
    Context context;

    public ErrorMessageHelper(Context context) {
        this.context = context;
    }

    public String GetFieldErrorInvalidValue(String fieldName)
    {
        return fieldName + " " + context.getString(R.string.toastmessage_invalid_value);
    }

    public String GetFieldErrorUserAlreadyExists()
    {
        return context.getString(R.string.toastmessage_user_already_exists);
    }

    public String GetPasswordMatchError()
    {
        return context.getString(R.string.toastmessage_passwords_not_match);
    }

    public String GetPasswordFormatError()
    {
        return context.getString(R.string.toastmessage_passwords_invalid);
    }

    public String GetEmailWrongError()
    {
        return context.getString(R.string.toastmessage_email_wrong);
    }

    public String GetProcessError()
    {
        return context.getString(R.string.toastmessage_process_failed);
    }
}
