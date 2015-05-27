package com.lividpansy.worktracker;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class DetailActivity extends ActionBarActivity {
    TrackerApp tA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tA = (TrackerApp) getApplicationContext();
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frag_container, new DetailFragment())
                    .commit();


        }


    }




}
