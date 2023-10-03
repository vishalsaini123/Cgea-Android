package com.creditgaea.sample.credit.java.creditgea.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice {

@SerializedName("id")
@Expose
private String id;
@SerializedName("user_id")
@Expose
private String userId;
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
@SerializedName("money")
@Expose
private String money;
@SerializedName("date")
@Expose
private String date;
@SerializedName("description")
@Expose
private String description;
@SerializedName("status")
@Expose
private String status;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
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

public String getMoney() {
return money;
}

public void setMoney(String money) {
this.money = money;
}

public String getDate() {
return date;
}

public void setDate(String date) {
this.date = date;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}