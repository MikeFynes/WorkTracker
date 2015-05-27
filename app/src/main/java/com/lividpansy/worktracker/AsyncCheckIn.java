package com.lividpansy.worktracker;

/**
 * Created by mike on 14/02/2015.
 */
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by mike on 03/02/2015.
 */
public class AsyncCheckIn extends AsyncTask<WorkTime, Integer, String> {
    public static String SERVER;


    AsyncListener listener;
    String wId;

    @Override
    protected String doInBackground(WorkTime... params) {

        String method = "checkIn";

        postData(method, params[0]);
        String result = getwId();
        return result;
    }

    public void postData(String valueIWantToSend, WorkTime workTime) {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(SERVER);



        try {




            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method",
                    valueIWantToSend));

            nameValuePairs.add(new BasicNameValuePair("empId", Integer.toString(workTime.getEmployee())));
            nameValuePairs.add(new BasicNameValuePair("tracker", Integer.toString(workTime.getTrackingType())));

            nameValuePairs.add(new BasicNameValuePair("sTime", Long.toString(workTime.getStartTime())));




            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);


            if (response.containsHeader("backEnd")) {

                setwId(response.getFirstHeader("wId").getValue());

            } else {

                setwId("1");
            }



        }catch(ClientProtocolException e){


        }catch(IOException e){


        }




    }




    protected void onPostExecute(String result) {

        super.onPostExecute(result);
        listener.workTimeId(Long.parseLong(result));
    }


    public String getwId() {
        return wId;
    }

    public void setwId(String wId) {
        this.wId = wId;
    }
}

