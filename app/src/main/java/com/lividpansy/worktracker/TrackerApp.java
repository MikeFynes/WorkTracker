package com.lividpansy.worktracker;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mike on 14/02/2015.
 */
public class TrackerApp extends Application {

    String LOG_FLAG = TrackerApp.class.getSimpleName();

    StatusSender sender;




    private final String SERVER_URL = "http://tc.dev.mw.metropolia.fi/Servlet/Servlet";
    private final String BEACON_NAME = "estimote";
    private String WIFI_NAME;

    private String serviceAction, loginCode;
    WorkTime currentWorkTime;

    private Boolean status = false;
    private Boolean loginStatus =false;
    private Boolean backgroundService = false;
    private int loginId;
    private int tracker;

    ArrayList<WorkTime> lastSevenDays;




    public String getSERVER_URL() {
        return SERVER_URL;
    }

    public String getServiceAction() {
        return serviceAction;
    }

    public void setServiceAction(String serviceAction) {
        this.serviceAction = serviceAction;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;


    }

    public int getTracker() {
        return tracker;
    }

    public void setTracker(int tracker) {
        this.tracker = tracker;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {

        this.status = status;

        Log.d("BACKGROUND SERVICE IS ", getBackgroundService().toString());
        if(getBackgroundService() != true){
            sender.changedStatus();
        }




    }


    public String getBEACON_NAME() {
        return BEACON_NAME;
    }


    public String getLoginCode() {
        return loginCode;
    }



    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
        setServiceAction("getUserId");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString(getString(R.string.pref_login_last), getLoginCode()).commit();

        sendMethodToService();
    }

    public void sendMethodToService(){
        Intent intent = new Intent(this, TrackerService.class);
        startService(intent);
    }

    public void updateList(){
        setServiceAction("updateList");
        sendMethodToService();
    }


    public void loadUserInterface(){


        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    public void wrongCode(){
        if(getBackgroundService() != true){
            sender.changedStatus();
        }
    }

    public ArrayList<WorkTime> getLastSevenDays() {
        return lastSevenDays;
    }

    public void setLastSevenDays(ArrayList<WorkTime> newList) {

       if(getLoginStatus() == true){
           this.lastSevenDays = newList;
           sender.changedStatus();
       }
        else{
           this.lastSevenDays = newList;


           if(getBackgroundService() != true) {
               loadUserInterface();
                setLoginStatus(true);
           }

       }
    }


    public String getWIFI_NAME() {
        return WIFI_NAME;
    }

    public void setWIFI_NAME(String WIFI_NAME) {
        this.WIFI_NAME = WIFI_NAME;
    }



    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean newLoginStatus) {
        this.loginStatus = newLoginStatus;
    }

    public Boolean getBackgroundService() {
        return backgroundService;
    }

    public void setBackgroundService(Boolean backgroundService) {
        this.backgroundService = backgroundService;
    }


}
