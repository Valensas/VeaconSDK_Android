package com.valensas.example;

import android.app.Application;

import com.veacon.sdk.VeaconHandler;
import com.veacon.sdk.VeaconSDK;

/**
 * Created on 6/11/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class SampleApplication extends Application{

    // VeaconHandler is a singleton object to interact with veacon actions
    public static VeaconHandler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        // get VeaconHandler instance
        handler = VeaconSDK.getVeaconHandler();
        // initialize VeaconHandler with your Secret Key, application's context and the class which
        // will be opened after a user clicks on a notification and dismisses the shown message.
        handler.initialize(
                "65b30ec6-974a-4599-ad77-b86eee03c4a4",
                getApplicationContext(),
                MainActivity.class);
        // The icon which will be displayed in the notification bar when a notification is received
        // as a result of a veacon
        handler.setNotificationIcon(R.drawable.ic_launcher);
        // To see additional logs for Veacons, enable LoggingMode.
        handler.setLoggingMode(false);
        // This is necessary for the Veacon service to become enabled when the user restarts
        // his/her phone
        registerActivityLifecycleCallbacks(handler);
    }
}
