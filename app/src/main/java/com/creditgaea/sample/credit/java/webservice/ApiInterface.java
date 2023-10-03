package com.creditgaea.sample.credit.java.webservice;

import com.creditgaea.sample.credit.java.creditgea.model.AddWalletResponse;
import com.creditgaea.sample.credit.java.creditgea.model.CreateInvoice;
import com.creditgaea.sample.credit.java.creditgea.model.InvoiceListResponse;
import com.creditgaea.sample.credit.java.creditgea.model.InvoiceRequest;
import com.creditgaea.sample.credit.java.creditgea.model.UpdateInvoice;
import com.creditgaea.sample.credit.java.creditgea.model.Wallet;
import com.creditgaea.sample.credit.java.creditgea.model.WalletListResponse;
import com.creditgaea.sample.credit.java.creditgea.utils.CarbonLogModel;
import com.creditgaea.sample.credit.java.creditgea.utils.LogResponse;
import com.creditgaea.sample.credit.java.creditgea.utils.PastTransactionResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("login")
    Call<UserResponse> login(@Body LoginModel params);

    @POST("userRegister")
    Call<UserResponse> register(@Body RegisterModel params);

    @POST("addScore")
    Call<ResponseBody> insertLog(@Body CarbonLogModel params);

    @POST("addinWallet")
    Call<AddWalletResponse> addToWallet(@Body Wallet params);

    @POST("updateScore")
    Call<ResponseBody> updateLog(@Body CarbonLogModel params);

    @POST("getWalletById")
    Call<LogResponse> getAllLogList(@Body CarbonLogModel params);


    @POST("getWallertListMoneyByUserId")
    Call<WalletListResponse> getAllWalletTransaction(@Body CarbonLogModel params);

    @POST("InvoiceListByEmailReceiver")
    Call<InvoiceListResponse> getAllReceiveInvoiceList(@Body InvoiceRequest params);

    @POST("UpdateInvoiceStatus")
    Call<ResponseBody> updateInvoice(@Body UpdateInvoice params);

    @POST("getScoreAllListonId")
    Call<PastTransactionResponse> getAllTransferTransaction(@Body User params);

    @POST("sendScore")
    Call<ResponseBody> transferCredit(@Body TransferScoreModel transferScoreModel);

    @POST("CreateInvoice")
    Call<ResponseBody> createInvoice(@Body CreateInvoice transferScoreModel);

    @POST("getWallertMoneyByUserId")
    Call<AddWalletResponse> getTotalWallet(@Body CarbonLogModel wallet);
}