package com.creditgaea.sample.credit.java.creditgea.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quickblox.sample.videochat.java.R;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;

import java.util.Calendar;

/**
 * Created by user on 11/22/2016.
 */

public class AirModeSelectedActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_time_jet,tv_time_nonjet,tv_time_solar;
    private Button cb_time_jet,cb_time_nonjet,cb_time_solar;
    private Context context;
    private String getUsername="",getDateEntered="";
    private int netscore=0;
    private Button btnSaveScoreLog;
    private boolean flagsaved;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=AirModeSelectedActivity.this;
        setContentView(R.layout.air_mode_screen);
        init();
    }

    private void init()
    {
        btnSaveScoreLog=(Button)findViewById(R.id.btnSave) ;
        tv_time_jet=(TextView)findViewById(R.id.time_air_jet);
        tv_time_nonjet=(TextView)findViewById(R.id.time_air_nonjet);
        tv_time_solar=(TextView)findViewById(R.id.time_air_solar);

        btnSaveScoreLog.setOnClickListener(this);
        tv_time_jet.setOnClickListener(this);
        tv_time_nonjet.setOnClickListener(this);
        tv_time_solar.setOnClickListener(this);

        cb_time_jet=(Button) findViewById(R.id.checkbox_air_jet);
        cb_time_nonjet=(Button)findViewById(R.id.checkbox_air_nonjet);
        cb_time_solar=(Button)findViewById(R.id.checkbox_air_solar);

        cb_time_jet.setOnClickListener(this);
        cb_time_nonjet.setOnClickListener(this);
        cb_time_solar.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsername=getIntent().getStringExtra(AppConstants.USERNAME);
        getDateEntered=getIntent().getStringExtra(AppConstants.TRAVELDATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.checkbox_air_jet:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time_jet.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Travel Time");
                mTimePicker.show();
                break;

            case R.id.checkbox_air_nonjet:
                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker1;
                mTimePicker1 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time_nonjet.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour1, minute1, false);//Yes 24 hour time
                mTimePicker1.setTitle("Select Travel Time");
                mTimePicker1.show();
                break;

            case R.id.checkbox_air_solar:
                Calendar mcurrentTime2 = Calendar.getInstance();
                int hour2 = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                int minute2 = mcurrentTime2.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time_solar.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour2, minute2, false);//Yes 24 hour time
                mTimePicker2.setTitle("Select Travel Time");
                mTimePicker2.show();
                break;

            case R.id.btnSave:
             /*   if(cb_time_jet.isChecked() || cb_time_nonjet.isChecked() ||
                        cb_time_solar.isChecked())
                {
                    if (cb_time_jet.isChecked()) {
                        if (!tv_time_jet.getText().toString().equals("Total Time- 00:00:00")) {
                            netscore = AppConstants.score_air_jet;
                        } else {
                            Toast.makeText(context, "Set time for jet", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if (cb_time_nonjet.isChecked()) {
                        if (!tv_time_nonjet.getText().toString().equals("Total Time- 00:00:00")) {
                            netscore = netscore + AppConstants.score_air_nonjet;
                        } else {
                            Toast.makeText(context, "Set time for non jet", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if (cb_time_solar.isChecked()) {
                        if (!tv_time_solar.getText().toString().equals("Total Time- 00:00:00")) {
                            netscore = netscore + AppConstants.score_air_solar;
                        } else {
                            Toast.makeText(context, "Set time for solar", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    else {
                            if (AppConstants.getTravelLogs(context) == 0) {
                                AppConstants.saveModeToDb(context, getUsername, getDateEntered, "", "Air", "", "" + netscore);
                            } else {
                                if (!AppConstants.isLogAvailableForDate(context, getDateEntered)) {
                                    AppConstants.saveModeToDb(context, getUsername, getDateEntered, "", "Air", "", "" + netscore);
                                } else {
                                    Toast.makeText(context, "You have already saved a log for today", Toast.LENGTH_SHORT).show();
                                }
                            }
                    }
                }
                else
                {
                    Toast.makeText(context,"please enable a travel log",Toast.LENGTH_SHORT).show();
                }*/
                if (tv_time_jet.getText().toString().equals("Total Time- 00:00:00") && tv_time_nonjet.getText().toString().equals("Total Time- 00:00:00")
                        && tv_time_solar.getText().toString().equals("Total Time- 00:00:00")) {
                    Toast.makeText(context,"please set a travel log", Toast.LENGTH_SHORT).show();

                } else {
                    if (!tv_time_jet.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_air_jet;
                    }
                    if (!tv_time_nonjet.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_air_nonjet;
                    }
                    if (!tv_time_solar.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_air_solar;
                    }

                    if(AppConstants.getTravelLogs(context)==0)
                    {
                        AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","Air","",""+netscore);
                       /* intent=new Intent(context,PastLogsActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                    else
                    {
                        if(AppConstants.isLogAvailableForDate(context,getDateEntered))
                        {
                            int getScoreOfday= Integer.parseInt(AppConstants.getNetScore(context,getDateEntered));
                            AppConstants.updateLogOnTableAtDate(context,getDateEntered,""+(netscore+getScoreOfday));
                           /* intent=new Intent(context,PastLogsActivity.class);
                            startActivity(intent);
                            finish();*/
                        }
                        else
                        {
                            AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","Air","",""+netscore);
                           /* intent=new Intent(context,PastLogsActivity.class);
                            startActivity(intent);
                            finish();*/
                        }
                    }

                }
                break;
        }

    }

   /* @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId())
        {
            case R.id.checkbox_air_jet:
                if(b)
                {
                    cb_time_jet.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_jet.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;

            case R.id.checkbox_air_nonjet:
                if(b)
                {
                    cb_time_nonjet.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_nonjet.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;

            case R.id.checkbox_air_solar:
                if(b)
                {
                    cb_time_solar.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_solar.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;

        }

    }*/
}
