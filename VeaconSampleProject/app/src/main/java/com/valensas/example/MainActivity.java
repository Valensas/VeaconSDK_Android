package com.valensas.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.veacon.sdk.VeaconHandler;
import com.veacon.sdk.listener.CustomActionListener;
import com.veacon.sdk.listener.VeaconStatusListener;

import java.util.HashMap;


public class MainActivity extends Activity implements CustomActionListener,VeaconStatusListener {

    private Switch monitoring;
    private VeaconHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = SampleApplication.handler;

        monitoring = (Switch) findViewById(R.id.monitoring);

        if(handler.isMonitoringActive()){
            monitoring.setChecked(true);
        }else{
            monitoring.setChecked(false);
        }

        monitoring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    handler.startVeaconMonitoring();
                }else{
                    handler.stopVeaconMonitoring();
                }
            }
        });

        handler.setCustomActionListener(this);
        handler.setVeaconStatusListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void customActionReceived(String key, HashMap data) {
        if(key.equals("log")) {
            System.err.println((String) ((HashMap) data.get("entry")).get("proximity"));
        }
    }

    @Override
    public void info(String detail) {
        System.out.println("DetailMessage: "+detail);
    }

    @Override
    public void error(String detail) {
        System.out.println("ErrorMessage: "+detail);
    }
}
