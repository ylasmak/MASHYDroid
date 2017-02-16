package com.example.youness.mashydroid;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.youness.mashydroid.Business.MashyDroidBusiness;
import com.example.youness.mashydroid.Business.PhoneNumberHelper;
import com.example.youness.mashydroid.Business.ServiceProvider;
import com.example.youness.mashydroid.Business.UserContext;

import org.json.JSONObject;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements ConfirmationMessage.NoticeDialogListener,
        ServiceProvider.ServiceProviderCallBack {

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

        // new MashyDroidBusiness().MashyProcessing(this);

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
                   ShouwMessage("OK");
               }
               if(sucess ==0)
               {
                   // Internal error
                   ShouwMessage("OK");
               }
               if(sucess == -1)
               {
                   //user alredy invited
                   ShouwMessage("OK");
               }
               if(sucess == -2)
               {
                   //user not exist in plateforme
                   ShouwMessage("OK");
               }

           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

    }


    private  void ShouwMessage(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
         builder.create().show();
    }
}
