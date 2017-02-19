package com.example.youness.mashydroid.Business;

import com.example.youness.mashydroid.Model.UserContact;
import com.example.youness.mashydroid.Model.UserPhoneNumber;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

/**
 * Created by youness on 11/01/2017.
 */

public class UserContext {

    private  static UserContext _instance;

    private UserContext(){}

    public static UserContext CurrentInstance()
    {
        if(_instance == null)
        {
            _instance = new UserContext();
        }
        return  _instance;
    }

    public  static  void Dispose()
    {
        _instance = null;
    }
    public  String Login;
    public  String Password;
    public String LoginToFlow;

    private  ArrayList<UserContact> _contactList;
    public String ServerUrl;

    public  ArrayList<UserContact> GetContactList()
    {
        if(_contactList == null)_contactList = new ArrayList<UserContact>();

        return _contactList;
    }


    public  String getFullPhoneNumber()
    {
        //return "+212655994768";
       if(CountryCallingCode == null)
       {
           return null ;
       }
        if(PhoneNumber.startsWith("+"))
        {
            return PhoneNumber;
        }
        String result="" ;
        if(CountryCallingCode != null) {
            String CountryCode = CountryCallingCode.trim();
        }
        if(CountryCallingCode.startsWith("00")) {
            CountryCallingCode = CountryCallingCode.substring(2,CountryCallingCode.length());
            CountryCallingCode = "+"+CountryCallingCode;
        }

        if(!CountryCallingCode.startsWith("+")){
            CountryCallingCode = "+"+CountryCallingCode;
        }

        String phoneNumber = PhoneNumber.trim();
        if(phoneNumber.startsWith("0"))
        {
            phoneNumber = phoneNumber.substring(1,phoneNumber.length());
        }

        result = CountryCallingCode+phoneNumber;


        return  result;

    }

    ArrayList<UserPhoneNumber> _arrayOfUsers ;

    public ArrayList<UserPhoneNumber> getListContact()
    {
        if(_arrayOfUsers == null)
        {
            _arrayOfUsers = new ArrayList<UserPhoneNumber>();
        }
        return _arrayOfUsers;
    }

    public double[] Location;
    boolean ActiveTracking;

    public GoogleMap mMap;
    public  String PhoneNumber;
    public  String PhoneSerialNumber;
    public  String CountryCode;
    public  String CountryCallingCode;
    public  boolean Auth;

}
