package com.creditgaea.sample.credit.java.creditgea.activity;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.MainThread;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.creditgaea.sample.credit.java.chat.ui.activity.DialogsActivity;
import com.creditgaea.sample.credit.java.chat.utils.chat.ChatHelper;
import com.creditgaea.sample.credit.java.creditgea.model.AddWalletResponse;
import com.creditgaea.sample.credit.java.creditgea.model.Invoice;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.creditgea.utils.SessionManager;
import com.creditgaea.sample.credit.java.webservice.UploadCarbonLogToServer;
import com.creditgaea.sample.credit.java.webservice.WalletServiceApi;
import com.google.gson.Gson;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.creditgaea.sample.credit.java.App;
import com.quickblox.sample.videochat.java.R;
import com.creditgaea.sample.credit.java.activities.OpponentsActivity;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.services.LoginService;
import com.creditgaea.sample.credit.java.util.QBResRequestExecutor;
import com.creditgaea.sample.credit.java.utils.Consts;
import com.creditgaea.sample.credit.java.utils.PermissionsChecker;
import com.creditgaea.sample.credit.java.utils.QBEntityCallbackImpl;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.utils.ToastUtils;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WalletServiceApi.WalletListener {
    private static final int UNAUTHORIZED = 401;
    private TextView editDate;
    private TextView tv_username;
  //  private View v_name;
    private EditText et_username;
    private Button btnGround, btnAir, btnWater, btnHome, btnAllLogs,btnWallet;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Context context;
    SimpleDateFormat df;
    private SessionManager sm;



    //video call related variable
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int SPLASH_DELAY = 1500;

    private static final String OVERLAY_PERMISSION_CHECKED_KEY = "overlay_checked";
    private static final String MI_OVERLAY_PERMISSION_CHECKED_KEY = "mi_overlay_checked";

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1764;

    private SharedPrefsHelper sharedPrefsHelper;
    private QBUser userForSave;
    protected QBResRequestExecutor requestExecutor;
    private ProgressDialog progressDialog;

    private Button btn_video;
    private Button btn_chat;
    public static int transactinScore = 0;

    CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        setContentView(R.layout.login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        init();
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderpicker();
            }
        });

       // customProgressDialog = new CustomProgressDialog(MainActivity.this);

        new UploadCarbonLogToServer(context, new UploadCarbonLogToServer.UploadLogListener() {
            @Override
            public void onUploaded(String response) {

            }

            @Override
            public void onError(String response) {
               // customProgressDialog.hide();
            }

            @Override
            public void getLogList(List<CarbonLogModel> list) {
                AppConstants.saveAllList(MainActivity.this,list);
               // customProgressDialog.dismiss();
               // getTransactionCreditList();
            }
            @Override
            public void getTransactionList(List<Result> list) {

            }
        }).getAllLogList();

        /*editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        editTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Travel Time");
                mTimePicker.show();
            }
        }); */

        if (checkOverlayPermissions()) {
            runNextScreen();
        }

    }

    private void getTotal() {
        new WalletServiceApi(MainActivity.this,MainActivity.this).getTotalWallet();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int score = AppConstants.getPastTransactionTotalScore(MainActivity.this);
        Log.e("check score ",""+score);

        if (sm.getUserName().equals("")) {
            tv_username.setVisibility(View.VISIBLE);
            et_username.setVisibility(View.VISIBLE);
           // v_name.setVisibility(View.VISIBLE);
        } else {
            if (sharedPrefsHelper.hasQbUser()) {
                LoginService.start(MainActivity.this, sharedPrefsHelper.getQbUser());
            }
            et_username.setVisibility(View.GONE);
           // v_name.setVisibility(View.INVISIBLE);
            tv_username.setVisibility(View.VISIBLE);
            tv_username.setGravity(Gravity.CENTER_HORIZONTAL);
            tv_username.setText("Welcom "+sm.getUserName());
        }
    }

    public void init() {

        requestExecutor = App.getInstance().getQbResRequestExecutor();
        sharedPrefsHelper = SharedPrefsHelper.getInstance();

        sm = new SessionManager(context);
        editDate = (TextView) findViewById(R.id.editlabel2);

        tv_username = (TextView) findViewById(R.id.text_label);
        btn_video = (Button) findViewById(R.id.btn_video);
        btnWallet = (Button) findViewById(R.id.btnWallet);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        et_username = (EditText) findViewById(R.id.editlabel_name);
        //v_name = (View) findViewById(R.id.viewlabel1);

        btnGround = (Button) findViewById(R.id.btnmode1);
        btnAir = (Button) findViewById(R.id.btnmode2);
        btnWater = (Button) findViewById(R.id.btnmode3);
        btnHome = (Button) findViewById(R.id.btnmode4);
        btnAllLogs = (Button) findViewById(R.id.btnmodechecklast);

        btnGround.setOnClickListener(this);
        btnAir.setOnClickListener(this);
        btnWater.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnAllLogs.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        btnWallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        String str_name = et_username.getText().toString().trim();
        String str_date = editDate.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_video:
                if (sharedPrefsHelper.hasQbUser()) {
                    LoginService.start(MainActivity.this, sharedPrefsHelper.getQbUser());
                    OpponentsActivity.start(MainActivity.this);
                }
               /* else if (tv_username.getText().toString().isEmpty()&&et_username.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter user name!", Toast.LENGTH_SHORT).show();
                } else if (!tv_username.getText().toString().isEmpty()) {
                    sm.saveUserName(str_name);
                    userForSave = createUserWithEnteredData();
                    startSignUpNewUser(userForSave,"video");
                } else if (str_name.length() > 0) {
                    sm.saveUserName(str_name);
                    userForSave = createUserWithEnteredData();
                    startSignUpNewUser(userForSave,"video");
                } else {
                    Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
                }*/
                break;

            case R.id.btnWallet:
                startActivity(new Intent(MainActivity.this,WalletActivity.class));
                break;

            case R.id.btn_chat:

                if (sharedPrefsHelper.hasQbUser()) {
                   restoreChatSession();
                }
               /* else if (tv_username.getText().toString().isEmpty()&&et_username.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter user name!", Toast.LENGTH_SHORT).show();
                } else if (!tv_username.getText().toString().isEmpty()) {
                      sm.saveUserName(str_name);
                 *//*   userForSave = createUserWithEnteredData();
                    startSignUpNewUser(userForSave,"chat");*//*
                    restoreChatSession();
                }
                else if (str_name.length() > 0) {
                    sm.saveUserName(str_name);
                    userForSave = createUserWithEnteredData();
                    prepareUser(str_name);
                  //  startSignUpNewUser(userForSave,"chat");
                } else {
                    Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
                }*/
                break;

            case R.id.btnmode1:
                if (sm.getUserName().equals("")) {
                    if (str_name.length() > 0) {
                        if (str_date.length() > 0) {
                            sm.saveUserName(str_name);
                            intent = new Intent(context, GroundModeSelectedActivity.class);
                            intent.putExtra(AppConstants.USERNAME, str_name);
                            intent.putExtra(AppConstants.TRAVELDATE, str_date);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (str_date.length() > 0) {
                        intent = new Intent(context, GroundModeSelectedActivity.class);
                        intent.putExtra(AppConstants.USERNAME, sm.getUserName());
                        intent.putExtra(AppConstants.TRAVELDATE, str_date);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                    }
                }


                break;

            case R.id.btnmode2:
                if (sm.getUserName().equals("")) {
                    if (str_name.length() > 0) {
                        if (str_date.length() > 0) {
                            sm.saveUserName(str_name);
                            intent = new Intent(context, AirModeSelectedActivity.class);
                            intent.putExtra(AppConstants.USERNAME, str_name);
                            intent.putExtra(AppConstants.TRAVELDATE, str_date);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (str_date.length() > 0) {
                        intent = new Intent(context, AirModeSelectedActivity.class);
                        intent.putExtra(AppConstants.USERNAME, sm.getUserName());
                        intent.putExtra(AppConstants.TRAVELDATE, str_date);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.btnmode3:
                if (sm.getUserName().equals("")) {
                    if (str_name.length() > 0) {
                        if (str_date.length() > 0) {
                            sm.saveUserName(str_name);
                            intent = new Intent(context, WaterModeSelectedActivity.class);
                            intent.putExtra(AppConstants.USERNAME, str_name);
                            intent.putExtra(AppConstants.TRAVELDATE, str_date);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (str_date.length() > 0) {
                        intent = new Intent(context, WaterModeSelectedActivity.class);
                        intent.putExtra(AppConstants.USERNAME, sm.getUserName());
                        intent.putExtra(AppConstants.TRAVELDATE, str_date);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.btnmode4:
                if (sm.getUserName().equals("")) {
                    if (str_name.length() > 0) {
                        if (str_date.length() > 0) {
                            sm.saveUserName(str_name);
                            intent = new Intent(context, HomeModeSelectedActivity.class);
                            intent.putExtra(AppConstants.USERNAME, str_name);
                            intent.putExtra(AppConstants.TRAVELDATE, str_date);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (str_date.length() > 0) {
                        intent = new Intent(context, HomeModeSelectedActivity.class);
                        intent.putExtra(AppConstants.USERNAME, sm.getUserName());
                        intent.putExtra(AppConstants.TRAVELDATE, str_date);
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "Enter date", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.btnmodechecklast:
                intent = new Intent(context, PastLogsActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void calenderpicker() {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        df = new SimpleDateFormat("yyyy-MM-dd");

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                        //txtDate.setText(dayOfMonth + "-"
                        //	+ (monthOfYear + 1) + "-" + year);
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);

                        editDate.setText(df.format(newDate.getTime()));

                    }
                }, mYear, mMonth, mDay);
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dpd.show();
    }

    //video call retlated calss


    private void runNextScreen() {
        if (sharedPrefsHelper.hasQbUser()) {
            LoginService.start(MainActivity.this, sharedPrefsHelper.getQbUser());
        }
    }


    private boolean checkOverlayPermissions() {
        Log.e(TAG, "Checking Permissions");
        boolean overlayChecked = sharedPrefsHelper.get(OVERLAY_PERMISSION_CHECKED_KEY, false);
        boolean miOverlayChecked = sharedPrefsHelper.get(MI_OVERLAY_PERMISSION_CHECKED_KEY, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this) && !overlayChecked) {
                Log.e(TAG, "Android Overlay Permission NOT Granted");
                buildOverlayPermissionAlertDialog();
                return false;
            } else if (PermissionsChecker.isMiUi() && !miOverlayChecked) {
                Log.e(TAG, "Xiaomi Device. Need additional Overlay Permissions");
                buildMIUIOverlayPermissionAlertDialog();
                return false;
            }
        }
        Log.e(TAG, "All Overlay Permission Granted");
        sharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true);
        sharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true);
        return true;
    }

    private void buildOverlayPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Overlay Permission Required");
        builder.setIcon(R.drawable.ic_error_outline_gray_24dp);
        builder.setMessage("To receive calls in background - \nPlease Allow overlay permission in Android Settings");
        builder.setCancelable(false);

        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtils.longToast("You might miss calls while your application in background");
                sharedPrefsHelper.save(OVERLAY_PERMISSION_CHECKED_KEY, true);
            }
        });

        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAndroidOverlayPermissionsSettings();
            }
        });

        AlertDialog alertDialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.create();
            alertDialog.show();
        }
    }

    private void showAndroidOverlayPermissionsSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Application Already has Overlay Permission");
        }
    }

    private void buildMIUIOverlayPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Additional Overlay Permission Required");
        builder.setIcon(R.drawable.ic_error_outline_orange_24dp);
        builder.setMessage("Please make sure that all additional permissions granted");
        builder.setCancelable(false);

        builder.setNeutralButton("I'm sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPrefsHelper.save(MI_OVERLAY_PERMISSION_CHECKED_KEY, true);
                runNextScreen();
            }
        });

        builder.setPositiveButton("Mi Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMiUiPermissionsSettings();
            }
        });

        AlertDialog alertDialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.create();
            alertDialog.show();
        }
    }

    private void showMiUiPermissionsSettings() {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", getPackageName());
        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }

    // login QBLOCK USER
    private void saveUserData(QBUser qbUser) {
        SharedPrefsHelper sharedPrefsHelper = SharedPrefsHelper.getInstance();
        sharedPrefsHelper.saveQbUser(qbUser);
    }

    private QBUser createUserWithEnteredData() {
        return createQBUserWithCurrentData(String.valueOf(et_username.getText()),
                String.valueOf(et_username.getText()));
    }

    private QBUser createQBUserWithCurrentData(String userLogin, String userFullName) {
        QBUser qbUser = null;
        if (!TextUtils.isEmpty(userLogin) && !TextUtils.isEmpty(userFullName)) {
            qbUser = new QBUser();
            qbUser.setLogin(userLogin);
            qbUser.setFullName(userFullName);
            //qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
        }
        return qbUser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Consts.EXTRA_LOGIN_RESULT_CODE) {
            //  hideProgressDialog();
            boolean isLoginSuccess = data.getBooleanExtra(Consts.EXTRA_LOGIN_RESULT, false);
            String errorMessage = data.getStringExtra(Consts.EXTRA_LOGIN_ERROR_MESSAGE);

            if (isLoginSuccess) {
                saveUserData(userForSave);
                signInCreatedUser(userForSave, "chat");
            } else {
                ToastUtils.longToast(getString(R.string.login_chat_login_error) + errorMessage);
                et_username.setText(userForSave.getLogin());
                et_username.setText(userForSave.getFullName());
            }
        } else if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (checkOverlayPermissions()) {
                runNextScreen();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void signInCreatedUser(final QBUser qbUser, String type) {
        Log.d(TAG, "SignIn Started");
        requestExecutor.signInUser(qbUser, new QBEntityCallbackImpl<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {
                Log.d(TAG, "SignIn Successful");
                sharedPrefsHelper.saveQbUser(userForSave);
                updateUserOnServer(qbUser,type);
            }

            @Override
            public void onError(QBResponseException responseException) {
                Log.d(TAG, "Error SignIn" + responseException.getMessage());
                hideProgressDialog();
                ToastUtils.longToast(R.string.sign_in_error);
            }
        });
    }

    private void updateUserOnServer(QBUser user, String type) {
        user.setPassword(null);
        QBUsers.updateUser(user).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                hideProgressDialog();
                if(type.equalsIgnoreCase("chat")){
                    DialogsActivity.start(MainActivity.this);
                }else {
                    OpponentsActivity.start(MainActivity.this);

                }
                //  finish();
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
                ToastUtils.longToast(R.string.update_user_error);
            }
        });
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, LoginService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        LoginService.start(this, qbUser, pendingIntent);
    }

    void showProgressDialog(@StringRes int messageId) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }
        progressDialog.setMessage(getString(messageId));
        progressDialog.show();
    }

    void hideProgressDialog() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (IllegalArgumentException ignored) {

            }
        }
    }

    private void startSignUpNewUser(final QBUser newUser, String type) {
        Log.d(TAG, "SignUp New User");
        showProgressDialog(R.string.please_wait);
        requestExecutor.signUpNewUser(newUser, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser result, Bundle params) {
                        Log.d(TAG, "SignUp Successful");
                        saveUserData(newUser);

                        if(type.equalsIgnoreCase("chat")){
                            DialogsActivity.start(MainActivity.this);
                        }else {
                            loginToChat(result);
                        }

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.d(TAG, "Error SignUp" + e.getMessage());
                        if (e.getHttpStatusCode() == Consts.ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS) {
                            signInCreatedUser(newUser,type);
                        } else {
                            hideProgressDialog();
                            ToastUtils.longToast(R.string.sign_up_error);
                        }
                    }
                }
        );
    }

    private void loginToChat(final QBUser qbUser) {
      //  qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
        userForSave = qbUser;
        startLoginService(qbUser);
    }

    // chat
    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged()) {
            DialogsActivity.start(this);
            finish();
        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser != null) {
                loginToChat(currentUser);
            }
        }
    }

    private QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null || user == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        Integer userId = qbSessionManager.getSessionParameters().getUserId();
        user.setId(userId);
        return user;
    }

    private void prepareUser(String name) {
        QBUser qbUser = new QBUser();
        qbUser.setLogin(name);
        qbUser.setFullName(name);
     //   qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
        signIn(qbUser);
    }

    private void signIn(final QBUser user) {
        showProgressDialog(R.string.dlg_login);
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                if (userFromRest.getFullName().equals(user.getFullName())) {
                    loginToChat(user);
                    saveUserData(userFromRest);
                } else {
                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                if (e.getHttpStatusCode() == UNAUTHORIZED) {
                    signUp(user);
                } else {
                    hideProgressDialog();
                    signIn(user);
                }
            }
        });
    }

    private void updateUser(final QBUser user) {
        ChatHelper.getInstance().updateUser(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                saveUserData(user);
                loginToChat(user);
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
               // showEr(R.string.login_chat_login_error, e, null);
            }
        });
    }

    private void signUp(final QBUser newUser) {
        SharedPrefsHelper.getInstance().removeQbUser();
        QBUsers.signUp(newUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                hideProgressDialog();
                signIn(newUser);
            }

            @Override
            public void onError(QBResponseException e) {
                hideProgressDialog();
               // showErrorSnackbar(R.string.login_sign_up_error, e, null);
            }
        });
    }

    public void getTransactionCreditList() {
        customProgressDialog.show();
        new UploadCarbonLogToServer(context, new UploadCarbonLogToServer.UploadLogListener() {
            @Override
            public void onUploaded(String response) {
                customProgressDialog.dismiss();
                //rice holly wood
            }

            @Override
            public void onError(String response) {
                customProgressDialog.dismiss();
                new android.app.AlertDialog.Builder(MainActivity.this).setMessage("" + response)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }

            @Override
            public void getLogList(List<CarbonLogModel> list) {

            }

            @Override
            public void getTransactionList(List<Result> list) {
                customProgressDialog.dismiss();
                Log.e("check list result ",""+new Gson().toJson(list));
                AppConstants.savePastTransactions(MainActivity.this, list);
            }
        }).getTransferCreditList();
    }

    @Override
    public void onAddWallet(AddWalletResponse response) {

    }

    @Override
    public void onError(String response) {

    }

    @Override
    public void onGetWalletList(List<Wallet> list) {

    }

    @Override
    public void onGetTotalWallet(String wallet) {
        MainActivity.transactinScore = Integer.parseInt(wallet);
    }

    @Override
    public void onGetInvoiceList(List<Invoice> list) {

    }
}
