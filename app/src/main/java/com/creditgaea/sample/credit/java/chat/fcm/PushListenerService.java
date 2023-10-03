package com.creditgaea.sample.credit.java.chat.fcm;

import android.util.Log;

import com.creditgaea.sample.credit.java.chat.utils.ActivityLifecycle;
import com.creditgaea.sample.credit.java.chat.utils.NotificationUtils;
import com.google.firebase.messaging.RemoteMessage;
import com.quickblox.messages.services.fcm.QBFcmPushListenerService;
import com.creditgaea.sample.credit.java.App;
import com.quickblox.sample.videochat.java.R;
import com.creditgaea.sample.credit.java.creditgea.activity.MainActivity;

import java.util.Map;

public class PushListenerService extends QBFcmPushListenerService {
    private static final String TAG = PushListenerService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;

    protected void showNotification(String message) {
        NotificationUtils.showNotification(App.getInstance(), MainActivity.class,
                App.getInstance().getString(R.string.notification_title), message,
                R.drawable.ic_logo_vector, NOTIFICATION_ID);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    protected void sendPushMessage(Map data, String from, String message) {
        super.sendPushMessage(data, from, message);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + message);
        if (ActivityLifecycle.getInstance().isBackground()) {
            showNotification(message);
        }
    }
}