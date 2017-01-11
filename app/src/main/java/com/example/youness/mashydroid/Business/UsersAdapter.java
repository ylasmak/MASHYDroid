package com.example.youness.mashydroid.Business;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.youness.mashydroid.Model.UserContact;
import com.example.youness.mashydroid.R;

import java.util.ArrayList;

/**
 * Created by youness on 11/01/2017.
 */
public class UsersAdapter extends ArrayAdapter<UserContact> {
    public UsersAdapter(Context context, ArrayList<UserContact> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        UserContact user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        Button tvLogin = (Button) convertView.findViewById(R.id.tvLogin);
        ToggleButton tvActive = (ToggleButton) convertView.findViewById(R.id.tvActive);
        // Populate the data into the template view using the data object
        tvLogin.setText(user.Login);
        tvActive.setChecked(user.ActiveTracking);
        // Return the completed view to render on screen
        return convertView;
    }
}
