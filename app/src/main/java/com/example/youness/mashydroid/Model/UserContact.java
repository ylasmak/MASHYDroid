package com.example.youness.mashydroid.Model;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by youness on 11/01/2017.
 */

public class UserContact {

    public String Login;
    public boolean ActiveTracking;
    public  double[] Location;

    public Marker mMarcker;

    public  UserContact(String login,boolean activeTracking)
    {
        this.Login = login;
        this.ActiveTracking = activeTracking;
    }
}
