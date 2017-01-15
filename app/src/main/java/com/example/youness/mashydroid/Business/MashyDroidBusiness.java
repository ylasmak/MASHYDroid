package com.example.youness.mashydroid.Business;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.youness.mashydroid.HomeActivity;
import com.example.youness.mashydroid.Model.UserContact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by youness on 03/01/2017.
 */

public class MashyDroidBusiness {

    public  boolean MashyProcessing(Activity app)
    {
        new SendPostRequest(app).execute( );
        GPSTracker gps = new GPSTracker(app);
        double lat =gps.getLatitude();
        double log = gps.getLongitude();
      return true;

    }

    public class SendPostRequest extends AsyncTask<Void, Void, Void> {

            Activity mContext;

            SendPostRequest(Activity app) {
            this.mContext = app;
        }

        protected Void doInBackground(Void... strings) {

            while(true) {
                try {
                    URL url = new URL(UserContext.CurrentInstance().ServerUrl.concat("lookup"));

                    String urlParameters = GetParameter();
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    urlConnection.setRequestProperty("Content-Length", "" +
                            Integer.toString(urlParameters.length()));
                    urlConnection.setRequestProperty("Content-Language", "en-US");

                    urlConnection.setUseCaches(false);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    //Send request
                    DataOutputStream wr = new DataOutputStream(
                            urlConnection.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();


                    //Get Response
                    InputStream is = urlConnection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    String result = response.toString();

                    JSONObject topLevel = new JSONObject(result);
                    int sucess = topLevel.getInt("sucess");
                    if (sucess > 0) {


                        JSONArray cercle = topLevel.getJSONObject("message").getJSONArray("cercle");

                        if (cercle != null && cercle.length() > 0) {
                            int cpt = cercle.length();
                            for (int i = 0; i < cpt; i++) {
                                String tmpLogin = cercle.getJSONObject(i).getString("Login");
                                boolean tmpActive = cercle.getJSONObject(i).getBoolean("ActiveTracking");

                                UserContext.CurrentInstance().GetContactList().add(new UserContact(tmpLogin, tmpActive));
                            }
                        }



                    }


                } catch (java.io.IOException | JSONException e) {
                    e.printStackTrace();
                }


                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }




        }


        private String GetParameter() throws UnsupportedEncodingException, JSONException {

            StringBuilder result = new StringBuilder();
            result.append(URLEncoder.encode("Parmeters", "UTF-8"));
            result.append("=");


            JSONObject obj = new JSONObject();
            obj.put("Login", UserContext.CurrentInstance().Login);
            if(UserContext.CurrentInstance().Location != null) {
                JSONArray topLevel = new JSONArray(UserContext.CurrentInstance().Location);
                obj.put("Location", topLevel);
            }

            result.append(URLEncoder.encode(obj.toString(), "UTF-8"));

            return result.toString();

        }

        protected void onPostExecute(final Boolean success) {

        }
    }
}