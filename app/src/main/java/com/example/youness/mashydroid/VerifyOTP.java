package com.example.youness.mashydroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.youness.mashydroid.Business.UserContext;
import com.example.youness.mashydroid.Model.UserContact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class VerifyOTP extends AppCompatActivity {

    private EditText mCode;
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCode = (EditText) findViewById(R.id.Code);
        Button mEmailSignInButton = (Button) findViewById(R.id.verify_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptVerify(mCode.getText().toString());
            }
        });
    }

    private void attemptVerify(String code) {
        if (mAuthTask != null) {
            return;
        }

        Intent intent = new Intent(this, HomeActivity.class);
        //startActivity(intent);
        if( UserContext.CurrentInstance().PhoneNumber != null) {
            //  showProgress(true);
            mAuthTask = new UserLoginTask(intent,code);
            mAuthTask.execute();
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {


        private  final Intent mintent;
        private  final  String  otpCode;

        UserLoginTask(  Intent intent,String code) {
            mintent = intent;
            otpCode = code;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                URL  url = new URL(UserContext.CurrentInstance().ServerUrl.concat("verify"));

                String urlParameters = GetParameter();
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
                String result = response.toString();

                JSONObject topLevel = new JSONObject(result);
                int sucess = topLevel.getInt("sucess");
                if(sucess > 0) {

                    UserContext.CurrentInstance().Auth = true;
                    JSONArray cercle = topLevel.getJSONObject("message").getJSONArray("cercle");

                    if(cercle != null && cercle.length() > 0)
                    {
                        int cpt = cercle.length();
                        for(int i =0;i<cpt;i++)
                        {
                            String tmpLogin = cercle.getJSONObject(i).getString("Login");
                            boolean tmpActive = cercle.getJSONObject(i).getBoolean("ActiveTracking");

                            UserContext.CurrentInstance().GetContactList().add(new UserContact(tmpLogin,tmpActive));
                        }
                    }

                    return true;
                }
                else return false;
            } catch (java.io.IOException | JSONException e) {
                e.printStackTrace();
            }


            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            //  showProgress(false);

            if (success) {
                //Intent intent = new Intent(this, HomeActivity.class);
                SavePhoneNumber(UserContext.CurrentInstance().PhoneNumber);
                startActivity(mintent);
                //finish();
            }
        }

        @Override
        protected void onCancelled() {

            // showProgress(false);
        }

        private String GetParameter()throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();

            result.append(URLEncoder.encode("PhoneNumber", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(UserContext.CurrentInstance().PhoneNumber, "UTF-8"));

            result.append("&");
            result.append(URLEncoder.encode("OTPCode", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(otpCode, "UTF-8"));



            return result.toString();
        }
    }


    //saving phone number in application storage

    private void SavePhoneNumber(String phoneNumber)
    {
        try {
            String FILENAME = "MashyStorage";

            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(phoneNumber.getBytes());
            fos.close();
        }catch ( IOException e)
        {
            e.printStackTrace();
        }


    }

}
