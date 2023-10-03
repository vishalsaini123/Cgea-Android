package com.creditgaea.sample.credit.java.creditgea.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCalulation {

    public static String getDateInyyyyMMDD(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return dateFormat.format(new Date());
    }
}
