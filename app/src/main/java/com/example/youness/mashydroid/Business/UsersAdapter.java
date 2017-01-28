package com.example.youness.mashydroid.Business;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.youness.mashydroid.Model.UserContact;
import com.example.youness.mashydroid.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

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
        tvLogin.setText(user.Login.toLowerCase());
        tvActive.setChecked(user.ActiveTracking);

        tvLogin.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              //  ***Do what you want with the click here***

                UserContext.CurrentInstance().LoginToFlow = ((Button)v).getText().toString();
                UserContext.CurrentInstance().mMap.clear();

                ViewPager pager = (ViewPager) LookingForParent(v);
                if(pager != null)
                pager.setCurrentItem(1);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    private ViewParent  LookingForParent(View value)
    {
        try
        {
            ViewParent parent = value.getParent();
            if(parent == null)
            {
                return  null;
            }

            if(parent instanceof ViewPager)
            {
                return  parent;
            }
            else
            {
                return LookingForParent((View) parent);
            }


        }
        catch (Exception e )
        {
            return  null;
        }
    }
}
