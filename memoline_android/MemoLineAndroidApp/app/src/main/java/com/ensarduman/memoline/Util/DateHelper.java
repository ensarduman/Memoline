package com.ensarduman.memoline.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by duman on 19/02/2018.
 */

public class DateHelper {
    private static String displayFormat = "yyyy-MM-dd HH:mm";
    private static String convertFormat = "yyyy-MM-dd HH:mm:ss";

    public static String getDateString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat(displayFormat);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public static Date String2Date(String datestr){
        SimpleDateFormat sdf=new SimpleDateFormat(convertFormat);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = sdf.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date GetUTCNow()
    {
        return GetUTCDate(new Date());
    }

    public static Date GetUTCDate(Date date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(convertFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formatted = dateFormat.format(date);
        return String2Date(formatted);
    }

    public static Date FromUTCDate(Date utcDate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(convertFormat);
        dateFormat.setTimeZone(TimeZone.getDefault());
        String formatted = dateFormat.format(utcDate);
        return String2Date(formatted);
    }
}
