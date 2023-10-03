package com.creditgaea.sample.credit.java.chat.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    private TimeUtils() {
    }

    public static String getTime(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static String getDate(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return dateFormat.format(new Date(milliseconds));
    }

    public static long getDateAsHeaderId(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        return Long.parseLong(dateFormat.format(new Date(milliseconds)));
    }


    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String getTimeInAmPm(String date) {
        SimpleDateFormat dateFormat;
        String dateTime = null;
        SimpleDateFormat convertDateFormat;
        if(date.contains(":")){
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            convertDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            convertDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        }


        try {
           dateTime =   convertDateFormat.format(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}