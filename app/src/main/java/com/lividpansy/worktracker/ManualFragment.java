package com.lividpansy.worktracker;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mike on 22/02/2015.
 */
public class ManualFragment extends Fragment implements StatusSender, View.OnClickListener {

    ArrayAdapter<String> hoursAdapter;
    String LOG_FLAG = ManualFragment.class.getSimpleName();
    Button btn_check;
    Boolean currentStatus;
    String statusText;
    View rootView;
    TextView status;




    public ManualFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).tA.sender = this;

        ((MainActivity) getActivity()).tA.setLoginStatus(true);

        rootView = inflater.inflate(R.layout.frag_main, container, false);

        btn_check = (Button) rootView.findViewById(R.id.btn_check);
        btn_check.setOnClickListener(this);



        statusText = "";
         status = (TextView) rootView.findViewById(R.id.current_status);




        // INITIATES LIST

        hoursAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_time, // The name of the layout ID.
                        R.id.list_item_time_textview, // The ID of the textview to populate.
                        new ArrayList<String>());


     if(((MainActivity) getActivity()).tA.getLastSevenDays() != null){
         fillList();

     }

        statusCheck();

        return rootView;
    }

public void updateCurrentStatus(){
    currentStatus = ((MainActivity) getActivity()).tA.getStatus();
}

    public void statusCheck() {

        updateCurrentStatus();
        Log.d(LOG_FLAG, "FRAG READS STATUS AS: "+Boolean.toString(currentStatus));
        if (currentStatus == true) {
            statusText = ((MainActivity) getActivity()).getResources().getString(R.string.checked_in);
            btn_check.setText(((MainActivity) getActivity()).getResources().getString(R.string.check_out));
        } else {
            statusText = ((MainActivity) getActivity()).getResources().getString(R.string.checked_out);
            btn_check.setText(((MainActivity) getActivity()).getResources().getString(R.string.check_in));
        }
        status.invalidate();
        status.setText(statusText);
    }




    @Override
    public void codeWrong() {

    }

    @Override
    public void changedStatus() {
        statusCheck();
        if(((MainActivity) getActivity()).tA.getLastSevenDays() != null){
            fillList();

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_check:

                if(currentStatus == true){

                    ((MainActivity) getActivity()).tA.setServiceAction("checkOut");

                }
                else{
                    ((MainActivity) getActivity()).tA.setTracker(3);
                    ((MainActivity) getActivity()).tA.setServiceAction("checkIn");
                    Log.d(LOG_FLAG, "CHECK IN");
                }

                ((MainActivity) getActivity()).tA.sendMethodToService();

                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int newListSize = Integer.parseInt(prefs.getString(getString(R.string.pref_list_key), getString(R.string.pref_list_default)));
        if(((MainActivity) getActivity()).tA.getLastSevenDays() != null) {
            if (((MainActivity) getActivity()).tA.getLastSevenDays().size() != newListSize) {
                ((MainActivity) getActivity()).tA.updateList();
                fillList();
            }

            if (((MainActivity) getActivity()).tA.getLastSevenDays() != null) {
                fillList();

            }
        }


    }


    public void fillList(){
        hoursAdapter.clear();
        ListView listView = (ListView) rootView.findViewById(R.id.listView_time);
        if(((MainActivity) getActivity()).tA.getLastSevenDays().size()>0){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            listView.setVisibility(View.VISIBLE);
        for (int i = 0; i < ((MainActivity) getActivity()).tA.getLastSevenDays().size(); i++) {

            String dateFormat = prefs.getString(getString(R.string.pref_date_key), getString(R.string.pref_date_default));

            SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            String date = sdf1.format(((MainActivity) getActivity()).tA.getLastSevenDays().get(i).getEndTime());

            hoursAdapter.add(date);


        }



        listView.setAdapter(hoursAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String time = Integer.toString(position);


                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, time);
                getActivity().startActivity(intent);

            }

        });

        }
        else{
           listView.setVisibility(View.GONE);
        }

    }
}
