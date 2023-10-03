package com.creditgaea.sample.credit.java.chat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.quickblox.BuildConfig;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.sample.videochat.java.R;


public class AppInfoActivity extends BaseActivity {

    private TextView appVersionTextView;
    private TextView sdkVersionTextView;
    private TextView appIDTextView;
    private TextView authKeyTextView;
    private TextView authSecretTextView;
    private TextView accountKeyTextView;
    private TextView apiDomainTextView;
    private TextView chatDomainTextView;
    private TextView appQAVersionTextView;

    public static void start(Context context) {
        Intent intent = new Intent(context, AppInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);

        initUI();
        fillUI();
    }

    private void initUI() {
    /*    appVersionTextView = findViewById(R.id.tv_app_version);
        sdkVersionTextView = findViewById(R.id.tv_sdk_version);
        appIDTextView = findViewById(R.id.tv_app_id);
        authKeyTextView = findViewById(R.id.tv_auth_key);
        authSecretTextView = findViewById(R.id.tv_auth_secret);
        accountKeyTextView = findViewById(R.id.tv_account_key);
        apiDomainTextView = findViewById(R.id.tv_api_domain);
        chatDomainTextView = findViewById(R.id.tv_chat_domain);
        appQAVersionTextView = findViewById(R.id.tv_qa_version);*/
    }

    public void fillUI() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.appinfo_title));
        }
        appVersionTextView.setText(BuildConfig.VERSION_NAME);
        sdkVersionTextView.setText(com.quickblox.BuildConfig.VERSION_NAME);
        appIDTextView.setText(QBSettings.getInstance().getApplicationId());
        authKeyTextView.setText(QBSettings.getInstance().getAuthorizationKey());
        authSecretTextView.setText(QBSettings.getInstance().getAuthorizationSecret());
        accountKeyTextView.setText(QBSettings.getInstance().getAccountKey());
        apiDomainTextView.setText(QBSettings.getInstance().getServerApiDomain());
        chatDomainTextView.setText(QBSettings.getInstance().getChatEndpoint());
/*
        if (BuildConfig.IS_QA) {
            String appVersion = BuildConfig.VERSION_NAME;
            String versionQACode = String.valueOf(BuildConfig.VERSION_QA_CODE);
            String qaVersion = appVersion + "." + versionQACode;
            Spannable spannable = new SpannableString(qaVersion);
            spannable.setSpan(new ForegroundColorSpan(Color.RED), appVersion.length() + 1,
                    qaVersion.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            appQAVersionTextView.setText(spannable, TextView.BufferType.SPANNABLE);
            appQAVersionTextView.setVisibility(View.VISIBLE);

            findViewById(R.id.text_qa_version_title).setVisibility(View.VISIBLE);
        }*/
    }
}