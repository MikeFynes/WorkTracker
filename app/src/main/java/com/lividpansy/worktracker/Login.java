package com.lividpansy.worktracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by mike on 15/02/2015.
 */
public class Login extends Fragment implements View.OnClickListener {
    String LOG_FLAG = Login.class.getSimpleName();
EditText et;
Button submit;
    TrackerApp tA;
    View rootView;

    public Login (){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_login, container, false);
        tA = (TrackerApp) getActivity().getApplicationContext();


        et = (EditText) rootView.findViewById(R.id.code_input);
        submit = (Button) rootView.findViewById(R.id.login_submit);
        submit.setOnClickListener(this);



        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_submit:
                String code = et.getText().toString();
                if(code != "") {
                    tA.setBackgroundService(false);
                    tA.setLoginCode(et.getText().toString());
                    SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(getActivity());
                    prefs.edit().putString(getString(R.string.pref_login_last), et.getText().toString()).commit();
                }
                else{
                    Toast.makeText(getActivity(), "You must enter a code!", Toast.LENGTH_SHORT).show();
                }
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        tA.setLoginStatus(false);
        tA.setBackgroundService(false);

    }
}
