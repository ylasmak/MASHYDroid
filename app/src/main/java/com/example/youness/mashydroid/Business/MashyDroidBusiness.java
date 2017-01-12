package com.example.youness.mashydroid.Business;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.youness.mashydroid.HomeActivity;

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

    public  boolean MashyProcessing(String login,String password) throws UnsupportedEncodingException
    {



       new SendPostRequest("").execute("http://localhost;8081/conexion");

      return true;

    }

    public class SendPostRequest extends AsyncTask<String, String, String> {

        protected void onPreExecute(){}

        String value;
        public SendPostRequest(String value) {
            this.value = value;
        }

        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                String urlParameters = strings[1];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlParameters.getBytes().length));
                urlConnection.setRequestProperty("Content-Language", "en-US");

                urlConnection.setUseCaches (false);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(
                        urlConnection.getOutputStream ());
                wr.writeBytes (urlParameters);
                wr.flush ();
                wr.close ();


                //Get Response
                InputStream is = urlConnection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                return response.toString();


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return  null;
        }



        @Override
        protected void onPostExecute(String result) {


        }
    }
}
