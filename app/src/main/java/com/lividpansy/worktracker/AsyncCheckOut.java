package com.lividpansy.worktracker;

/**
 * Created by mike on 14/02/2015.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 04/02/2015.
 */
public class AsyncCheckOut extends AsyncTask<WorkTime, Integer, Void> {
    AsyncListener listener;
    String SERVER;

    @Override
    protected Void doInBackground(WorkTime... params) {

        String method = "checkOut";

        postData(method, params[0]);


        return null;
    }

    public void postData(String valueIWantToSend, WorkTime workTime) {
        String wId = "";
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(SERVER);



        try {




            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method",
                    valueIWantToSend));

            nameValuePairs.add(new BasicNameValuePair("wId", Long.toString(workTime.getId())));

            nameValuePairs.add(new BasicNameValuePair("eTime", Long.toString(workTime.getEndTime())));




            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


            httpclient.execute(httppost);

        } catch(IOException e){
            Log.d("CHECKOUT ERROR ", e.toString());

        }




    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.checkOutSuccess();
    }
}