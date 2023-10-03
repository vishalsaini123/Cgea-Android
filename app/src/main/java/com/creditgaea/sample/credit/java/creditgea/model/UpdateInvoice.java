package com.creditgaea.sample.credit.java.creditgea.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateInvoice {

@SerializedName("userId")
@Expose
private String userId;
@SerializedName("invoice_id")
@Expose
private String invoiceId;
@SerializedName("status")
@Expose
private String status;

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getInvoiceId() {
return invoiceId;
}

public void setInvoiceId(String invoiceId) {
this.invoiceId = invoiceId;
}

public String getStatus() {
return status;
}

public void setStatus(String status) {
this.status = status;
}

}