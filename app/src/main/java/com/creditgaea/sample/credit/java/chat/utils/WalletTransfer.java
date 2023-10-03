package com.creditgaea.sample.credit.java.chat.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.creditgaea.sample.credit.java.chat.ui.activity.DialogsActivity;
import com.creditgaea.sample.credit.java.creditgea.activity.MainActivity;
import com.creditgaea.sample.credit.java.creditgea.activity.WalletActivity;
import com.creditgaea.sample.credit.java.creditgea.model.CreateInvoice;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.creditgaea.sample.credit.java.webservice.APIClient;
import com.creditgaea.sample.credit.java.webservice.ApiInterface;
import com.creditgaea.sample.credit.java.webservice.TransferScoreModel;
import com.creditgaea.sample.credit.java.webservice.User;
import com.google.gson.Gson;
import com.quickblox.sample.videochat.java.R;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletTransfer {

    public boolean isWalletTransferOpen = true;
    private User profileInfo;
    Context context;
    private CustomProgressDialog customProgressDialog;
    private ApiInterface apiInterface;
    private walletListener walletListener;
    private String receiverName;

    public interface walletListener{
        public void sendWallet(boolean isAlertShowing, int score,boolean isTransfer);
    }

    public WalletTransfer(Context context,String receiverName,walletListener walletListener){
        this.context = context;
        this.receiverName = receiverName;
        customProgressDialog = new CustomProgressDialog(context);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        this.walletListener = walletListener;

        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);

        if(profileJson!=null){
            profileInfo =  new Gson().fromJson(profileJson,User.class);
        }

        WalletTransferAlert();

    }


    public void WalletTransferAlert() {


        walletListener.sendWallet(true,0,true);

        View layout =((Activity)context).getLayoutInflater().inflate(R.layout.layout_register,
                null);
        final Dialog dialog = setDialog(context, layout);
        final EditText et_transfer= (EditText) layout.findViewById(R.id.et_transfer);
        final EditText et_description= (EditText) layout.findViewById(R.id.et_description);
        final TextView tv_wallet= (TextView) layout.findViewById(R.id.tv_wallet);
        final TextView tv_title= (TextView) layout.findViewById(R.id.tv_title);
        final View view= (View) layout.findViewById(R.id.view);
        int total = MainActivity.transactinScore;
        tv_wallet.setText("My Wallet : $"+total);

       et_transfer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
           @Override
           public void onFocusChange(View v, boolean hasFocus) {
               if(et_transfer.getText().toString().equalsIgnoreCase("0")){
                   if(hasFocus){
                       et_transfer.setText("");
                   }else {
                       et_transfer.setText("0");
                   }
               }
           }
       });

       et_transfer.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               /*if(!s.toString().trim().isEmpty()){
                   int value = Integer.parseInt(s.toString());
                   if(value>total){
                       et_transfer.setTextColor(Color.RED);
                   }else {
                       et_transfer.setTextColor(Color.BLACK);
                   }
               }*/
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        final ImageView iv_close = (ImageView) layout.findViewById(R.id.iv_close);
        Button btnOk = (Button) layout.findViewById(R.id.btn_transfer);
        Button btnRequest = (Button) layout.findViewById(R.id.btn_request);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWalletTransferOpen = false;
                walletListener.sendWallet(false,0,false);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                if(et_transfer.getText()!=null && et_transfer.getText().toString().isEmpty()){
                    valid = false;
                    et_transfer.requestFocus();
                    et_transfer.setError("");
                }else if(Integer.parseInt(et_transfer.getText().toString())>total){
                    valid = false;
                    et_transfer.requestFocus();
                    et_transfer.setError("Insufficient Credit!");
                }
                if(valid){
                    customProgressDialog.show();
                    TransferScoreModel transferScoreModel =new TransferScoreModel();
                    transferScoreModel.setDate(""+TimeUtils.getDate());
                    transferScoreModel.setReceiverEmail(DialogsActivity.selectedReceiverEmail);
                    transferScoreModel.setScore(et_transfer.getText().toString());
                    transferScoreModel.setSenderUserId(profileInfo.getId());
                    transferScoreModel.setSenderEmail(profileInfo.getUserEmail());
                    transferScoreModel.setSenderName(profileInfo.getUserName());
                    transferScoreModel.setReceiverName(receiverName);
                    if(et_description.getText()!=null){
                        transferScoreModel.setDescription(et_description.getText().toString());
                    }
                    Log.e("check transfer","model"+new Gson().toJson(transferScoreModel));

                    Call<ResponseBody> login = apiInterface.transferCredit(transferScoreModel);
                    login.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            customProgressDialog.hide();
                            dialog.dismiss();
                            if (response.code() == 200) {
                                walletListener.sendWallet(false,Integer.parseInt(Objects.requireNonNull(et_transfer.getText()).toString()),true);

                            } else {
                                walletListener.sendWallet(false,0,true);

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            walletListener.sendWallet(false,0,true);
                            customProgressDialog.hide();
                        }
                    });
                }

            }
        });

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                if(et_transfer.getText()!=null && et_transfer.getText().toString().isEmpty()){
                    valid = false;
                    et_transfer.requestFocus();
                    et_transfer.setError("");
                }else if(Integer.parseInt(et_transfer.getText().toString())==0){
                    valid = false;
                    et_transfer.requestFocus();
                    et_transfer.setError("Insufficient Credit!");
                }
                if(valid){
                    customProgressDialog.show();
                    CreateInvoice transferScoreModel =new CreateInvoice();
                    transferScoreModel.setReceiverEmail(DialogsActivity.selectedReceiverEmail);
                    transferScoreModel.setMoney(et_transfer.getText().toString());
                    transferScoreModel.setUserId(profileInfo.getId());
                    transferScoreModel.setSenderEmail(profileInfo.getUserEmail());
                    transferScoreModel.setSenderName(profileInfo.getUserName());
                    transferScoreModel.setReceiverName(receiverName);
                    if(et_description.getText()!=null){
                        transferScoreModel.setDescription(et_description.getText().toString());
                    }
                    Call<ResponseBody> login = apiInterface.createInvoice(transferScoreModel);
                    login.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            customProgressDialog.hide();
                            dialog.dismiss();
                            if (response.code() == 200) {
                                walletListener.sendWallet(false,Integer.parseInt(Objects.requireNonNull(et_transfer.getText()).toString()),false);

                            } else {
                                walletListener.sendWallet(false,0, false);

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            walletListener.sendWallet(false,0,false);
                            customProgressDialog.hide();
                        }
                    });
                }

            }
        });

        if(WalletActivity.isSend){
            view.setVisibility(View.GONE);
            if(WalletActivity.TransferType){
                btnRequest.setVisibility(View.GONE);
                tv_title.setText("Transfer Wallet");
            }else {
                btnOk.setVisibility(View.GONE);
                tv_title.setText("Request Invoice");
            }
            WalletActivity.isSend = false;
        }else {
            view.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);
            btnRequest.setVisibility(View.VISIBLE);
            tv_title.setText("Transfer/Request Wallet");
        }
       // WalletActivity.isSend = false;
        dialog.show();
    }

    public static Dialog setDialog(Context context, View view) {
        Dialog dialog_post_type = new Dialog(context);
        dialog_post_type.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_post_type.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_post_type.setCancelable(true);
        dialog_post_type.setContentView(view);
        return dialog_post_type;
    }

}
