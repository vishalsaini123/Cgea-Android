package com.creditgaea.sample.credit.java.creditgea.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.creditgaea.sample.credit.java.creditgea.activity.MainActivity;
import com.creditgaea.sample.credit.java.creditgea.activity.PastLogsActivity;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.webservice.UploadCarbonLogToServer;
import com.creditgaea.sample.credit.java.webservice.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/26/2016.
 */

public class AppConstants {

    public static final String USERNAME = "USERNAME";
    public static final String TRAVELDATE = "TRAVELDATE";
    public static final String PREFSUSERNAME = "PREFSUSERNAME";

    public static final int score_ground_gasoline = -1;
    public static final int score_ground_diesel = 0;
    public static final int score_ground_hybrid = 0;
    public static final int score_ground_electric = +1;
    public static final int score_ground_bicycle = +1;

    public static final int score_home_electricity = -1;
    public static final int score_home_mobile = 0;
    public static final int score_home_solar = +1;

    public static final int score_water_gasoline = -1;
    public static final int score_water_diesel = 0;
    public static final int score_water_wind = +1;
    public static final int score_water_solar = +1;

    public static final int score_air_jet = -1;
    public static final int score_air_nonjet = -1;
    public static final int score_air_solar = +1;
    public static final String USER_INFO = "user_info";


    //-----------------------------------database method name--------------------------------//


    public static void saveModeToDb(Context context, String u_name, String u_date,
                                    String u_traveltime, String u_travelmode, String u_traveltype, String u_travelscore
    ) {


        User user = new User();
        String profileJson = SharedPrefsHelper.getInstance().get(USER_INFO);
        if (profileJson != null) {
            user = new Gson().fromJson(profileJson, User.class);
        }
        CarbonLogModel carbonLogModel = new CarbonLogModel();
        carbonLogModel.setUser_id(user.getId());
        carbonLogModel.setUser_name(user.getUserName());
        carbonLogModel.setTravel_type(u_traveltype);
        carbonLogModel.setTravel_mode(u_travelmode);
        carbonLogModel.setTravel_time(u_traveltime);
        carbonLogModel.setDate(u_date);
        carbonLogModel.setScore(u_travelscore);
        Log.e("check enter log",""+new Gson().toJson(carbonLogModel));
        new UploadCarbonLogToServer(context, new UploadCarbonLogToServer.UploadLogListener() {
            @Override
            public void onUploaded(String response) {
                CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
                SQLiteDatabase db = data.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CreditGaeaDb.C_USERNAME, u_name);
                cv.put(CreditGaeaDb.C_DATETRAVEL, u_date);
                cv.put(CreditGaeaDb.C_TRAVELTIME, u_traveltime);
                cv.put(CreditGaeaDb.C_TRAVELMODE, u_travelmode);
                cv.put(CreditGaeaDb.C_TRAVELMODE_TYPE, u_traveltype);
                cv.put(CreditGaeaDb.C_TRAVELSCORE, Integer.parseInt(u_travelscore));
                db.insertOrThrow(CreditGaeaDb.TABLENAME, null, cv);

                try {
                    if(data!=null){

                        data.close();
                    }
                }catch (Exception e){

                }

                Intent intent = new Intent(context, PastLogsActivity.class);
                ((Activity) context).startActivity(intent);
                ((Activity) context).finish();
            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void getLogList(List<CarbonLogModel> list) {

            }

            @Override
            public void getTransactionList(List<Result> list) {

            }


        }).uploadCarbonLog(carbonLogModel);

    }

    public static void saveAllList(Context context, List<CarbonLogModel> carbonLogList
    ) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getWritableDatabase();

        try {
            db.delete(CreditGaeaDb.TABLENAME, null, null);
        } catch (Exception e) {

        }

        for (int i = 0; i < carbonLogList.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(CreditGaeaDb.C_USERNAME, carbonLogList.get(i).getUser_name());
            cv.put(CreditGaeaDb.C_DATETRAVEL, carbonLogList.get(i).getDate());
            cv.put(CreditGaeaDb.C_TRAVELTIME, carbonLogList.get(i).getTravel_time());
            cv.put(CreditGaeaDb.C_TRAVELMODE, carbonLogList.get(i).getTravel_mode());
            cv.put(CreditGaeaDb.C_TRAVELMODE_TYPE, carbonLogList.get(i).getTravel_type());
            cv.put(CreditGaeaDb.C_TRAVELSCORE, Integer.parseInt(carbonLogList.get(i).getScore()));
            db.insertOrThrow(CreditGaeaDb.TABLENAME, null, cv);

        }

