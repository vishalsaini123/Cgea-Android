package com.creditgaea.sample.credit.java.creditgea.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creditgaea.sample.credit.java.App;
import com.creditgaea.sample.credit.java.chat.ui.activity.AppInfoActivity;
import com.creditgaea.sample.credit.java.chat.ui.activity.BaseActivity;
import com.creditgaea.sample.credit.java.chat.utils.chat.ChatHelper;
import com.creditgaea.sample.credit.java.creditgea.utils.APIError;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.creditgea.utils.ErrorUtils;
import com.creditgaea.sample.credit.java.creditgea.utils.SessionManager;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.utils.ValidationUtils;
import com.creditgaea.sample.credit.java.webservice.APIClient;
import com.creditgaea.sample.credit.java.webservice.ApiInterface;
import com.creditgaea.sample.credit.java.webservice.LoginModel;
import com.creditgaea.sample.credit.java.webservice.RegisterModel;
import com.creditgaea.sample.credit.java.webservice.UserResponse;
import com.google.gson.Gson;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.videochat.java.R;
import com.quickblox.users.model.QBUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private static final int UNAUTHORIZED = 401;
    private static final String DRAFT_LOGIN = "draft_login";
    private static final String DRAFT_USERNAME = "draft_username";

    private EditText loginEt;
    private EditText passwordEt;
    private EditText et_userName;
    private TextView loginHint;
    private TextView usernameHint;
    private Button btnLogin;
    private CheckBox chbSave;
    private LinearLayout rootView;
    private LinearLayout hidableHolder;
    private ApiInterface apiInterface;
    CustomProgressDialog customProgressDialog;
    private TextView tv_label_login;
    private TextView tv_btn_login_signup;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_chat);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        customProgressDialog = new CustomProgressDialog(LoginActivity.this);
        initViews();
        prepareListeners();
        changeLoginBtnLabel();
        tv_btn_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_userName.getVisibility() == View.VISIBLE) {
                    et_userName.setVisibility(View.GONE);
                } else {
                    et_userName.setVisibility(View.VISIBLE);
                }

                changeLoginBtnLabel();
            }
        });
        // fillViews();
        //  defineFocusedBehavior();
    }

    private void changeLoginBtnLabel() {
        if (et_userName.getVisibility() == View.VISIBLE) {
            tv_label_login.setText("Already a Creditgaea User? ");
            tv_btn_login_signup.setText("Sign In");
            btnLogin.setText("Sign Up");
        } else {
            tv_label_login.setText("New User? ");
            tv_btn_login_signup.setText("Sign Up");
            btnLogin.setText("Sign In");
        }
    }

    private void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(20f);
        }
        loginHint = findViewById(R.id.tv_login_hint);
        usernameHint = findViewById(R.id.tv_username_hint);
        btnLogin = findViewById(R.id.tv_btn_login);
        loginEt = findViewById(R.id.et_login);
        passwordEt = findViewById(R.id.et_password);
        et_userName = findViewById(R.id.et_user_name);
        tv_btn_login_signup = findViewById(R.id.tv_btn_login_signup);
        tv_label_login = findViewById(R.id.tv_label_login);
        chbSave = findViewById(R.id.chb_login_save);
        //  btnLogin = findViewById(R.id.tv_btn_login);
        hidableHolder = findViewById(R.id.ll_hidable_holder);
        rootView = findViewById(R.id.root_view_login_activity);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
    }

    private void fillViews() {
        String draftLogin = SharedPrefsHelper.getInstance().get(DRAFT_LOGIN, null);
        String draftUserName = SharedPrefsHelper.getInstance().get(DRAFT_USERNAME, null);

        if (!TextUtils.isEmpty(draftLogin)) {
            loginEt.setText(draftLogin);
        }
        if (!TextUtils.isEmpty(draftUserName)) {
            passwordEt.setText(draftUserName);
        }
        validateFields();
    }

    private void defineFocusedBehavior() {
        loginHint.setVisibility(View.GONE);
        usernameHint.setVisibility(View.GONE);

        loginEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (hasFocus) {
                        loginEt.setTranslationZ(10f);
                    } else {
                        loginEt.setTranslationZ(0f);
                    }
                }
                if (ValidationUtils.isLoginValid(LoginActivity.this, loginEt)) {
                    loginHint.setVisibility(View.GONE);
                } else {
                    loginHint.setVisibility(View.VISIBLE);
                }
            }
        });

        passwordEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (hasFocus) {
                        passwordEt.setTranslationZ(10f);
                    } else {
                        passwordEt.setTranslationZ(0f);
                    }
                }
                if (ValidationUtils.isLoginValid(LoginActivity.this, passwordEt)) {
                    usernameHint.setVisibility(View.GONE);
                } else {
                    usernameHint.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void prepareListeners() {
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                chbSave.setChecked(!chbSave.isChecked());

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (chbSave.isChecked()) {
                    if (vibrator != null) {
                        vibrator.vibrate(80);
                    }
                } else {
                    if (vibrator != null) {
                        vibrator.vibrate(250);
                    }
                }
                return true;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_userName.getVisibility() == View.VISIBLE) {
                    if (et_userName.getText().toString().isEmpty()) {
                        et_userName.setError("Field can't be left blank!");
                        et_userName.requestFocus();
                    } else if (validateFields()) {

                        if (passwordEt.getText().toString().length() >= 3) {

                            RegisterModel registerModel = new RegisterModel();
                            registerModel.setPassword(passwordEt.getText().toString());
                            registerModel.setUserName(et_userName.getText().toString());
                            registerModel.setUserEmail(loginEt.getText().toString());
                            RegisterUser(registerModel);

                        } else {
                            passwordEt.setError("Password must be at least 3 character");
                            passwordEt.requestFocus();
                        }


                    }

                } else {
                    if (validateFields()) {
                        customProgressDialog.show();
                        prepareUser();
                    }
                }


            }
        });

        loginEt.addTextChangedListener(new TextWatcherListener(loginEt));
        passwordEt.addTextChangedListener(new TextWatcherListener(passwordEt));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login_app_info:
                AppInfoActivity.start(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Boolean validateFields() {

        boolean isValid = true;
        if (loginEt.getText().toString().isEmpty()) {
            isValid = false;
            loginEt.setError("Can't be left blank");
        } else if (!com.creditgaea.sample.credit.java.creditgea.utils.ValidationUtils.isValidEmail(loginEt.getText().toString())) {
            isValid = false;
            loginEt.setError("Invalid Email!");
        } else if (passwordEt.getText().toString().isEmpty()) {
            loginEt.setError("Can't be left blank");
            isValid = false;
        }
        return isValid;
    }

    private void saveDrafts() {
        SharedPrefsHelper.getInstance().save(DRAFT_LOGIN, loginEt.getText().toString());
        SharedPrefsHelper.getInstance().save(DRAFT_USERNAME, passwordEt.getText().toString());
    }

    private void clearDrafts() {
        SharedPrefsHelper.getInstance().save(DRAFT_LOGIN, "");
        SharedPrefsHelper.getInstance().save(DRAFT_USERNAME, "");
    }

    private void prepareUser() {
        LoginModel user = new LoginModel();
        user.setUserName(loginEt.getText().toString());
        user.setPassword(passwordEt.getText().toString());
        Login(user);
    }


    public void Login(LoginModel user) {
        Call<UserResponse> login = apiInterface.login(user);
        login.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                customProgressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getUser() != null) {
                    QBUser qbUser = new QBUser();
                    SharedPrefsHelper.getInstance().save(AppConstants.USER_INFO, new Gson().toJson(response.body().getUser()));
                    Log.e("check email", " " + new Gson().toJson(response.body().getUser().getUserEmail()));
                    Log.e("check email", " " + new Gson().toJson(response.body().getUser().getUserName()));
                    qbUser.setLogin(response.body().getUser().getUserEmail());
                    qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
                    qbUser.setFullName(response.body().getUser().getUserName());
               /*     qbUser.setId(Integer.parseInt(response.body().getUser().getId()));
                    qbUser.setPhone(response.body().getUser().getUserPhone());
                    qbUser.setEmail(response.body().getUser().getUserEmail());
    */
                    signIn(qbUser);

/*
                    SharedPrefsHelper.getInstance().saveQbUser(qbUser);
                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                    if (!chbSave.isChecked()) {
                        clearDrafts();
                    }
                    sessionManager.saveUserName(qbUser.getFullName());
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    hideProgressDialog();*/
                } else {
                    customProgressDialog.dismiss();
                    new AlertDialog.Builder(LoginActivity.this).setCancelable(false)
                            .setTitle("Error!").setMessage("Invalid Username or Password!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgressDialog();
                new AlertDialog.Builder(LoginActivity.this).setCancelable(false)
                        .setTitle("Error!").setMessage("Please check your internet connection!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

    }

    /**
     * Register user
     * @param registerModel
     */
    public void RegisterUser(RegisterModel registerModel) {
        customProgressDialog.show();
        Call<UserResponse> login = apiInterface.register(registerModel);
        login.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                customProgressDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getUser() != null) {
                    QBUser qbUser = new QBUser();
                    SharedPrefsHelper.getInstance().save(AppConstants.USER_INFO, new Gson().toJson(response.body().getUser()));
                    Log.e("check email", " " + new Gson().toJson(response.body().getUser().getUserEmail()));
                    Log.e("check email", " " + new Gson().toJson(response.body().getUser().getUserName()));
                    qbUser.setLogin(response.body().getUser().getUserEmail());
                    qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
                    qbUser.setFullName(response.body().getUser().getUserName());
                    signUp(qbUser);
                } else {

                    APIError error = ErrorUtils.parseError(response);
                    String errorMessage = error.message();

                    if (errorMessage == null) {
                        errorMessage = "Something went wrong";
                    }

                    new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this)
                            .setCancelable(false)
                            .setMessage(errorMessage)
                            .setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface dialog, int which) {
                                        }
                                    })
                            .show();

                  /*  Toast.makeText(LoginActivity.this, ""+new Gson().toJson(response.body()), Toast.LENGTH_SHORT).show();
                    customProgressDialog.dismiss();
                    new AlertDialog.Builder(LoginActivity.this).setCancelable(false)
                            .setTitle("Error!").setMessage("User Already Exist!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();*/
                }

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgressDialog();
                new AlertDialog.Builder(LoginActivity.this).setCancelable(false)
                        .setTitle("Error!").setMessage("Please check your internet connection!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

    }

    private void signIn(final QBUser user) {
        //showProgressDialog(R.string.dlg_login);
        customProgressDialog.show();
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {


                if (userFromRest.getFullName().equalsIgnoreCase(user.getFullName())) {
                    //user.setFullName(userFromRest.getFullName());
                    loginToChat(user);
                } else {

                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                    // show user not exist
                /*    new AlertDialog.Builder(LoginActivity.this).setCancelable(false)
                            .setTitle("Error!").setMessage("Invalid UserId or Password!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();*/
                }
            }

            @Override
            public void onError(QBResponseException e) {
                customProgressDialog.dismiss();
                if (e.getHttpStatusCode() == UNAUTHORIZED) {
                    // signUp(user);
                } else {
                    hideProgressDialog();
                    showErrorSnackbar(R.string.login_chat_login_error, e, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signIn(user);
                        }
                    });
                }
            }
        });
    }

    private void signUp(final QBUser user) {
        //showProgressDialog(R.string.dlg_login);
        // user.setFullName(et_userName.getText().toString());
        customProgressDialog.show();
        ChatHelper.getInstance().createNewUser(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                
                if (userFromRest.getFullName().equalsIgnoreCase(user.getFullName())) {
                    //user.setFullName(userFromRest.getFullName());
                    loginToChat(user);
                } else {

                    //Need to set password NULL, because server will update user only with NULL password
                    user.setPassword(null);
                    updateUser(user);
                    // show user not exist
                /*    new AlertDialog.Builder(LoginActivity.this).setCancelable(false)
                            .setTitle("Error!").setMessage("Invalid UserId or Password!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();*/
                }
            }

            @Override
            public void onError(QBResponseException e) {
                customProgressDialog.dismiss();
                if (e.getHttpStatusCode() == UNAUTHORIZED) {
                    signUp(user);
                } else {
                    hideProgressDialog();
                    showErrorSnackbar(R.string.login_chat_login_error, e, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signIn(user);
                        }
                    });
                }
            }
        });
    }

    private void updateUser(final QBUser user) {
        ChatHelper.getInstance().updateUser(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {

                loginToChat(user);
            }

            @Override
            public void onError(QBResponseException e) {
                customProgressDialog.dismiss();
                showErrorSnackbar(R.string.login_chat_login_error, e, null);
            }
        });
    }

 /*   private void signUp(final QBUser newUser) {
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
                showErrorSnackbar(R.string.login_sign_up_error, e, null);
            }
        });
    }*/

    private void loginToChat(final QBUser user) {

        //Need to set password, because the server will not register to chat without password
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                customProgressDialog.dismiss();
                SharedPrefsHelper.getInstance().saveQbUser(user);
                SessionManager sessionManager = new SessionManager(LoginActivity.this);
                if (!chbSave.isChecked()) {
                    clearDrafts();
                }
                sessionManager.saveUserName(user.getFullName());
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                hideProgressDialog();
            }

            @Override
            public void onError(QBResponseException e) {
                customProgressDialog.dismiss();
                hideProgressDialog();
                showErrorSnackbar(R.string.login_chat_login_error, e, null);
            }
        });
    }


    private class TextWatcherListener implements TextWatcher {
        private EditText editText;
        /*    private Timer timer = new Timer();*/

        private TextWatcherListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
         /*   String text = s.toString().replace("  ", " ");
            if (!editText.getText().toString().equals(text)) {
                editText.setText(text);
                editText.setSelection(text.length());
            }
            validateFields();*/
        }

        @Override
        public void afterTextChanged(Editable s) {
         /*   timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    saveDrafts();
                }
            }, 300);*/
        }
    }
}