package com.example.youness.mashydroid.Business;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Created by youness on 26/07/2017.
 */



public final class LastKnownLocation {

    private  FusedLocationProviderClient mFusedLocationClient;
    private  final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    private Location  mLastLocation;

    public  double[] GetLastProsition(Activity app)
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(app);

        if ( ContextCompat.checkSelfPermission(
                app, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(app, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }



        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(app, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mLastLocation = location;
                        }
                    }
                })
                .addOnFailureListener(app, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mLastLocation = null;
                    }
                });

        if(mLastLocation != null) {
            double[] Location = new double[]{mLastLocation.getLatitude(), mLastLocation.getLongitude()};

            return Location;
        }
        else
        {
            return  null;
        }

    }
}
