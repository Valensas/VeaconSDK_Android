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

    private void startMonitoring() {
        if (!Application.handler.isMonitoringActive()) {
            Application.handler.startVeaconMonitoring();
        }
    }

    private void stopMonitoring() {
        if (Application.handler.isMonitoringActive()) {
            Application.handler.stopVeaconMonitoring();
        }
    }
}
