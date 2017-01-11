package com.example.youness.mashydroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.youness.mashydroid.Business.UsersAdapter;
import com.example.youness.mashydroid.Model.UserContact;

import java.util.ArrayList;


public class ContactList extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




         View v = inflater.inflate(R.layout.activity_contact_list, container, false);

        ArrayList<UserContact> arrayOfUsers = new ArrayList<UserContact>();
        arrayOfUsers.add(new UserContact("mlasmak@gmail.com",false));
        arrayOfUsers.add(new UserContact("mlasmak@gmail.com",true));
        arrayOfUsers.add(new UserContact("mlasmak@gmail.com",false));

        UsersAdapter adapter = new UsersAdapter(this.getContext(), arrayOfUsers);

        ListView listView = (ListView) v.findViewById(R.id.listViewContact);
        listView.setAdapter(adapter);

        return  v;
    }

}
