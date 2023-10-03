package com.creditgaea.sample.credit.java.creditgea.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 12/3/2016.
 */

public class SessionManager {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String PrefsFile = "PrefsFile";

    public SessionManager(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PrefsFile, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void saveUserName(String name)
    {
        editor.putString(AppConstants.PREFSUSERNAME,name);
        editor.commit();
    }

    public String getUserName()
    {
        return sharedPreferences.getString(AppConstants.PREFSUSERNAME,"");
    }

}
