package com.example.youness.mashydroid.Business;

import com.example.youness.mashydroid.Model.UserContact;

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

    public  String Login;
    public  String Password;

    private  ArrayList<UserContact> _contactList;
    public String ServerUrl;

    public  ArrayList<UserContact> GetContactList()
    {
        if(_contactList == null)_contactList = new ArrayList<UserContact>();

        return _contactList;
    }

    public double[] Location;
    boolean ActiveTracking;


}
