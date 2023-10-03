package com.creditgaea.sample.credit.java.creditgea.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 11/27/2016.
 */

public class CreditGaeaDb extends SQLiteOpenHelper {
    public static final String DBNAME="CreditGaea.db";
    public static final String TABLENAME="CreditGaeaTab";
    public static final String C_USERNAME="C_USERNAME";
    public static final String C_DATETRAVEL="C_DATETRAVEL";
    public static final String C_TRAVELTIME="C_TRAVELTIME";
    public static final String C_TRAVELMODE="C_TRAVELMODE";
    public static final String C_TRAVELMODE_TYPE="C_TRAVELMODE_TYPE";
    public static final String C_TRAVELSCORE="C_TRAVELSCORE";


    public static final String TRANSACTION="PastTransaction";
    public static final String TRANSACTION_User_Id="user_id";
    public static final String TRANSACTION_SENDER_ID="sender_id";
    public static final String TRANSACTION_SENDER_EMAIL="sender_email";
    public static final String TRANSACTION_SCORE="score";
    public static final String TRANSACTION_RECEIVER_EMAIL="receiver_email";
    public static final String TRANSACTION_RECEIVER_ID="receiver_id";
    public static final String TRANSACTION_DATE="date";

    public static final int dbver=1;

    public CreditGaeaDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String str_CreatePastTrans="Create table PastTransaction(_id integer primary key autoincrement,user_id text,sender_id text," +
               "sender_email text,score integer,receiver_id text,receiver_email text,date text)";
        sqLiteDatabase.execSQL(str_CreatePastTrans);

        String str_CreateTab="Create table CreditGaeaTab(_id integer primary key autoincrement,C_USERNAME text,C_DATETRAVEL text," +
                "C_TRAVELTIME text,C_TRAVELMODE text,C_TRAVELMODE_TYPE text,C_TRAVELSCORE integer)";
        sqLiteDatabase.execSQL(str_CreateTab);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table CreditGaeaTab");
        sqLiteDatabase.execSQL("Drop table PastTransaction");
        onCreate(sqLiteDatabase);
    }



}
