package com.creditgaea.sample.credit.java.creditgea.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.quickblox.sample.videochat.java.R;


public class CustomProgressDialog extends AlertDialog {
public CustomProgressDialog(Context context) {
    super(context);
    getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
}

@Override
public void show() {
    try {
        super.show();
    }catch (Exception e){

    }

    setContentView(R.layout.waitlayout);
 }
}