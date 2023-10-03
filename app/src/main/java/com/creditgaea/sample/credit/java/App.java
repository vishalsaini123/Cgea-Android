package com.creditgaea.sample.credit.java;

import android.app.Application;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.creditgaea.sample.credit.java.chat.managers.BackgroundListener;
import com.creditgaea.sample.credit.java.chat.utils.ActivityLifecycle;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.sample.videochat.java.R;
import com.creditgaea.sample.credit.java.util.QBResRequestExecutor;


public class App extends Application {
    //App credentials
//    private static final String APPLICATION_ID = "77367";
//    private static final String AUTH_KEY = "54Wm87ZG2ebMOHg";
//    private static final String AUTH_SECRET = "p4mwBBEP24jbzqc";
//    private static final String ACCOUNT_KEY = "K3zJxHqMd2JAEwpbhyyN";
//    public static final String USER_DEFAULT_PASSWORD = "quickblox";

    private static final String APPLICATION_ID = "101860";
    private static final String AUTH_KEY = "DEeMLg6ALefOvMB";
    private static final String AUTH_SECRET = "5mARrNc4qycXGFC";
    private static final String ACCOUNT_KEY = "UuRWwaWLtc6XJ--pz47W";
    public static final String USER_DEFAULT_PASSWORD = "quickblox";

    public static final int CHAT_PORT = 5223;
    public static final int SOCKET_TIMEOUT = 300;
    public static final boolean KEEP_ALIVE = true;
    public static final boolean USE_TLS = true;
    public static final boolean AUTO_JOIN = false;
    public static final boolean AUTO_MARK_DELIVERED = true;
    public static final boolean RECONNECTION_ALLOWED = true;
    public static final boolean ALLOW_LISTEN_NETWORK = true;

    private static App instance;
    private QBResRequestExecutor qbResRequestExecutor;

    //Chat settings range
    private static final int MAX_PORT_VALUE = 65535;
    private static final int MIN_PORT_VALUE = 1000;
    private static final int MIN_SOCKET_TIMEOUT = 300;
    private static final int MAX_SOCKET_TIMEOUT = 60000;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //initFabric();
        ActivityLifecycle.init(this);
        checkChatSettings();
        checkAppCredentials();
        initCredentials();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new BackgroundListener());

    }

    private void checkChatSettings() {
        if ((CHAT_PORT < MIN_PORT_VALUE || CHAT_PORT > MAX_PORT_VALUE)
                || (SOCKET_TIMEOUT < MIN_SOCKET_TIMEOUT || SOCKET_TIMEOUT > MAX_SOCKET_TIMEOUT)) {
            throw new AssertionError(getString(R.string.error_chat_credentails_empty));
        }
    }

 /*   private void initFabric() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }
    }*/

    private void checkAppCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw new AssertionError(getString(R.string.error_credentials_empty));
        }
    }

    private void initCredentials() {
        QBSettings.getInstance().init(getApplicationContext(), APPLICATION_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        // Uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your_api_endpoint.com", "your_chat_endpoint", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
    }

    public synchronized QBResRequestExecutor getQbResRequestExecutor() {
        return qbResRequestExecutor == null
                ? qbResRequestExecutor = new QBResRequestExecutor()
                : qbResRequestExecutor;
    }

    public static App getInstance() {
        return instance;
    }
}