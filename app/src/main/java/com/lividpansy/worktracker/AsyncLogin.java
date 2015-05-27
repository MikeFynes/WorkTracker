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
 * Created by mike on 15/02/2015.
 */
public class AsyncLogin extends AsyncTask<String, Integer, String> {
    String LOG_FLAG = AsyncLogin.class.getSimpleName();
    public static String SERVER;
    AsyncListener listener;

    String wifi;

    @Override
    protected String doInBackground(String... params) {
        String method = "login";
        String result = postData(method, params[0]);


        return result;
    }

    public String postData(String valueIWantToSend, String code) {
        code = code.trim();
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(SERVER
                //"http://10.0.2.2:8084/Servlet/Servlet"
                //  "http://tc.dev.mw.metropolia.fi/Servlet/Servlet"

        );
        Log.d(LOG_FLAG, "I CREATED A POST");


        try {




            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("method",
                    valueIWantToSend));

            nameValuePairs.add(new BasicNameValuePair("code", code));



            Log.d(LOG_FLAG, valueIWantToSend);

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            Log.d(LOG_FLAG, "SENT THE POST");

            if (response.containsHeader("backEnd")) {
                Log.d(LOG_FLAG, response.getFirstHeader("backEnd").getValue());

                try{
                    Log.d(LOG_FLAG, response.getFirstHeader("wifi").getValue());
                }
                catch (NullPointerException e){
                    Log.d(LOG_FLAG, "WIFI IS NULL "+ e.toString());

                }


                try{
                    String empId = response.getFirstHeader("empId").getValue();
                    setWifi(response.getFirstHeader("wifi").getValue());





                    return empId;
                }
                catch (NullPointerException e){
                    Log.d(LOG_FLAG, e.toString());
                    String empId = null;

                    return empId;
                }


            } else {


            }



        }catch(ClientProtocolException e){


        }catch(IOException e){


        }



        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result != null){
            int id = Integer.parseInt(result);
            listener.loginSuccess(id, getWifi());


        }
        else{
            listener.loginFail();

        }


    }


    public String getWifi() {
        return wifi;
    }

    public void setWifi(String wifi) {
        this.wifi = wifi;
    }
}
