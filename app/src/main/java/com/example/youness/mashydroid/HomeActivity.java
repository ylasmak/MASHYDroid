package com.example.youness.mashydroid;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;

import com.example.youness.mashydroid.Business.MashyDroidBusiness;
import com.example.youness.mashydroid.Business.PhoneNumberHelper;
import com.example.youness.mashydroid.Business.ServiceProvider;
import com.example.youness.mashydroid.Business.UserContext;
import com.example.youness.mashydroid.Model.UserPhoneNumber;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements ConfirmationMessage.NoticeDialogListener,
        ServiceProvider.ServiceProviderCallBack {

    private  String phoneNumber;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Contact List"));
        tabLayout.addTab(tabLayout.newTab().setText("Contact Map"));
        tabLayout.addTab(tabLayout.newTab().setText("Contact Phone"));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

         new MashyDroidBusiness().MashyProcessing(this);


        loadContactList();

    }

    public void loadContactList()
    {
        ArrayList<UserPhoneNumber> arrayOfUsers = new ArrayList<UserPhoneNumber>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        ArrayList<String> tmp = new ArrayList<String>();

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                tmp.clear();

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

                       if(!tmp.contains(PhoneNumberHelper.GetFullPhoneNumber( phoneNo))) {
                           arrayOfUsers.add(new UserPhoneNumber(name, phoneNo));
                           tmp.add(PhoneNumberHelper.GetFullPhoneNumber( phoneNo));
                       }
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

        UserContext.CurrentInstance().getListContact().addAll(arrayOfUsers);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       // if (id == R.id.action_settings) {
            return true;
      //  }

       // return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

        String phoneNumber = ((ConfirmationMessage)dialog).mPhoneNumber;
        phoneNumber =PhoneNumberHelper.GetFullPhoneNumber(phoneNumber);

        String MyPhoneNumber  = UserContext.CurrentInstance().getFullPhoneNumber();

        HashMap<String,String> params = new HashMap<String,String>();

        params.put("OriginRequestPhoneNumber",phoneNumber);
        params.put("InvitationPhoneNunber",MyPhoneNumber);

        this.phoneNumber = phoneNumber;

        ServiceProvider service = new ServiceProvider("POST","requestInvitation",params,this);
        service.execute();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
    }

    public void onEndServiceResult(JSONObject result, String action)
    {
       try
       {
           if(action.equals("requestInvitation"))
           {
               int sucess = result.getInt("sucess");
               if(sucess==1)
               {
                   ShouMessage(String.format(getString(R.string.invitation_confirmation), this.phoneNumber));
               }
               if(sucess ==0)
               {
                   // Internal error
                   ShouMessage(getString(R.string.invitation_error));
               }
               if(sucess == -1)
               {
                   //user alredy invited
                   ShouMessage(String.format(getString(R.string.invitation_exist), this.phoneNumber));
               }
               if(sucess == -2)
               {
                   //user not exist in plateforme
                   ShouConfirmation(String.format(getString(R.string.contact_not_exist_in_system), this.phoneNumber));
               }

           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }


    private  void ShouMessage(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
         builder.create().show();
    }


    private  void ShouConfirmation(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    Context baseContext   =((ContextThemeWrapper)(((AlertDialog) dialog).getContext())).getBaseContext();
                        ((HomeActivity)baseContext).sendSMS();
                    }
                }) .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private  void sendSMS()
    {
        String message = getString(R.string.Send_sms_invitation);
        PhoneNumberHelper.SendSMS(this.phoneNumber,message);
    }


}

