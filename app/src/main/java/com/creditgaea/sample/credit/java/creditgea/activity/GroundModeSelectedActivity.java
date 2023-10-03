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

public class GroundModeSelectedActivity extends AppCompatActivity implements View.OnClickListener {
      private TextView tv_time_gasoline,tv_time_diesel,tv_time_hybrid,tv_time_electric,tv_time_bicycle;
      private Button cb_time_gasoline,cb_time_diesel,cb_time_hybrid,cb_time_electric,cb_time_bicycle;
      private Context context;
      private Button btnSaveScoreLog;
    private String getUsername="",getDateEntered="";
    private String time1="",time2="",time3="",time4="",time5="";
    private int netscore=0;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=GroundModeSelectedActivity.this;
        setContentView(R.layout.ground_mode_screen);
        init();
    }

    private void init()
    {   btnSaveScoreLog=(Button)findViewById(R.id.btnSave) ;
        tv_time_gasoline=(TextView)findViewById(R.id.time_ground_gasoline);
        tv_time_diesel=(TextView)findViewById(R.id.time_ground_diesel);
        tv_time_hybrid=(TextView)findViewById(R.id.time_ground_hybrid);
        tv_time_electric=(TextView)findViewById(R.id.time_ground_electric);
        tv_time_bicycle=(TextView)findViewById(R.id.time_ground_bicycle);

        btnSaveScoreLog.setOnClickListener(this);
        tv_time_gasoline.setOnClickListener(this);
        tv_time_diesel.setOnClickListener(this);
        tv_time_hybrid.setOnClickListener(this);
        tv_time_electric.setOnClickListener(this);
        tv_time_bicycle.setOnClickListener(this);

        cb_time_gasoline=(Button) findViewById(R.id.checkbox_ground_gasoline);
        cb_time_diesel=(Button)findViewById(R.id.checkbox_ground_diesel);
        cb_time_hybrid=(Button)findViewById(R.id.checkbox_ground_hybrid);
        cb_time_electric=(Button)findViewById(R.id.checkbox_ground_electric);
        cb_time_bicycle=(Button)findViewById(R.id.checkbox_ground_bicycle);

        cb_time_gasoline.setOnClickListener(this);
        cb_time_diesel.setOnClickListener(this);
        cb_time_hybrid.setOnClickListener(this);
        cb_time_electric.setOnClickListener(this);
        cb_time_bicycle.setOnClickListener(this);
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
            case R.id.checkbox_ground_gasoline:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time1=selectedHour + ":" + selectedMinute;
                        tv_time_gasoline.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Travel Time");
                mTimePicker.show();
                break;

            case R.id.checkbox_ground_diesel:
                Calendar mcurrentTime1 = Calendar.getInstance();
                int hour1 = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker1;
                mTimePicker1 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time2=selectedHour + ":" + selectedMinute;
                        tv_time_diesel.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour1, minute1, false);//Yes 24 hour time
                mTimePicker1.setTitle("Select Travel Time");
                mTimePicker1.show();
                break;

            case R.id.checkbox_ground_hybrid:
                Calendar mcurrentTime2 = Calendar.getInstance();
                int hour2 = mcurrentTime2.get(Calendar.HOUR_OF_DAY);
                int minute2 = mcurrentTime2.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker2;
                mTimePicker2 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time3=selectedHour + ":" + selectedMinute;
                        tv_time_hybrid.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour2, minute2, false);//Yes 24 hour time
                mTimePicker2.setTitle("Select Travel Time");
                mTimePicker2.show();
                break;

            case R.id.checkbox_ground_electric:
                Calendar mcurrentTime3 = Calendar.getInstance();
                int hour3 = mcurrentTime3.get(Calendar.HOUR_OF_DAY);
                int minute3 = mcurrentTime3.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker3;
                mTimePicker3 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time4=selectedHour + ":" + selectedMinute;
                        tv_time_electric.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour3, minute3, false);//Yes 24 hour time
                mTimePicker3.setTitle("Select Travel Time");
                mTimePicker3.show();
                break;

            case R.id.checkbox_ground_bicycle:
                Calendar mcurrentTime4 = Calendar.getInstance();
                int hour4 = mcurrentTime4.get(Calendar.HOUR_OF_DAY);
                int minute4 = mcurrentTime4.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker4;
                mTimePicker4 = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time5=selectedHour + ":" + selectedMinute;
                        tv_time_bicycle.setText("Total Time- "+selectedHour + ":" + selectedMinute);
                    }
                }, hour4, minute4, false);//Yes 24 hour time
                mTimePicker4.setTitle("Select Travel Time");
                mTimePicker4.show();
                break;
            case R.id.btnSave:


                if (tv_time_gasoline.getText().toString().equals("Total Time- 00:00:00") && tv_time_diesel.getText().toString().equals("Total Time- 00:00:00")
                        && tv_time_hybrid.getText().toString().equals("Total Time- 00:00:00") && tv_time_electric.getText().toString().equals("Total Time- 00:00:00")
                        && tv_time_bicycle.getText().toString().equals("Total Time- 00:00:00") ) {
                    Toast.makeText(context,"please set a travel log", Toast.LENGTH_SHORT).show();

                } else {
                    if (!tv_time_gasoline.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_ground_gasoline;
                    }
                    if (!tv_time_diesel.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_ground_diesel;
                    }
                    if (!tv_time_hybrid.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_ground_hybrid;
                    }
                    if (!tv_time_electric.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_ground_electric;
                    }
                    if (!tv_time_bicycle.getText().toString().equals("Total Time- 00:00:00")) {
                        netscore = AppConstants.score_ground_bicycle;
                    }

                    if(AppConstants.getTravelLogs(context)==0)
                    {
                        AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","Ground","",""+netscore);

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
                            AppConstants.saveModeToDb(context,getUsername,getDateEntered,"","","",""+netscore);

                        }
                    }

                }
                break;
        }

    }

    /*@Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId())
        {
            case R.id.checkbox_ground_gasoline:
                  if(b)
                  {
                      cb_time_gasoline.setButtonDrawable(R.drawable.form_check_box);
                  }
                  else
                  {
                      cb_time_gasoline.setButtonDrawable(R.drawable.form_un_check_box);
                  }
                break;

            case R.id.checkbox_ground_diesel:
                if(b)
                {
                    cb_time_diesel.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_diesel.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;

            case R.id.checkbox_ground_hybrid:
                if(b)
                {
                    cb_time_hybrid.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_hybrid.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;
            case R.id.checkbox_ground_electric:
                if(b)
                {
                    cb_time_electric.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_electric.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;
            case R.id.checkbox_ground_bicycle:
                if(b)
                {
                    cb_time_bicycle.setButtonDrawable(R.drawable.form_check_box);
                }
                else
                {
                    cb_time_bicycle.setButtonDrawable(R.drawable.form_un_check_box);
                }
                break;
        }

    }*/
}
