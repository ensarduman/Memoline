package com.ensarduman.memoline.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ensarduman on 18.03.2018.
 */

public class RegularExpressionHelper {
    public static boolean isValid(String value, String expression) {
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password)
    {
        return isValid(password, "^([a-zA-Z0-9]{6,18}?)$");
    }

    public static boolean isEmailValid(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
