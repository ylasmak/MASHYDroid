package com.example.youness.mashydroid.Business;

import android.telephony.SmsManager;

/**
 * Created by youness on 15/02/2017.
 */

public class PhoneNumberHelper {

    public  static  String GetFullPhoneNumber(String phoneNumber)
    {
        if(phoneNumber!= null)
        {
            String result=phoneNumber;

            result= result.replace("(","");
            result= result.replace(")","");
            result= result.replace("-","");
            result= result.replace(" ","");

            result = result.trim();

            if(result.startsWith("+"))
            {
                result = "+"+result.substring(1,result.length());

            }
            if(result.startsWith("0") && !result.startsWith("00"))
            {
                result = "+"+ UserContext.CurrentInstance().CountryCallingCode +result.substring(1,result.length());

            }


            return result;
        }
        else
        {
            return null;
        }

    }

    public  static void SendSMS(String PhoneNumber,String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(PhoneNumber, null, message, null, null);
    }


}
