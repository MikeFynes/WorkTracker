package com.lividpansy.worktracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity implements StatusSender{
    TrackerApp tA;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tA = (TrackerApp)getApplicationContext();

        tA.sender = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loginCode = prefs.getString(getString(R.string.pref_login_last), getString(R.string.pref_login_default));
        if(loginCode != "noKey"){
        tA.setLoginCode(loginCode);
            tA.setBackgroundService(false);
        }

            if (savedInstanceState == null && tA.getBackgroundService() != true) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frag_container, new Login())
                        .commit();


            }

        }





    @Override
    public void codeWrong() {
        Toast.makeText(this, "Code entered not recognized!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changedStatus() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        tA.setBackgroundService(false);
    }
}
