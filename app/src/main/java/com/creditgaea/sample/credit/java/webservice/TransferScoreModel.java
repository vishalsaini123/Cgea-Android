package com.creditgaea.sample.credit.java.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransferScoreModel {

    @SerializedName("senderUserId")
    @Expose
    private String senderUserId;
    @SerializedName("senderEmail")
    @Expose
    private String senderEmail;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("receiverEmail")
    @Expose
    private String receiverEmail;

    @SerializedName("senderName")
    @Expose
    private String senderName;

    @SerializedName("receiverName")
    @Expose
    private String receiverName;

    @SerializedName("description")
    @Expose
    private String description;

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}