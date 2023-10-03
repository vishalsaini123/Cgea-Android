package com.creditgaea.sample.credit.java.creditgea.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.creditgaea.sample.credit.java.chat.ui.activity.DialogsActivity;
import com.creditgaea.sample.credit.java.chat.utils.chat.ChatHelper;
import com.creditgaea.sample.credit.java.creditgea.adapter.AlllogsAdapter;
import com.creditgaea.sample.credit.java.creditgea.model.AddWalletResponse;
import com.creditgaea.sample.credit.java.creditgea.model.Invoice;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;
import com.creditgaea.sample.credit.java.creditgea.utils.CreditGaeaDb;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.creditgea.utils.SessionManager;
import com.creditgaea.sample.credit.java.services.LoginService;
import com.creditgaea.sample.credit.java.utils.Consts;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.webservice.ApiInterface;
import com.creditgaea.sample.credit.java.webservice.UploadCarbonLogToServer;
import com.creditgaea.sample.credit.java.webservice.User;
import com.creditgaea.sample.credit.java.webservice.WalletServiceApi;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.gson.Gson;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.sample.videochat.java.R;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

import static com.creditgaea.sample.credit.java.creditgea.activity.MainActivity.transactinScore;

/**
 * Created by user on 11/27/2016.
 */

public class WalletActivity extends AppCompatActivity implements WalletServiceApi.WalletListener, View.OnClickListener {

    public static boolean isSend;
    public static boolean TransferType;
    private ArrayList<CarbonLogModel> arrayList;
    private Context context;  private ListView lv_logs;
    CustomProgressDialog customProgressDialog;

    private Button btn_transfer;
    private Button btn_request;
    private Button btn_wallet_record;
    private Button btn_sent_record;
    private Button btn_request_send;
    private Button btn_request_record;
    private TextView tv_wallet;
    public static String recordType;
    public static boolean isPaidType;
    private QBUser userForSave;
    public static boolean isSecondTime;
    private TextView tv_user_name;

    private SessionManager sm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = WalletActivity.this;
        setContentView(R.layout.layout_wallet);
        arrayList = new ArrayList<>();
        sm = new SessionManager(context);
        initViews();
        getTotal();

    }



    private void getTotal() {
        new WalletServiceApi(WalletActivity.this,WalletActivity.this).getTotalWallet();
    }

    private void initViews() {
        btn_transfer = findViewById(R.id.btn_transfer);
        btn_request = findViewById(R.id.btn_request);
        btn_wallet_record = findViewById(R.id.btn_wallet_record);
        btn_transfer = findViewById(R.id.btn_transfer);
        btn_sent_record = findViewById(R.id.btn_sent_record);
        btn_request_record = findViewById(R.id.btn_request_record);
        btn_request_send = findViewById(R.id.btn_request_send);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_wallet = findViewById(R.id.tv_wallet);

        btn_request.setOnClickListener(this);
        btn_wallet_record.setOnClickListener(this);
        btn_sent_record.setOnClickListener(this);
        btn_request_record.setOnClickListener(this);
        btn_transfer.setOnClickListener(this);
        btn_request_send.setOnClickListener(this);



        tv_user_name.setText("Cgaea Wallet ID : "+sm.getUserName());

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
        if(tv_wallet!=null){

            if (wallet==null){
                transactinScore = 0;
            }else {
                transactinScore = Integer.parseInt(wallet);
            }
            tv_wallet.setText("Available Cgaea Balance $ "+transactinScore);
        }
    }

    @Override
    public void onGetInvoiceList(List<Invoice> list) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_wallet_record:
                WalletActivity.recordType = "wallet";
                openRecordActivity();
                break;
            case R.id.btn_sent_record:
                WalletActivity.recordType = "transaction";
                openRecordActivity();
                break;

            case R.id.btn_request_record:
                WalletActivity.recordType = "invoice";
                WalletActivity.isPaidType = true;
                openRecordActivity();
                break;
            case R.id.btn_request:
                WalletActivity.recordType = "invoice";
                WalletActivity.isPaidType = false;
                openRecordActivity();
                break;

            case R.id.btn_transfer:
                WalletActivity.isSend= true;
                WalletActivity.TransferType = true;
                restoreChatSession();
                break;
          case R.id.btn_request_send:
                WalletActivity.isSend= true;
                WalletActivity.TransferType = false;
                restoreChatSession();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaidType = false;
        isSend = false;
        if(isSecondTime){
            isSecondTime = false;
            getTotal();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        isSecondTime = true;
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



    private void loginToChat(final QBUser qbUser) {
        //  qbUser.setPassword(App.USER_DEFAULT_PASSWORD);
        userForSave = qbUser;
        startLoginService(qbUser);
    }

    private void startLoginService(QBUser qbUser) {
        Intent tempIntent = new Intent(this, LoginService.class);
        PendingIntent pendingIntent = createPendingResult(Consts.EXTRA_LOGIN_RESULT_CODE, tempIntent, 0);
        LoginService.start(this, qbUser, pendingIntent);
    }

    private void openRecordActivity() {
        startActivity(new Intent(WalletActivity.this,ListRecordActivity.class));
    }
}
