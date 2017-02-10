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


            ArrayList<UserPhoneNumber> arrayOfUsers = new ArrayList<UserPhoneNumber>();

            ContentResolver cr = getActivity().getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            arrayOfUsers.add(new UserPhoneNumber(name,phoneNo));

                        }

                        pCur.close();
                    }
                }
            }

            Collections.sort(arrayOfUsers, new Comparator<UserPhoneNumber>() {
                @Override
                public int compare(UserPhoneNumber user1, UserPhoneNumber user2)
                {

                    return  user1.FullName.compareTo(user2.FullName);
                }
            });
            ContactAdapter adapter = new ContactAdapter(this.getContext(), arrayOfUsers);
            ListView listView = (ListView) v.findViewById(R.id.listContact);
            listView.setAdapter(adapter);


        }catch (Exception ex)
        {

        }

        return v;
    }






}
