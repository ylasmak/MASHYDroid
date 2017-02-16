package com.example.youness.mashydroid.Business;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by youness on 16/02/2017.
 */

public class ServiceProvider extends AsyncTask<Void, Void, Boolean> {

   private String _method;
   private HashMap<String,String> _params;
   private  String _action;
   private  JSONObject _serviceResult;
   private ServiceProviderCallBack _origin;


    public interface ServiceProviderCallBack {
         void onEndServiceResult(JSONObject result,String action);

    }



    public  ServiceProvider(String method, String action , HashMap<String,String> params, ServiceProviderCallBack origin)
    {
        _method = method;
        _params = params;
        _action = action;
        _origin = origin;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String line;

        try
        {
            URL url = new URL(UserContext.CurrentInstance().ServerUrl.concat(_action));

            String urlParameters = GetParameter();
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(_method);
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

            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();


             _serviceResult = new JSONObject(response.toString());
            return  true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return  false;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if(success)
        {
            try {

              _origin.onEndServiceResult(_serviceResult,_action);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    private  String GetParameter()
    {
        if(_params == null || _params.size() ==0)
        {
            return  null;
        }

        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : _params.entrySet()) {
           if(result.length() > 0)
           {
               result.append("&");
           }
            result.append( entry.getKey());
            result.append("=");
            result.append(entry.getValue());

        }
        return result.toString();
    }
}
