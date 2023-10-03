package com.creditgaea.sample.credit.java.webservice;

import android.content.Context;
import android.util.Log;

import com.creditgaea.sample.credit.java.creditgea.model.AddWalletResponse;
import com.creditgaea.sample.credit.java.creditgea.model.Invoice;
import com.creditgaea.sample.credit.java.creditgea.model.InvoiceListResponse;
import com.creditgaea.sample.credit.java.creditgea.model.InvoiceRequest;
import com.creditgaea.sample.credit.java.creditgea.model.UpdateInvoice;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.model.WalletListResponse;
import com.creditgaea.sample.credit.java.creditgea.utils.AppConstants;
import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;
import com.creditgaea.sample.credit.java.creditgea.utils.CustomProgressDialog;
import com.creditgaea.sample.credit.java.creditgea.utils.LogResponse;
import com.creditgaea.sample.credit.java.creditgea.utils.PastTransactionResponse;
import com.creditgaea.sample.credit.java.creditgea.utils.Result;
import com.creditgaea.sample.credit.java.utils.SharedPrefsHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletServiceApi {

    private WalletListener uploadCarbonLogToServer;
    private ApiInterface apiInterface;
    CustomProgressDialog customProgressDialog;
    Context context;

    public interface WalletListener {
        public void onAddWallet(AddWalletResponse response);
        public void onError(String response);
        public void onGetWalletList(List<Wallet> list);
        public void onGetTotalWallet(String wallet);
        public void onGetInvoiceList(List<Invoice> list);
    }


    public WalletServiceApi(Context context, WalletListener uploadCarbonLogToServer) {
        this.uploadCarbonLogToServer = uploadCarbonLogToServer;
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        this.context = context;
        customProgressDialog = new CustomProgressDialog(context);
    }

    public void addInWallet(Wallet wallet) {

        customProgressDialog.show();
        Call<AddWalletResponse> addWalletCall = apiInterface.addToWallet(wallet);
        addWalletCall.enqueue(new Callback<AddWalletResponse>() {
            @Override
            public void onResponse(Call<AddWalletResponse> call, Response<AddWalletResponse> response) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onAddWallet(response.body());
            }

            @Override
            public void onFailure(Call<AddWalletResponse> call, Throwable t) {
                uploadCarbonLogToServer.onError(t.getMessage());
                customProgressDialog.hide();
            }
        });

    }

    public void getTotalWallet() {
        CarbonLogModel wallet = new CarbonLogModel();

        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
        if(profileJson!=null){
            User user =  new Gson().fromJson(profileJson,User.class);
            wallet.setUser_id(user.getId());
        }
        try {
            if(customProgressDialog==null){
                customProgressDialog = new CustomProgressDialog(context);
            }
            customProgressDialog.show();
        }catch (Exception e){

        }

        Call<AddWalletResponse> addWalletCall = apiInterface.getTotalWallet(wallet);
        addWalletCall.enqueue(new Callback<AddWalletResponse>() {
            @Override
            public void onResponse(Call<AddWalletResponse> call, Response<AddWalletResponse> response) {
                try {
                    customProgressDialog.dismiss();
                }catch (Exception e){

                }

                if (response.code() == 200) {
                    uploadCarbonLogToServer.onGetTotalWallet(response.body().getWallet().getWallet());
                } else {
                    uploadCarbonLogToServer.onGetTotalWallet("0");
                }
            }

            @Override
            public void onFailure(Call<AddWalletResponse> call, Throwable t) {
                uploadCarbonLogToServer.onError(t.getMessage());

                try {
                    customProgressDialog.dismiss();
                }catch (Exception e){

                }

            }
        });

    }



    public void getAllWalletTransaction() {
        customProgressDialog.show();
        CarbonLogModel carbonLogModel = new CarbonLogModel();
        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
        if(profileJson!=null){
           User user =  new Gson().fromJson(profileJson,User.class);
            carbonLogModel.setUser_id(user.getId());
        }

        Call<WalletListResponse> login = apiInterface.getAllWalletTransaction(carbonLogModel);
        login.enqueue(new Callback<WalletListResponse>() {
            @Override
            public void onResponse(Call<WalletListResponse> call, Response<WalletListResponse> response) {
                customProgressDialog.hide();
                if(response.code()==200){
                    uploadCarbonLogToServer.onGetWalletList(response.body().getWallet());
                }else if(response.code() == 400){
                    uploadCarbonLogToServer.onGetWalletList(new ArrayList<>());

                }
            }

            @Override
            public void onFailure(Call<WalletListResponse> call, Throwable t) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onError(t.getMessage());
            }
        });

    }


    public void getInvoiceList() {
        customProgressDialog.show();
        InvoiceRequest carbonLogModel = new InvoiceRequest();
        String profileJson = SharedPrefsHelper.getInstance().get(AppConstants.USER_INFO);
        User user = null;
        if(profileJson!=null){
             user =  new Gson().fromJson(profileJson,User.class);
        }

        carbonLogModel.setEmail(user.getUserEmail());

        Call<InvoiceListResponse> login = apiInterface.getAllReceiveInvoiceList(carbonLogModel);
        login.enqueue(new Callback<InvoiceListResponse>() {
            @Override
            public void onResponse(Call<InvoiceListResponse> call, Response<InvoiceListResponse> response) {
                customProgressDialog.hide();
                if(response.code()==200){
                    uploadCarbonLogToServer.onGetInvoiceList(response.body().getInvoice());
                }else if(response.code() == 400){
                    uploadCarbonLogToServer.onGetInvoiceList(new ArrayList<>());

                }
            }

            @Override
            public void onFailure(Call<InvoiceListResponse> call, Throwable t) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onError(t.getMessage());
            }
        });

    }


    public void updateInvoice(String status,String invoiceId,String userId) {
        customProgressDialog.show();
        UpdateInvoice updateInvoice = new UpdateInvoice();
        updateInvoice.setUserId(userId);
        updateInvoice.setStatus(status);
        updateInvoice.setInvoiceId(invoiceId);

        Call<ResponseBody> login = apiInterface.updateInvoice(updateInvoice);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customProgressDialog.hide();
                if(response.code()==200){
                    uploadCarbonLogToServer.onGetTotalWallet("success");
                }else if(response.code() == 400){
                    uploadCarbonLogToServer.onGetInvoiceList(new ArrayList<>());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onError(t.getMessage());
            }
        });

    }

    public void transferWallet(TransferScoreModel transferScoreModel) {
        Log.e("check transfer","model"+new Gson().toJson(transferScoreModel));
        customProgressDialog.show();
        Call<ResponseBody> login = apiInterface.transferCredit(transferScoreModel);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                customProgressDialog.hide();
                customProgressDialog.dismiss();
                if (response.code() == 200) {
                    uploadCarbonLogToServer.onGetTotalWallet("success");
                } else {
                    uploadCarbonLogToServer.onGetTotalWallet("");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                customProgressDialog.hide();
                uploadCarbonLogToServer.onError(t.getMessage());
            }
        });

    }

}
