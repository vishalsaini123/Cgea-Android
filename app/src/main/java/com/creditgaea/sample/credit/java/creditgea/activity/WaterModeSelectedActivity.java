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

public class WaterModeSelectedActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_time_gasoline,tv_time_diesel,tv_time_wind,tv_time_solar;
    private Button cb_time_gasoline,cb_time_diesel,cb_time_wind,cb_time_solar;
    private Context context;
    private String getUsername="",getDateEntered="";
    private Button btnSaveScoreLog;
    private int netscore=0;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=WaterModeSelectedActivity.this;
        setContentView(R.layout.water_mode_screen);
        init();
    }

    private void init()
    {
        btnSaveScoreLog=(Button)findViewById(R.id.btnSave) ;
        tv_time_gasoline=(TextView)findViewById(R.id.time_water_gasoline);
        tv_time_diesel=(TextView)findViewById(R.id.time_water_diesel);
        tv_time_wind=(TextView)findViewById(R.id.time_water_wind);
        tv_time_solar=(TextView)findViewById(R.id.time_water_solar);

        btnSaveScoreLog.setOnClickListener(this);
        tv_time_gasoline.setOnClickListener(this);
        tv_time_diesel.setOnClickListener(this);
        tv_time_wind.setOnClickListener(this);
        tv_time_solar.setOnClickListener(this);

        cb_time_gasoline=(Button) findViewById(R.id.checkbox_water_gasoline);
        cb_time_diesel=(Button)findViewById(R.id.checkbox_water_diesel);
        cb_time_wind=(Button)findViewById(R.id.checkbox_water_wind);
        cb_time_solar=(Button)findViewById(R.id.checkbox_water_solar);

        cb_time_gasoline.setOnClickListener(this);
        cb_time_diesel.setOnClickListener(this);
        cb_time_wind.setOnClickListener(this);
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
            case R.id.checkbox_water_gasoline:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time_gasoline.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Travel Time");
                mTimePicker.show();
                break;

            case R.id.checkbox_water_diesel:
                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker1;
                mTimePicker1 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time_diesel.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour1, minute1, false);//Yes 24 hour time
                mTimePicker1.setTitle("Select Travel Time");
                mTimePicker1.show();
                break;

            case R.id.checkbox_water_wind:
                Calendar mcurrentTime2 = Calendar.getInstance();
                int hour2 = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                int minute2 = mcurrentTime2.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time_wind.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour2, minute2, false);//Yes 24 hour time
                mTimePicker2.setTitle("Select Travel Time");
                mTimePicker2.show();
                break;

            case R.id.checkbox_water_solar:
                Calendar mcurrentTime3 = Calendar.getInstance();
                int hour3 = mcurrentTime3.get(Calendar.HOUR_OF_DAY);
                int minute3 = mcurrentTime3.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker3;
                mTimePicker3 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tv_time_solar.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour3, minute3, false);//Yes 24 hour time
                mTimePicker3.setTitle("Select Travel Time");
                mTimePicker3.show();
                break;

            case R.id.btnSave:
                /*if(cb_time_gasoline.isChecked() || cb_time_diesel.isChecked() ||
                        cb_time_wind.isChecked() || cb_time_solar.isChecked() )
                {
                    if (cb_time_gasoline.isChecked()) {
                        if (!tv_time_gasoline.getText().toString().equals("Total Time- 00:00:00")) {
                            netscore = AppConstants.score_water_gasoline;
                        } else {
                            Toast.makeText(context, "Set time for gasoline", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    } if (cb_time_diesel.isChecked()) {
                    if (!tv_time_diesel.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = netscore + AppConstants.score_water_diesel;
                    } else {
                        Toast.makeText(context, "Set time for diesel", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } if(cb_time_wind.isChecked()) {
                    if (!tv_time_wind.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = netscore + AppConstants.score_water_wind;
                    } else {
                        Toast.makeText(context, "Set time for wind", Toast.LENGTH_SHORT).show();
                        break;
                    }
                } if (cb_time_solar.isChecked()) {
                    if (!tv_time_solar.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = netscore + AppConstants.score_water_solar;
                    } else {
                        Toast.makeText(context, "Set time for solar", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                else
                {
                    if(AppConstants.getTravelLogs(context)==0)
                    {
                        AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","Water","",""+netscore);
                    }
                    else
                    {
                        if(!AppConstants.isLogAvailableForDate(context,getDateEntered))
                        {
                            AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","Water","",""+netscore);
                        }
                        else
                        {
                            Toast.makeText(context,"You have already saved a log for today",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                }
                else
                {
                    Toast.makeText(context,"please enable a travel log",Toast.LENGTH_SHORT).show();
                }*/
                //AppConstants.saveModeToDb(context,getUsername,getDateEntered,);
                if (tv_time_gasoline.getText().toString().equals("Total Time- 00:00:00") && tv_time_diesel.getText().toString().equals("Total Time- 00:00:00")
                        && tv_time_wind.getText().toString().equals("Total Time- 00:00:00") && tv_time_solar.getText().toString().equals("Total Time- 00:00:00")
                         ) {
                    Toast.makeText(context,"please set a travel log", Toast.LENGTH_SHORT).show();

                } else {
                    if (!tv_time_gasoline.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_water_gasoline;
                    }
                    if (!tv_time_diesel.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_water_diesel;
                    }
                    if (!tv_time_wind.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_water_wind;
                    }
                    if (!tv_time_solar.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_water_solar;
                    }

                    if(AppConstants.getTravelLogs(context)==0)
                    {
                        AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","Water","",""+netscore);
                    }
                    else
                    {
                        if(AppConstants.isLogAvailableForDate(context,getDateEntered))
                        {
                            int getScoreOfday= Integer.parseInt(AppConstants.getNetScore(context,getDateEntered));
                            AppConstants.updateLogOnTableAtDate(context,getDateEntered,""+(netscore+getScoreOfday));

                        }
                        else
                        {
                            AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","Water","",""+netscore);

                        }
                    }

                }
                break;
        }

    }

 /*   @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId())
        {
            case R.id.checkbox_water_gasoline:
                if(b)
                {
                    cb_time_gasoline.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_gasoline.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;

            case R.id.checkbox_water_diesel:
                if(b)
                {
                    cb_time_diesel.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_diesel.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;

            case R.id.checkbox_water_wind:
                if(b)
                {
                    cb_time_wind.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_wind.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;
            case R.id.checkbox_water_solar:
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
