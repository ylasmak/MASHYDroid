package com.example.youness.mashydroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.example.youness.mashydroid.Business.UserContext;
import com.example.youness.mashydroid.Model.UserContact;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPphoneNumber;
    private EditText mSerialText;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.


        mPphoneNumber = (EditText) findViewById(R.id.phoneNumber);
        mSerialText = (EditText) findViewById(R.id.serialNumber);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            UserContext.CurrentInstance().ServerUrl = bundle.getString("mashy_server_url");

            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_PHONE_STATE);
            final int REQUEST_CODE_READ_PHONE_STATE = 123;
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_READ_PHONE_STATE); // define this constant yourself
            }

            TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
            UserContext.CurrentInstance().PhoneNumber = GetPhoneNumber();
            UserContext.CurrentInstance().PhoneSerialNumber = tMgr.getSimSerialNumber();
            attemptLogin();

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TAG", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("TAG", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        mPphoneNumber.setText(UserContext.CurrentInstance().PhoneNumber);
        mSerialText.setText(UserContext.CurrentInstance().PhoneSerialNumber);

        mSerialText.setInputType(InputType.TYPE_NULL);
        mSerialText.setTextIsSelectable(true);
        mSerialText.setKeyListener(null);


        mPphoneNumber.requestFocus();
       // mPasswordView.setText("password");
    }

    // Getting phone number from file storage
    private String GetPhoneNumber()   {

        String line,line1 = "";
        try {
            String FILENAME = "MashyStorage";
            File file = new File(FILENAME);
            FileInputStream fis = openFileInput(FILENAME);

                if (fis != null) {
                    InputStreamReader inputreader = new InputStreamReader(fis);
                    BufferedReader buffreader = new BufferedReader(inputreader);


                    while ((line = buffreader.readLine()) != null)
                        line1 += line;
                    fis.close();
                }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return  line1;

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

            Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
            //startActivity(intent);
           /* if( UserContext.CurrentInstance().PhoneNumber != null) {
              //  showProgress(true);
                mAuthTask = new UserLoginTask("Login",intent);
                mAuthTask.execute();
            }*/
    }

    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        Intent intent = new Intent(this, VerifyOTP.class);
        UserContext.CurrentInstance().Login = mEmailView.getText().toString();
        UserContext.CurrentInstance().PhoneNumber = mPphoneNumber.getText().toString();

        //startActivity(intent);
        if( UserContext.CurrentInstance().PhoneNumber != null) {
           if(!isEmailValid ( UserContext.CurrentInstance().Login)) {
               mEmailView.setError(getString(R.string.error_invalid_email));
               mEmailView.requestFocus();
           }
            else {
               showProgress(true);
               mAuthTask = new UserLoginTask("Register", intent);
               mAuthTask.execute();
           }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private  final Intent mintent;
        private  final String mAction;

        UserLoginTask(String action,  Intent intent) {

            mintent = intent;
            mAction = action;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                URL url = new URL(UserContext.CurrentInstance().ServerUrl.concat("connexion"));
                if(mAction.equals("Login")) {
                     url = new URL(UserContext.CurrentInstance().ServerUrl.concat("connexion"));
                }
                if(mAction.equals("Register"))
                {
                    url = new URL(UserContext.CurrentInstance().ServerUrl.concat("register"));
                }



               // UserContext.CurrentInstance().Dispose();

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
            mAuthTask = null;
          //  showProgress(false);

            if (success) {
                    startActivity(mintent);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
           // showProgress(false);
        }

        private String GetParameter()throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();

            result.append(URLEncoder.encode("PhoneNumber", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(UserContext.CurrentInstance().PhoneNumber, "UTF-8"));

            result.append("&");
            result.append(URLEncoder.encode("SerialNumber", "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(UserContext.CurrentInstance().PhoneSerialNumber, "UTF-8"));

            if(mAction.equals("Register"))
            {
                result.append("&");
                result.append(URLEncoder.encode("Email", "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(UserContext.CurrentInstance().Login, "UTF-8"));


            }


            return result.toString();
        }
    }
}

