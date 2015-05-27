package com.lividpansy.worktracker;

/**
 * Created by mike on 14/02/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by mike on 04/02/2015.
 */
public class WifiChangeDetector extends BroadcastReceiver {
    String LOG_FLAG = WifiChangeDetector.class.getSimpleName();
    TrackerApp tA;


    @Override
    public void onReceive(Context context, Intent intent) {
    tA = (TrackerApp) context.getApplicationContext();

        String action = intent.getAction();
        Log.d(LOG_FLAG, "BROADCAST RECEIVED IS "+action);


        // BELOW IS STANDARD METHOD FOR DETECTING CONNECTIVITY CHANGES - THEN SERVICE IS STARTED TO CHECK IF WIFI IS WORK PLACE WIFI
        ConnectivityManager connectivityChecker = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityChecker.getActiveNetworkInfo();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());


        String wifiName = prefs.getString(context.getString(R.string.pref_wifi_key), context.getString(R.string.pref_wifi_default));


        WifiManager wifiChecker = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiChecker.getConnectionInfo();
        Log.d("WIFI NAME IS ", wifiInfo.getSSID());
        if (networkInfo != null && wifiInfo.getSSID().contentEquals("\""+wifiName+"\"")  ) {
            Log.d(LOG_FLAG, "Service Starting "+wifiName);

                tA.setServiceAction("checkIn");
            tA.setBackgroundService(true);
                tA.setTracker(1);
            intent = new Intent(context, TrackerService.class);

            context.startService(intent);
            Log.d(LOG_FLAG, "STATE CHANGE WIFI");


        }
        else if(networkInfo != null){
            intent = new Intent(context, TrackerService.class);
            tA.setBackgroundService(true);
            tA.setServiceAction("checkOut");
            context.startService(intent);
            Log.d(LOG_FLAG, "STATE CHANGE NO WIFI CHECKOUT");

        }
        else{
            intent = new Intent(context, TrackerService.class);

            context.stopService(intent);
            Log.d(LOG_FLAG, "STATE CHANGE NO CONNECTION");
        }




    }

}



