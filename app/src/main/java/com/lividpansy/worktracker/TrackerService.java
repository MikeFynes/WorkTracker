package com.lividpansy.worktracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mike on 14/02/2015.
 */
public class TrackerService extends Service implements AsyncListener {
    TrackerApp tA;
    String LOG_FLAG = TrackerService.class.getSimpleName();
    SharedPreferences prefs;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {


        tA = (TrackerApp) getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = prefs.getInt(getString(R.string.pref_user_key), 0);
        switch(tA.getServiceAction()){

            case "checkIn":

                if(userId>0){
                    tA.setLoginId(userId);
                    checkIn();
                }
                else{
                    fetchUser();
                }

                break;
            case "checkOut":


                if(userId>0){
                    tA.setLoginId(userId);
                    checkOut();
                }
                else{
                    fetchUser();
                }

                break;



            case "getUserId":

                fetchUser();
                break;

            case "updateList":
                getDetailsForList();

                break;

            default:

                break;




        }



        return Service.START_NOT_STICKY;

    }

    @Override
    public void workTimeId(long result) {
        tA.currentWorkTime.setId(result);
        tA.setStatus(true);
        Log.d("CHECK IN ", "SUCCESS");
    }

    @Override
    public void checkOutSuccess() {
        tA.setStatus(false);
        getDetailsForList();
        Log.d("CHECKOUT ", "SUCCESS");
    }

    @Override
    public void loginSuccess(int result, String wifiName) {
        tA.setLoginId(result);
        tA.setWIFI_NAME(wifiName);
        Log.d(LOG_FLAG + " BACKGROUND SERVICE IS ", tA.getBackgroundService().toString());

        prefs.edit().putString(getString(R.string.pref_wifi_key), tA.getWIFI_NAME()).apply();
        prefs.edit().putInt(getString(R.string.pref_user_key), result).apply();

        if(tA.getServiceAction().equals("checkIn")){
            checkIn();
        }
        else if(tA.getServiceAction().equals("checkOut")){
            checkOut();
        }




        if(tA.getBackgroundService() != true){
            getDetailsForList();
        }

    }

    @Override
    public void loginFail() {
            tA.wrongCode();
    }

    @Override
    public void sendList(ArrayList<WorkTime> result) {

         tA.setLastSevenDays(result);

    }

    @Override
    public void listIsEmpty() {
        tA.loadUserInterface();
    }

    public void checkIn(){
        if(tA.getStatus() == false){
            Log.d(LOG_FLAG, "CHECK IN STARTED");

            long dateInMillis = new Date().getTime();

            tA.currentWorkTime = new WorkTime(tA.getLoginId(),tA.getTracker(), dateInMillis);

            AsyncCheckIn checkIn = new AsyncCheckIn();
            checkIn.SERVER = tA.getSERVER_URL();
            checkIn.listener = this;
            checkIn.execute(tA.currentWorkTime);


            }
        else{

            Log.d(LOG_FLAG, "CHECK IN FAIL");

            }
        }


    public void checkOut(){
        ConnectivityManager connectivityChecker = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityChecker.getActiveNetworkInfo();
        if(networkInfo != null){



        if(tA.getStatus() == true){

            Log.d(LOG_FLAG, "CHECK OUT STARTED");

            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            long savedEnd = prefs.getLong(getString(R.string.end_time_key), Long.parseLong(getString(R.string.end_time_default)));

            if(savedEnd < 1){


            long dateInMillis = new Date().getTime();
                tA.currentWorkTime.setEndTime(dateInMillis);
            }
            else{
                long dateInMillis = savedEnd;
                tA.currentWorkTime.setEndTime(dateInMillis);
            }


            AsyncCheckOut checkOut = new AsyncCheckOut();
            checkOut.SERVER = tA.getSERVER_URL();
            checkOut.listener = this;
            checkOut.execute(tA.currentWorkTime);

        }
        else{
            Log.d(LOG_FLAG, "CHECK OUT FAIL");
        }

        }
        else{
            long dateInMillis = new Date().getTime();
           prefs.edit().putLong(getString(R.string.end_time_key),dateInMillis);
            prefs.edit().commit();
        }


    }


    public void fetchUser(){

        AsyncLogin login = new AsyncLogin();
        login.SERVER = tA.getSERVER_URL();
        login.listener = this;

        String lastLoginKey = prefs.getString(getString(R.string.pref_login_last), getString(R.string.pref_login_default));



        login.execute(lastLoginKey);

    }


      public void getDetailsForList(){

          SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
           String listSize = sharedPrefs.getString(getString(R.string.pref_list_key), getString(R.string.pref_list_default));
          String empId = Integer.toString(tA.getLoginId());
             AsyncListGrabber listGrabber = new AsyncListGrabber();
          listGrabber.SERVER = tA.getSERVER_URL();
          listGrabber.listener = this;
          int MAX_LIST_SIZE = 25;
          if(Integer.parseInt(listSize) > MAX_LIST_SIZE){
              listSize = Integer.toString(MAX_LIST_SIZE);
          }
          listGrabber.execute(empId, listSize);


      }



}
