package com.example.youness.mashydroid.Business;

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
                result = "00"+result.substring(1,result.length());

            }
            if(result.startsWith("0") && !result.startsWith("00"))
            {
                result = "00"+ UserContext.CurrentInstance().CountryCallingCode +result.substring(1,result.length());

            }


            return result;
        }
        else
        {
            return null;
        }
    }
}
