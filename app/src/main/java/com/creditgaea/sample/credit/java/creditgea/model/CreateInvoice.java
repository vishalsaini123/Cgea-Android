package com.creditgaea.sample.credit.java.creditgea.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateInvoice {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("money")
    @Expose
    private String money;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("sender_email")
    @Expose
    private String senderEmail;
    @SerializedName("receiver_email")
    @Expose
    private String receiverEmail;
    @SerializedName("description")
    @Expose
    private String description;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}