package com.example.youness.mashydroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.example.youness.mashydroid.Business.UserContext;
import com.example.youness.mashydroid.Business.UsersAdapter;
import com.example.youness.mashydroid.Model.UserContact;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        try {
            View v = inflater.inflate(R.layout.activity_maps, container, false);

            SupportMapFragment mapFragment = (SupportMapFragment)getFragmentManager().getFragments().get(1).getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            return  v;
        }
        catch (Exception ex)
        {
            Log.d("Erreur",ex.getMessage());
            return  null;
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng tmp = new LatLng(33.923439, -6.939421 );
        //mMap.addMarker(new MarkerOptions().position(tmp).title("Macha Allah"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tmp,15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        UserContext.CurrentInstance().mMap = mMap;



    }
}
