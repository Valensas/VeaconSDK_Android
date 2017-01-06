package com.valensas.veaconsdk.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.veacon.sdk.VeaconSDK;
import com.veacon.sdk.listener.CustomActionListener;
import com.veacon.sdk.listener.VeaconStatusListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements CustomActionListener, VeaconStatusListener {

    private Switch monitoring;
    private final static int COARSE_LOCATION_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monitoring = (Switch) findViewById(R.id.monitoring);

        Application.handler.setCustomActionListener(this);
        Application.handler.setVeaconStatusListener(this);
        monitoring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (grantPermissionForLocation()) {
                        startMonitoring();
                    }
                } else {
                    stopMonitoring();
                }
            }
        });
        monitoring.setChecked(true);
    }

    @Override
    public void customActionReceived(String key, HashMap data) {
        if (key.equals("log")) {
            System.err.println((String) ((Map) data.get("entry")).get("proximity"));
        }
    }

    @Override
    public void info(String detail) {
        Toast.makeText(this, detail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(String detail) {
        Toast.makeText(this, detail, Toast.LENGTH_SHORT).show();
    }

    private boolean grantPermissionForLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case COARSE_LOCATION_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMonitoring();
                }
                return;
            }
        }
    }

    /*
        For Android SDK >= 24, on some devices GPS is required to find the Beacon device. Unfortunately, Android's
        Bluetooth Adapter cannot return an error about that. So it is highly recommended that, you should warn the user to open
        his/her GPS or you can do it for him/her using Google's location API.
        Detail: http://stackoverflow.com/questions/29801368/how-to-show-enable-location-dialog-like-google-maps
     */
    private void startMonitoring() {
        int result = Application.handler.startVeaconMonitoring();
        switch (result) {
            case VeaconSDK.MONITORING_STARTED_SUCCESSFULLY:
                break;
            case VeaconSDK.MONITORING_ALREADY_ACTIVE:
                break;
            case VeaconSDK.BLUETOOTH_CLOSED:
                // You can show an Alert Dialog to push user opening his/her bluetooth or
                // you can open it programmatically. BluetoothAdapter.getDefaultAdapter().enable()
                break;
            case VeaconSDK.BLUETOOTH_NOT_SUPPORTED:
                // You can disable all of your features based on Veacon SDK.
                break;
            default:
                break;
        }
    }

    private void stopMonitoring() {
        if (Application.handler.isMonitoringActive()) {
            Application.handler.stopVeaconMonitoring();
        }
    }
}