        try {
            if(data!=null){
                data.close();
            }
        }catch (Exception e){

        }

    }

    public static int getTravelLogs(Context context) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from CreditGaeaTab", null);
        int count = cur.getCount();
        return count;
    }

    public static Cursor getTravelallLogs(Context context) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from CreditGaeaTab", null);
        return cur;
    }

    public List<CarbonLogModel> getAllTravelLog(Context context) {
        List<CarbonLogModel> logModels = new ArrayList<>();
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursorData = db.rawQuery("Select * from CreditGaeaTab", null);
        while (cursorData.moveToNext()) {
            CarbonLogModel model = new CarbonLogModel();
            model.setDate(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_DATETRAVEL)));
            model.setScore(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELSCORE)));
            model.setTravel_mode(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELMODE)));
            model.setTravel_time(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELTIME)));
            model.setTravel_type(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELMODE_TYPE)));
            model.setUser_name(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_USERNAME)));
            logModels.add(model);
        }

        try {
            cursorData.close();
            data.close();
        } catch (Exception e) {

        }

        return logModels;


    }

    public static CarbonLogModel getCarbonLogByDate(String date, Context context) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursorData = db.rawQuery("Select * from CreditGaeaTab where " + CreditGaeaDb.C_DATETRAVEL + "='" + date + "'", null);
        CarbonLogModel model = new CarbonLogModel();

      /*  if (cursorData.getCount() > 0) {
            model.setDate(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_DATETRAVEL)));
            model.setScore(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELSCORE)));
            model.setTravel_mode(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELMODE)));
            model.setTravel_time(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELTIME)));
            model.setTravel_type(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELMODE_TYPE)));
            model.setUser_name(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_USERNAME)));
        }*/



        while (cursorData.moveToNext()) {
            model.setDate(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_DATETRAVEL)));
            model.setScore(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELSCORE)));
            model.setTravel_mode(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELMODE)));
            model.setTravel_time(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELTIME)));
            model.setTravel_type(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_TRAVELMODE_TYPE)));
            model.setUser_name(cursorData.getString(cursorData.getColumnIndex(CreditGaeaDb.C_USERNAME)));
        }

        try {
            cursorData.close();
            data.close();
        } catch (Exception e) {

        }

        Log.e("check date log"," : "+new Gson().toJson(model));
        return model;
    }

    public static boolean isLogAvailableForDate(Context context, String date) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from CreditGaeaTab where " + CreditGaeaDb.C_DATETRAVEL + "='" + date + "'", null);
        if (cur.getCount() > 0) {
            try {
                if(cur!=null){
                    cur.close();
                    data.close();
                }
            }catch (Exception e){

            }
            return true;
        } else {
            try {
                if(cur!=null){
                    cur.close();
                    data.close();
                }
            }catch (Exception e){

            }
            return false;
        }

    }

    public static String getNetScore(Context context, String date) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from CreditGaeaTab where " + CreditGaeaDb.C_DATETRAVEL + "='" + date + "'", null);
        String scoreUser = "";
        while (cur.moveToNext()) {
            scoreUser = cur.getString(cur.getColumnIndex(CreditGaeaDb.C_TRAVELSCORE));
        }
        try {
            if(cur!=null){
                cur.close();
                data.close();
            }
        }catch (Exception e){

        }
        return scoreUser;
    }

    public static int getNetScoreTotal(Context context) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cur = db.rawQuery("Select * from CreditGaeaTab ", null);
        int scoreUser =0;
        while (cur.moveToNext()) {
            scoreUser = scoreUser + cur.getInt(cur.getColumnIndex(CreditGaeaDb.C_TRAVELSCORE));
        }

        try {
            if(cur!=null){
                cur.close();
                data.close();
            }
        }catch (Exception e){

        }
        return scoreUser;

    }

    public static String getNetScore(Context context) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cur = db.rawQuery("Select sum(" +
                "" +CreditGaeaDb.C_TRAVELSCORE+
                ") from CreditGaeaTab", null);
        String scoreUser = "";
        while (cur.getCount()>0 && cur.moveToNext()) {
            scoreUser = String.valueOf(cur.getInt(cur.getColumnIndex(CreditGaeaDb.C_TRAVELSCORE)));
        }

        try {
            if(cur!=null){
                cur.close();
                data.close();
            }
        }catch (Exception e){

        }

        return scoreUser;
    }

    public static void updateLogOnTableAtDate(Context context, String date, String score) {
        User user = new User();
        String profileJson = SharedPrefsHelper.getInstance().get(USER_INFO);
        if (profileJson != null) {
            user = new Gson().fromJson(profileJson, User.class);
        }
        CarbonLogModel carbonLogModel = getCarbonLogByDate(date, context);
        carbonLogModel.setUser_id(user.getId());
        carbonLogModel.setScore(score);
        carbonLogModel.setDate(date);
        carbonLogModel.setUser_name(user.getUserName());

        new UploadCarbonLogToServer(context, new UploadCarbonLogToServer.UploadLogListener() {
            @Override
            public void onUploaded(String response) {
                CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
                SQLiteDatabase db = data.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CreditGaeaDb.C_TRAVELSCORE, Integer.parseInt(score));
                db.update(CreditGaeaDb.TABLENAME, cv, "C_DATETRAVEL=?", new String[]{date});
                Intent intent = new Intent(context, PastLogsActivity.class);

                try {
                    if(data!=null){
                        data.close();
                    }
                }catch (Exception e){

                }

                ((Activity) context).startActivity(intent);
                ((Activity) context).finish();
            }
            @Override
            public void onError(String response) {

            }

            @Override
            public void getLogList(List<CarbonLogModel> list) {

            }

            @Override
            public void getTransactionList(List<Result> list) {

            }


        }).updateLog(carbonLogModel);


    }


    // Past Transaction

    public static void savePastTransactions(Context context, List<Result> carbonLogList
    ) {
        CreditGaeaDb data = new CreditGaeaDb(context, CreditGaeaDb.TRANSACTION, null, CreditGaeaDb.dbver);
        SQLiteDatabase db = data.getWritableDatabase();

        try {
            db.delete(CreditGaeaDb.TRANSACTION, null, null);
        } catch (Exception e) {

        }

      int totalScore = 0;
        for (int i = 0; i < carbonLogList.size(); i++) {
            totalScore =  totalScore + Integer.parseInt(carbonLogList.get(i).getScore());
            ContentValues cv = new ContentValues();
            cv.put(CreditGaeaDb.TRANSACTION_User_Id, carbonLogList.get(i).getScoreId());
            cv.put(CreditGaeaDb.TRANSACTION_SENDER_ID, carbonLogList.get(i).getSenderUserId());
            cv.put(CreditGaeaDb.TRANSACTION_SENDER_EMAIL, carbonLogList.get(i).getSenderMail());
            cv.put(CreditGaeaDb.TRANSACTION_RECEIVER_ID, carbonLogList.get(i).getReceiverUserId());
            cv.put(CreditGaeaDb.TRANSACTION_RECEIVER_EMAIL, carbonLogList.get(i).getReceiverEmail());
            cv.put(CreditGaeaDb.TRANSACTION_SCORE, Integer.parseInt(carbonLogList.get(i).getScore()));
            db.insertOrThrow(CreditGaeaDb.TRANSACTION, null, cv);
        }

        MainActivity.transactinScore = totalScore;

        try {
            db.close();
        }catch (Exception e){
             e.printStackTrace();
        }


    }

    public static int getPastTransactionTotalScore(Context context) {
        CreditGaeaDb data = null;
        Cursor cur = null;
        int scoreUser =0;
        try {
             data = new CreditGaeaDb(context, CreditGaeaDb.DBNAME, null, CreditGaeaDb.dbver);
            SQLiteDatabase db = data.getReadableDatabase();
             cur = db.rawQuery("Select * from " +
                    "" +CreditGaeaDb.TRANSACTION+
                    "", null);

            while (cur.moveToNext()) {
                Log.e("check transaction ",""+new Gson().toJson(cur.getInt(cur.getColumnIndex(CreditGaeaDb.TRANSACTION_SCORE))));
                scoreUser = scoreUser + cur.getInt(cur.getColumnIndex(CreditGaeaDb.TRANSACTION_SCORE));
            }
        }catch (Exception e){

        }

        try {
            if(cur!=null){
                cur.close();
            }
           if(data!=null){
               data.close();
           }
        }catch (Exception e){

        }

        return scoreUser;
    }
}
