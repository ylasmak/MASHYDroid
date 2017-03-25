package com.example.youness.mashydroid;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.youness.mashydroid.Business.ContactAdapter;
import com.example.youness.mashydroid.Business.UserContext;
import com.example.youness.mashydroid.Model.UserContact;
import com.example.youness.mashydroid.Model.UserPhoneNumber;

import java.io.Console;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.R.attr.data;


public class PhoneContact extends Fragment{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_phone_contact, container, false);
        try {


            ArrayList<UserPhoneNumber> arrayOfUsers = UserContext.CurrentInstance().getListContact();

            ContactAdapter adapter = new ContactAdapter(this.getContext(), arrayOfUsers);
            ListView listView = (ListView) v.findViewById(R.id.listContact);
            listView.setAdapter(adapter);


        }catch (Exception ex)
        {

        }

        return v;
    }






}
