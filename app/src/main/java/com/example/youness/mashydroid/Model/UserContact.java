package com.example.youness.mashydroid.Model;

/**
 * Created by youness on 11/01/2017.
 */

public class UserContact {

    public String Login;
    public boolean ActiveTracking;

    public  UserContact(String login,boolean activeTracking)
    {
        this.Login = login;
        this.ActiveTracking = activeTracking;
    }
}
