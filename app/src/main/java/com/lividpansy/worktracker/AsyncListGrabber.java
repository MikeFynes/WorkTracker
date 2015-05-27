package com.lividpansy.worktracker;

import android.os.AsyncTask;
import android.util.Log;

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
 * Created by mike on 22/02/2015.
 */
public class AsyncListGrabber extends AsyncTask<String, Integer, ArrayList<WorkTime>> {
    String LOG_FLAG = AsyncListGrabber.class.getSimpleName();

    public static String SERVER;
    AsyncListener listener;

    ArrayList<WorkTime> listTime;

    @Override
    protected ArrayList<WorkTime> doInBackground(String... params) {

        String method = "checkUser";
        postData(method, params[0], params[1]);
        if(listTime != null){
            return listTime;
        }
        else{
            return null;
        }

    }
    public void postData(String valueIWantToSend, String id, String listSize) {
           Log.d(LOG_FLAG, "LIST GRAB STARTED");
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(SERVER);



        try {




            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method",
                    valueIWantToSend));

            nameValuePairs.add(new BasicNameValuePair("id", id));






            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);


            if (response.containsHeader("backEnd")) {


                int size = Integer.parseInt(response.getFirstHeader("size").getValue());

                if (size != 0){




                int parserSize = Integer.parseInt(listSize);
if(parserSize>size){
    parserSize = size;
}



                    try {

                        listTime = new ArrayList<WorkTime>();
                        for (int i = 0; i < parserSize; i++) {
                            long wId = Long.parseLong(response.getFirstHeader("id" + Integer.toString(i)).getValue());
                            int tracker = Integer.parseInt(response.getFirstHeader("trackingType" + Integer.toString(i)).getValue());
                            long sTime = Long.parseLong(response.getFirstHeader("start" + Integer.toString(i)).getValue());
                            long eTime = Long.parseLong(response.getFirstHeader("end" + Integer.toString(i)).getValue());
                            WorkTime item = new WorkTime(wId, Integer.parseInt(id), tracker, sTime, eTime);

                            listTime.add(item);


                        }
                    }
                        catch(NullPointerException e){

                        }

                    }













            } else {

                    listTime = new ArrayList<WorkTime>();
            }



        }catch(ClientProtocolException e){



        }catch(IOException e){


        }




    }




    protected void onPostExecute(ArrayList<WorkTime> result) {

        super.onPostExecute(result);


        if(result != null){
            listener.sendList(result);

        }
        else{
            listener.listIsEmpty();

        }

    }



}
