package com.valensas.veaconsdk.app;

import com.veacon.sdk.VeaconHandler;
import com.veacon.sdk.VeaconSDK;

/**
 * Created on 5/26/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class Application extends android.app.Application {

    public static VeaconHandler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = VeaconSDK.getVeaconHandler();
        handler.initialize("11111111-1111-1111-1111-111111111111", getApplicationContext(), MainActivity.class);
        handler.setNotificationIcon(R.drawable.ic_launcher);
        handler.setLoggingMode(true);
        registerActivityLifecycleCallbacks(handler);
    }

}
