package com.creditgaea.sample.credit.java.creditgea.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.creditgaea.sample.credit.java.chat.utils.TimeUtils;
import com.creditgaea.sample.credit.java.creditgea.model.AddWalletResponse;
import com.creditgaea.sample.credit.java.creditgea.model.Invoice;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.webservice.ApiInterface;
import com.creditgaea.sample.credit.java.webservice.UploadCarbonLogToServer;
import com.creditgaea.sample.credit.java.webservice.User;
import com.creditgaea.sample.credit.java.webservice.WalletServiceApi;
import com.google.gson.Gson;
import com.quickblox.sample.videochat.java.R;
import com.creditgaea.sample.credit.java.creditgea.adapter.AlllogsAdapter;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;

import java.util.ArrayList;
import java.util.List;

import static com.creditgaea.sample.credit.java.creditgea.activity.MainActivity.transactinScore;

/**
 * Created by user on 11/27/2016.
 */

public class PastLogsActivity extends AppCompatActivity {
    private ListView lv_logs;
    private TextView tv_emptylogs;
    private ArrayList<CarbonLogModel> arrayList;
    private Context context;
    private LinearLayout lin_netscore;
    private TextView tv_netscore;
    private AlllogsAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        if(isSecondTime){
            isSecondTime = false;
            new UploadCarbonLogToServer(context, new UploadCarbonLogToServer.UploadLogListener() {
                @Override
                public void onUploaded(String response) {
                    //rice holly wood
                }

                @Override
                public void onError(String response) {
                    new AlertDialog.Builder(PastLogsActivity.this).setMessage("" + response)
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
                    AppConstants.saveAllList(PastLogsActivity.this, list);
                    if(list!=null && list.size()>0){
                        arrayList.clear();
                        arrayList = new ArrayList<>();
                        arrayList.addAll(list);
                    }
                    // getTransactionCreditList();
                    if (arrayList.size() > 0 || list.size()>0) {
                        int score = AppConstants.getNetScoreTotal(PastLogsActivity.this);
                        //int transaction = AppConstants.getPastTransactionTotalScore(PastLogsActivity.this);
                        totalScore = score;
                        tv_netscore.setText("Wallet : " + totalScore);
                        tv_transaction.setText("Transactions : "+transactinScore);
                        tv_earn.setText("Earned : "+score);
                        lin_netscore.setVisibility(View.GONE);
                        lv_logs.setVisibility(View.VISIBLE);
                        tv_emptylogs.setVisibility(View.GONE);
                         adapter = new AlllogsAdapter(context, arrayList);
                        lv_logs.setAdapter(adapter);
                        if(arrayList.size()==0 || list.size()==0){
                            tv_emptylogs.setVisibility(View.VISIBLE);
                        }else {
                            if(isValidScore (list)){
                                btn_transfer.setVisibility(View.VISIBLE);
                            }else {
                                btn_transfer.setVisibility(View.GONE);
                            }

                        }
                    } else {
                        lin_netscore.setVisibility(View.GONE);
                        lv_logs.setVisibility(View.GONE);
                        btn_transfer.setVisibility(View.GONE);
                        tv_emptylogs.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void getTransactionList(List<Result> list) {

                }
            }).getAllLogList();
        }
    }

    private boolean isValidScore(List<CarbonLogModel> list) {

        int score = 0;
        for(int i=0;i<list.size();i++){
            try {
                score = Integer.parseInt(score + list.get(i).getScore());

            }catch (Exception e){

            }
        }

        return score > 0;

    }

    private TextView tv_earn;
    private TextView tv_transaction;
    private ApiInterface apiInterface;

    CustomProgressDialog customProgressDialog;

    private Button btn_transfer;
    private int totalScore;
    public static boolean isSecondTime;

    @Override
    protected void onStop() {
        super.onStop();
        isSecondTime = true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = PastLogsActivity.this;
        setContentView(R.layout.alllogslayout);
        arrayList = new ArrayList<>();
        lv_logs = (ListView) findViewById(R.id.list_logs);
        lin_netscore = (LinearLayout) findViewById(R.id.lin_netscore);
        tv_emptylogs = (TextView) findViewById(R.id.tv_nonlogview);
        tv_netscore = (TextView) findViewById(R.id.tv_netscore);
        tv_earn = (TextView) findViewById(R.id.tv_earn);
        tv_transaction = (TextView) findViewById(R.id.tv_transaction);
        btn_transfer = (Button) findViewById(R.id.btn_transfer);
        customProgressDialog = new CustomProgressDialog(PastLogsActivity.this);

        new UploadCarbonLogToServer(context, new UploadCarbonLogToServer.UploadLogListener() {
            @Override
            public void onUploaded(String response) {
                //rice holly wood
            }

            @Override
            public void onError(String response) {
                new AlertDialog.Builder(PastLogsActivity.this).setMessage("" + response)
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
                AppConstants.saveAllList(PastLogsActivity.this, list);
               // getTransactionCreditList();

                arrayList.clear();
                arrayList = new ArrayList<>();
                arrayList.addAll(list);
                
                if (arrayList.size() > 0 || list.size()>0) {
                    int score = AppConstants.getNetScoreTotal(PastLogsActivity.this);
                    //int transaction = AppConstants.getPastTransactionTotalScore(PastLogsActivity.this);
                     totalScore = score;
                    tv_netscore.setText("Wallet : " + totalScore);
                    tv_transaction.setText("Transactions : "+transactinScore);
                    tv_earn.setText("Earned : "+score);
                    lin_netscore.setVisibility(View.GONE);
                    lv_logs.setVisibility(View.VISIBLE);
                    tv_emptylogs.setVisibility(View.GONE);
                     adapter = new AlllogsAdapter(context, new ArrayList<>(list));
                    lv_logs.setAdapter(adapter);
                    btn_transfer.setVisibility(View.VISIBLE);
                    tv_emptylogs.setVisibility(View.GONE);
                    if(list.size()==0){
                        tv_emptylogs.setVisibility(View.VISIBLE);
                    }else {
                        if(isValidScore (list)){
                            btn_transfer.setVisibility(View.VISIBLE);
                        }else {
                            btn_transfer.setVisibility(View.GONE);
                        }
                    }
                } else {

                    lin_netscore.setVisibility(View.GONE);
                    lv_logs.setVisibility(View.GONE);
                    btn_transfer.setVisibility(View.GONE);
                    tv_emptylogs.setVisibility(View.VISIBLE);
                }
                
                


            }

            @Override
            public void getTransactionList(List<Result> list) {

            }
        }).getAllLogList();


        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalScore>0){
                    int walletMoney = totalScore*10;
                    Wallet wallet = new Wallet();
                    String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
                    if(profileJson!=null){
                        User user =  new Gson().fromJson(profileJson,User.class);
                        wallet.setUserId(Integer.valueOf(user.getId()));
                    }
                    wallet.setWallet(""+walletMoney);
                    wallet.setType("2");
                    wallet.setDate(TimeUtils.getDate());
                    new WalletServiceApi(PastLogsActivity.this, new WalletServiceApi.WalletListener() {
                        @Override
                        public void onAddWallet(AddWalletResponse response) {
                            startActivity(new Intent(PastLogsActivity.this,WalletActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(String response) {

                        }

                        @Override
                        public void onGetWalletList(List<Wallet> list) {

                        }

                        @Override
                        public void onGetTotalWallet(String wallet) {
                            try {
                                transactinScore = Integer.parseInt(wallet);
                            }catch (Exception e){

                            }

                        }

                        @Override
                        public void onGetInvoiceList(List<Invoice> list) {

                        }
                    }).addInWallet(wallet);
                }
            }
        });



    }

