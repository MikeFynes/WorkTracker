package com.lividpansy.worktracker;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mike on 15/02/2015.
 */
public class BluetoothChangeDetector extends BroadcastReceiver {
    String LOG_FLAG = BluetoothChangeDetector.class.getSimpleName();

    TrackerApp tA;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_FLAG, "BROADCAST RECEIVED");
        tA = (TrackerApp) context.getApplicationContext();

        String action = intent.getAction();
        Log.d(LOG_FLAG, action);
        // When discovery finds a device

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {

            // Get the BluetoothDevice object from the Intent

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(LOG_FLAG, device.getName());
                if(device.getName().contentEquals(tA.getBEACON_NAME())){
                    tA.setBackgroundService(true);
                    Log.d(LOG_FLAG, "Service Starting");
                    tA.setTracker(2);
                    tA.setServiceAction("getUserId");

                    intent = new Intent(context, TrackerService.class);

                    context.startService(intent);
                    Log.d(LOG_FLAG, "STATE CHANGE BEACON");


                }
            else{
                    tA.setBackgroundService(true);
                    Log.d(LOG_FLAG, "Starting");
                    tA.setServiceAction("getUserId");
                    intent = new Intent(context, TrackerService.class);

                    context.startService(intent);
                    Log.d(LOG_FLAG, "STATE CHANGE NO BEACON");
                }
        }
    }
}