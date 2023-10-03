package com.creditgaea.sample.credit.java.creditgea.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creditgaea.sample.credit.java.chat.ui.activity.DialogsActivity;
import com.creditgaea.sample.credit.java.chat.utils.TimeUtils;
import com.creditgaea.sample.credit.java.creditgea.adapter.WalletInvoiceAdapter;
import com.creditgaea.sample.credit.java.creditgea.adapter.WalletRecordAdapter;
import com.creditgaea.sample.credit.java.creditgea.adapter.WalletTransactionAdapter;
import com.creditgaea.sample.credit.java.creditgea.model.AddWalletResponse;
import com.creditgaea.sample.credit.java.creditgea.model.Invoice;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.webservice.TransferScoreModel;
import com.creditgaea.sample.credit.java.webservice.UploadCarbonLogToServer;
import com.creditgaea.sample.credit.java.webservice.User;
import com.creditgaea.sample.credit.java.webservice.WalletServiceApi;
import com.google.gson.Gson;
import com.quickblox.sample.videochat.java.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 11/27/2016.
 */

public class ListRecordActivity extends AppCompatActivity implements WalletServiceApi.WalletListener,
        UploadCarbonLogToServer.UploadLogListener,WalletInvoiceAdapter.AdapterInvoiceListener {

    private ArrayList<Wallet> walletRecordList;
    private ArrayList<Result> transactionList;
    private ArrayList<Invoice> invoiceList;
    private Context context;
    private RecyclerView rv_record;
    CustomProgressDialog customProgressDialog;
    private TextView tv_wallet;
    private WalletRecordAdapter walletRecordAdapter;
    private WalletInvoiceAdapter walletInvoiceAdapter;
    private WalletTransactionAdapter walletTransactionAdapter;
    private TextView tv_toolbar_title;
    private User profileInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ListRecordActivity.this;
        setContentView(R.layout.layout_record);
        walletRecordList = new ArrayList<>();
        transactionList = new ArrayList<>();
        invoiceList = new ArrayList<>();
        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);

        if(profileJson!=null){
            profileInfo =  new Gson().fromJson(profileJson, User.class);
        }
        initViews();
        getTotal();

    }

    private void getTotal() {
        new WalletServiceApi(ListRecordActivity.this, ListRecordActivity.this).getTotalWallet();
        if(WalletActivity.recordType.equalsIgnoreCase("wallet")){
            new WalletServiceApi(ListRecordActivity.this, ListRecordActivity.this).getAllWalletTransaction();
        }else if(WalletActivity.recordType.equalsIgnoreCase("invoice")){
            new WalletServiceApi(ListRecordActivity.this, ListRecordActivity.this).getInvoiceList();
        }else {
            new UploadCarbonLogToServer(ListRecordActivity.this, ListRecordActivity.this).getTransferCreditList();
        }
    }

    private void initViews() {

        tv_wallet = findViewById(R.id.tv_wallet);
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        rv_record = findViewById(R.id.rv_record);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        walletRecordAdapter = new WalletRecordAdapter(this,walletRecordList);
        walletInvoiceAdapter = new WalletInvoiceAdapter(this,invoiceList,this);
        walletTransactionAdapter = new WalletTransactionAdapter(this,transactionList);
        rv_record.setLayoutManager(linearLayoutManager);

        if(WalletActivity.recordType.equalsIgnoreCase("wallet")){
            rv_record.setAdapter(walletInvoiceAdapter);
            if(tv_toolbar_title !=null){
                tv_toolbar_title.setText("Record of Cgaea $ Added");
            }

        }else if(WalletActivity.recordType.equalsIgnoreCase("invoice")){
            if(!WalletActivity.isPaidType){
                if(tv_toolbar_title !=null){
                    tv_toolbar_title.setText("C-Gaea $ Request Invoices");
                }

            }else {
                if(tv_toolbar_title !=null){
                    tv_toolbar_title.setText("C-Gaea $ Paid Invoices");
                }

            }

            rv_record.setAdapter(walletInvoiceAdapter);
        }else {
            if(tv_toolbar_title !=null){
                tv_toolbar_title.setText("C-Gaea $ Transaction Record");
            }

            rv_record.setAdapter(walletTransactionAdapter);
        }



    }
    @Override
    public void onAddWallet(AddWalletResponse response) {

    }

    @Override
    public void onUploaded(String response) {

    }

    @Override
    public void onError(String response) {

    }

    @Override
    public void getLogList(List<CarbonLogModel> list) {

    }

    @Override
    public void getTransactionList(List<Result> list) {


        walletTransactionAdapter = new WalletTransactionAdapter(ListRecordActivity.this,list);
        rv_record.setAdapter(walletTransactionAdapter);
    }

    @Override
    public void onGetWalletList(List<Wallet> list) {

        for(int i=0;i<list.size();i++){
            if(list.get(i).getType().equalsIgnoreCase("2")){
                walletRecordList.add(list.get(i));
            }
        }

        if(walletRecordList==null){
            walletRecordList = new ArrayList<>();
        }

        walletRecordAdapter = new WalletRecordAdapter(ListRecordActivity.this,walletRecordList);
        rv_record.setAdapter(walletRecordAdapter);
    }

    @Override
    public void onGetTotalWallet(String wallet) {
        if(tv_wallet!=null){

            if(wallet==null){
                MainActivity.transactinScore = 0;
            }else {
                MainActivity.transactinScore = Integer.parseInt(wallet);
            }
            tv_wallet.setText("Available Cgaea Balance $ "+MainActivity.transactinScore);


        }
    }

    @Override
    public void onGetInvoiceList(List<Invoice> list) {
        invoiceList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            int status = Integer.parseInt(list.get(i).getStatus());
            if(status<3){
                if(WalletActivity.isPaidType){
                    if(status>1){
                        invoiceList.add(list.get(i));
                    }
                }else {
                    if(status==1){
                        invoiceList.add(list.get(i));
                    }
                }
            }


        }

        if(invoiceList==null){
            invoiceList = new ArrayList<>();
        }

        walletInvoiceAdapter = new WalletInvoiceAdapter(ListRecordActivity.this,invoiceList,ListRecordActivity.this);
        rv_record.setAdapter(walletInvoiceAdapter);
    }


    /**
     * @Rehan
     * if isAccept true that mean user accepted the invoice now ready to pay
     * @param isAccept
     * @param invoice
     * status @2 means paid
     *status  @3 means rejected
     *status @1 mean waiting to be approved or rejecte
     */

    @Override
    public void onSelected(boolean isAccept, Invoice invoice) {
        if(isAccept){
            if(MainActivity.transactinScore>=Integer.parseInt(invoice.getMoney())){

                TransferScoreModel transferScoreModel =new TransferScoreModel();
                transferScoreModel.setDate(""+ TimeUtils.getDate());
                transferScoreModel.setReceiverEmail(invoice.getSenderEmail());
                transferScoreModel.setScore(invoice.getMoney());
                transferScoreModel.setSenderUserId(profileInfo.getId());
                transferScoreModel.setSenderEmail(profileInfo.getUserEmail());
                transferScoreModel.setSenderName(invoice.getReceiverName());
                transferScoreModel.setReceiverName(invoice.getSenderName());
                transferScoreModel.setDescription(invoice.getDescription());
                // do transaction
               new WalletServiceApi(this, new WalletServiceApi.WalletListener() {
                   @Override
                   public void onAddWallet(AddWalletResponse response) {

                   }

                   @Override
                   public void onError(String response) {

                       new AlertDialog.Builder(ListRecordActivity.this).setMessage(response).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       }).show();
                   }

                   @Override
                   public void onGetWalletList(List<Wallet> list) {

                   }

                   @Override
                   public void onGetTotalWallet(String wallet) {
                       updateInvoice("2",invoice.getId(),invoice.getUserId());
                   }

                   @Override
                   public void onGetInvoiceList(List<Invoice> list) {

                   }
               }).transferWallet(transferScoreModel);

            }else {
                new AlertDialog.Builder(ListRecordActivity.this).setMessage("Insufficient Balance")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        }else {
            // change status of list
            updateInvoice("3",invoice.getId(),invoice.getUserId());
        }
    }

    public void updateInvoice (String status,String invoiceId,String userId){
        new WalletServiceApi(ListRecordActivity.this, new WalletServiceApi.WalletListener() {
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
                // update wallet response
                getTotal();

            }

            @Override
            public void onGetInvoiceList(List<Invoice> list) {

            }
        }).updateInvoice(status,invoiceId,userId);

    }
}
