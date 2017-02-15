package com.example.youness.mashydroid.Business;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.youness.mashydroid.ConfirmationMessage;
import com.example.youness.mashydroid.Model.UserContact;
import com.example.youness.mashydroid.Model.UserPhoneNumber;
import com.example.youness.mashydroid.R;

import java.util.ArrayList;

/**
 * Created by youness on 10/02/2017.
 */

public class ContactAdapter extends ArrayAdapter<UserPhoneNumber> {
    public ContactAdapter(Context context, ArrayList<UserPhoneNumber> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserPhoneNumber user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contacts_list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvFullName = (TextView) convertView.findViewById(R.id.FullName);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.Number);
        LinearLayout tvLayout = (LinearLayout) convertView.findViewById(R.id.Layout);


        // Populate the data into the template view using the data object
        tvFullName.setText(user.FullName);
        tvNumber.setText(user.PhoneNumber);
        tvLayout.setTag(user.FullName +" " +user.PhoneNumber);

        tvLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LinearLayout layout = (LinearLayout)v;
                layout.getChildCount();
                TextView tvNumber = (TextView) v.findViewById(R.id.Number);


                String message =getContext().getString( R.string.Send_invitation);
                message = message +layout.getTag().toString();
                ConfirmationMessage.newInstance(message,tvNumber.getText().toString()).show(((Activity)getContext()).getFragmentManager(),"Notify");


            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