    public void getTransactionCreditList() {
        new UploadCarbonLogToServer(context, new UploadCarbonLogToServer.UploadLogListener() {
            @Override
            public void onUploaded(String response) {
                //rice holly wood
            }

            @Override
            public void onError(String response) {
                new AlertDialog.Builder(PastLogsActivity.this).setMessage("" + response)
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
                Log.e("check list result ",""+new Gson().toJson(list));
                AppConstants.savePastTransactions(PastLogsActivity.this, list);

                if (arrayList.size() > 0 || transactinScore>0) {
                    int score = AppConstants.getNetScoreTotal(PastLogsActivity.this);
                    //int transaction = AppConstants.getPastTransactionTotalScore(PastLogsActivity.this);
                    int totalScore = transactinScore;
                    tv_netscore.setText("Wallet : " + totalScore);
                    tv_transaction.setText("Transactions : "+transactinScore);
                    tv_earn.setText("Earned : "+score);
                    lin_netscore.setVisibility(View.GONE);
                    lv_logs.setVisibility(View.VISIBLE);
                    tv_emptylogs.setVisibility(View.GONE);
                    AlllogsAdapter adapter = new AlllogsAdapter(context, arrayList);
                    lv_logs.setAdapter(adapter);
                    if(arrayList.size()==0){
                        tv_emptylogs.setVisibility(View.VISIBLE);
                    }else {
                        btn_transfer.setVisibility(View.GONE);
                    }
                } else {
                    lin_netscore.setVisibility(View.GONE);
                    lv_logs.setVisibility(View.GONE);
                    btn_transfer.setVisibility(View.GONE);
                    tv_emptylogs.setVisibility(View.VISIBLE);
                }

            }
        }).getTransferCreditList();
    }


}
