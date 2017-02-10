package com.example.youness.mashydroid.Business;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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
        tvFullName.setText(user.FullName.toLowerCase());
        tvNumber.setText(user.PhoneNumber.toLowerCase());

        tvLayout.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LinearLayout layout = (LinearLayout)v;
                layout.getChildCount();

            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
