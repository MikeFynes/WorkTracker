package com.lividpansy.worktracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mike on 22/02/2015.
 */
public class DetailFragment extends Fragment {
     String LOG_FLAG = DetailFragment.class.getSimpleName();
    int timePos;

    public DetailFragment (){

    }

    View myView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        myView = inflater.inflate(R.layout.fragment_detail, container, false);


        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
             timePos = Integer.parseInt(intent.getStringExtra(Intent.EXTRA_TEXT));



        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String dateFormat = prefs.getString(getString(R.string.pref_date_key), getString(R.string.pref_date_default));




        TextView dateTv = (TextView) myView.findViewById(R.id.det_date);
        TextView startTv = (TextView) myView.findViewById(R.id.det_start);
        TextView endTv = (TextView) myView.findViewById(R.id.det_end);
        TextView totalTv = (TextView) myView.findViewById(R.id.det_total);


        SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        sdf1.setTimeZone(TimeZone.getDefault());
        String date = sdf1.format(((DetailActivity) getActivity()).tA.getLastSevenDays().get(timePos).getEndTime());

        dateTv.setText(date);

        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        sdf2.setTimeZone(TimeZone.getDefault());
        String startTime = sdf2.format(((DetailActivity) getActivity()).tA.getLastSevenDays().get(timePos).getStartTime());

        startTv.setText(startTime);

        SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        sdf3.setTimeZone(TimeZone.getDefault());
        String endTime = sdf3.format(((DetailActivity) getActivity()).tA.getLastSevenDays().get(timePos).getEndTime());

        endTv.setText(endTime);



        totalTv.setText(totalTimeText());


        getActivity().setTitle(date);

        return myView;
    }


    public String totalTimeText(){

            long endTime = ((DetailActivity) getActivity()).tA.getLastSevenDays().get(timePos).getEndTime();
            long startTime = ((DetailActivity) getActivity()).tA.getLastSevenDays().get(timePos).getStartTime();

        long timeLong = endTime - startTime;

                String time = "";

        int finalSecs = safeLongToInt(timeLong);
        int hrs = (int) finalSecs/ (1000*60*60) % 24;
        if(hrs < 10)
            time += "0" + hrs +":";
        else
            time += hrs +":";

        int mins = (int) ((finalSecs / (1000*60)) % 60);
        if(mins < 10)
            time += "0" + mins+":";
        else
            time += mins+":";

        int secs = (int) (finalSecs / 1000) % 60;
        if(secs < 10)
            time += "0" + secs;
        else
            time += secs;







        return time;
    }


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }



}


