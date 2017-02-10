package com.example.youness.mashydroid.Model;

/**
 * Created by youness on 10/02/2017.
 */

public class UserPhoneNumber {
    public String PhoneNumber;
    public String FullName;

    public UserPhoneNumber(String fullName,String phoneNumber)
    {
        this.PhoneNumber = phoneNumber;
        this.FullName= fullName;
    }
}
