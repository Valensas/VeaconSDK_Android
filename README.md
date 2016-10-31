VEACON SDK
=========
Veacon is a low-powered, low-cost iBeacon transmitter that can notify nearby devices of its presence. Veacons turn any physical location into a beacon transmitting data to iOS and Android devices. VeaconSDK provides the simplest way to you in order to connect your application to our veacon portal. For more information about Veacons please visit [veacon.com].

Current Version
----
1.1

Installation
-----------
Veacon SDK is provided as an Android Library project with all necessary dependencies and configurations. You can start using it by simply downloading [AAR file](https://github.com/Valensas/VeaconSDK_Android/tree/master/VeaconSampleProject/veacon-sdk). If your working IDE is Android Studio, you can Open Module Settings, Add New Module as JAR/AAR file and select the downloaded file. Finally, you should add the following line to your app level gradle file as a dependency:

```c
compile project(':veacon-sdk')
```
In our SDK, we use Gson to handle Json convertion tasks. If you already use Gson in your project, you don't need to do anything, but if you don't use, you need to add one of the versions of Gson in your app level gradle file as a dependency:

```c
compile 'com.google.code.gson:gson:2.5'
```    

After this process, add tools:replace="android:name" in your AndroidManifest as following example:

```c
<application tools:replace="android:name"
        android:name=".Application"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme" >

        <activity
            ...

    </application>
```  


Requirements
-------
VeaconSDK requires devices that have Bluetooth 4.0 LE support and Android 4.3 (API level 18) or above. Other devices that below this specification are not supported by VeaconSDK.

In order to use VeaconSDK, you must have an account on [Veacon Portal]. After you create your account, you can get your secret key from portal.

Usage
------
VeaconSDK contains a singleton object to handle Veacon actions and two different interfaces for callback methods.  
#### Initialization
In order to use Veacon SDK for Android, please create a class which extends Application. If you already have an Application class you should use it for the following configurations. Your basic configuration for VeaconSDK will be as following:
```c
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
                "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
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
```
Now, your application is ready to receive Veacon signals. The only thing you need to do in order to receive signals is enabling the detection mode.

There are a couple of optional configurations you can use in your entire application. You can open or close the logging mode and you can implement listeners in order to receive custom actions you entered from the Veacon portal.
#### Logging Mode
Logging mode is used for debugging. When the logging mode enabled, each time application sees a Veacon change around it, Veacon Portal sends a custom event which contains information about the Veacon. By this way, you can simply debug your application without creating scenarios on Veacon Portal.

In order to enable Logging mode, modify your code as following:
```c
handler.setLoggingMode(true);   
```
#### CustomActionListener
You can implement the CustomActionListener in order to get custom actions you entered as scenario from Veacon Portal. To do so, add the following code to your application:
```c
public class MainActivity extends Activity implements CustomActionListener
```
```c
handler.setCustomActionListener(this);
```
By implementing CustomActionListener, you need to write a new function to your application.

* public void customActionReceived(String key, HashMap data)

The following code simply handles the custom action comes from Logging Mode as an example.
```c
    @Override
    public void customActionReceived(String key, HashMap data) {
        if(key.equals("log")) {
            System.err.println((String) ((HashMap) data.get("entry")).get("proximity"));
        }
    }
```
#### VeaconStatusListener
You can implement the VeaconStatusListener in order to get info and error messages from the Veacon SDK:
```c
public class MainActivity extends Activity implements VeaconStatusListener
```
```c
handler.setVeaconStatusListener(this);
```
By implementing VeaconStatusListener, you need to write two new functions to your application.

* public void info(String detail)
* public void error(String detail)

The following code simply handles the errors and info by printing them.
```c
    @Override
    public void info(String detail) {
        System.out.println("DetailMessage: "+detail);
    }

    @Override
    public void error(String detail) {
        System.out.println("ErrorMessage: "+detail);
    }
```
#### Veacon Monitoring
In order to detect Veacons around your application, you need to start Veacon monitoring. Once you start monitoring, you will start to get actions that you defined in the Veacon Portal. If not, at least you will get logs if you enabled LoggingMode. To start or stop Veacon monitoring as an example we used a Switch to control monitoring. The following lines of code shows the simple process:
```c
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
```
Sample Project
----
You can find the sample project that is used to create this documentation under VeaconSampleProject folder in this repository.
License
----
See LICENSE file

[veacon.com]:http://veacon.com
[Veacon Portal]:http://portal.veacon.com
