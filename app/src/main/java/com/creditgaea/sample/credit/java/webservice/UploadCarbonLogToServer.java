package com.creditgaea.sample.credit.java.webservice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.creditgaea.sample.credit.java.App;
import com.creditgaea.sample.credit.java.creditgea.activity.LoginActivity;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.creditgea.utils.LogResponse;
import com.creditgaea.sample.credit.java.creditgea.utils.PastTransactionResponse;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.google.gson.Gson;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadCarbonLogToServer {

    private UploadLogListener uploadCarbonLogToServer;
    private ApiInterface apiInterface;
    CustomProgressDialog customProgressDialog;
    Context context;

    public interface UploadLogListener {
        public void onUploaded(String response);
        public void onError(String response);
        public void getLogList(List<CarbonLogModel> list);
        public void getTransactionList(List<Result> list);
    }


    public  UploadCarbonLogToServer( Context context, UploadLogListener uploadCarbonLogToServer) {
        this.uploadCarbonLogToServer = uploadCarbonLogToServer;
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        this.context = context;
        customProgressDialog = new CustomProgressDialog(context);
    }

    public void uploadCarbonLog(CarbonLogModel carbonLogModel) {

        customProgressDialog.show();
        Call<ResponseBody> login = apiInterface.insertLog(carbonLogModel);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customProgressDialog.hide();
                if (response.code() == 200) {
                    uploadCarbonLogToServer.onUploaded("success");
                } else {
                    uploadCarbonLogToServer.onUploaded("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                uploadCarbonLogToServer.onError(t.getMessage());
                customProgressDialog.hide();
            }
        });

    }

    public void updateLog(CarbonLogModel carbonLogModel) {
        customProgressDialog.show();
        Call<ResponseBody> login = apiInterface.updateLog(carbonLogModel);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onUploaded("success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onError(t.getMessage());
            }
        });

    }


    public void getAllLogList() {
        customProgressDialog.show();
        CarbonLogModel carbonLogModel = new CarbonLogModel();
        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
        if(profileJson!=null){
           User user =  new Gson().fromJson(profileJson,User.class);
            carbonLogModel.setUser_id(user.getId());
        }

        Call<LogResponse> login = apiInterface.getAllLogList(carbonLogModel);
        login.enqueue(new Callback<LogResponse>() {
            @Override
            public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                try {
                    customProgressDialog.dismiss();
                }catch (Exception e){

                }

                if(response.code()==200){
                    uploadCarbonLogToServer.getLogList(response.body().getResult());
                }else {
                    uploadCarbonLogToServer.getLogList(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<LogResponse> call, Throwable t) {
                try {
                    customProgressDialog.dismiss();
                }catch (Exception e){

                }
                uploadCarbonLogToServer.onError(t.getMessage());
            }
        });

    }

    public void getTransferCreditList(){
        customProgressDialog.show();
        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
        User user = null;
        if(profileJson!=null){
            user =  new Gson().fromJson(profileJson,User.class);
        }
        Call<PastTransactionResponse> login = apiInterface.getAllTransferTransaction(user);
        login.enqueue(new Callback<PastTransactionResponse>() {
            @Override
            public void onResponse(Call<PastTransactionResponse> call, Response<PastTransactionResponse> response) {
                customProgressDialog.dismiss();
                if(response.code()==200){
                    uploadCarbonLogToServer.getTransactionList(response.body().getResult());
                }else if(response.code() == 400){
                    uploadCarbonLogToServer.getLogList(new ArrayList<>());

                }
            }

            @Override
            public void onFailure(Call<PastTransactionResponse> call, Throwable t) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onError(t.getMessage());
            }
        });

    }


}
