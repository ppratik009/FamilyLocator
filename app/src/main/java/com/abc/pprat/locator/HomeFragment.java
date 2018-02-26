package com.abc.pprat.locator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;


public class HomeFragment extends Fragment implements ChildEventListener {


    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    static boolean Iscamera = false;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference codesReference = firebaseDatabase.getReference("Codes");
    Map<String, String> userData;
    private SharedPreferences sharedPreferences;
    private DatabaseReference userCodeReference;

    private ProgressDialog progressDialog;
    GPSTracker gps;
    Context mContext;
    double latitude;
    double longitude;

    String name;
    String email;
    String status;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.selfLocation));

        sharedPreferences = getActivity().getSharedPreferences("RefLocator", Context.MODE_PRIVATE);

        mContext = getContext();

        String str = sharedPreferences.getString("Email", "REFEmail");
        name = sharedPreferences.getString("Name", "ReFFullName");
        status = sharedPreferences.getString("Status", "ReStatus");
        String code = sharedPreferences.getString("Code", "RefCode");
        email = str.split("@")[0];
        // Toast.makeText(getContext(), ""+email, Toast.LENGTH_SHORT).show();

        userCodeReference = codesReference.child(code);
        userCodeReference.addChildEventListener(this);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (mMap != null) {

                    Location();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void Location() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            // Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, getActivity());

            // Check if GPS enabled
            if (gps.canGetLocation()) {


                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading your location please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                progressDialog.dismiss();
                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();
                                mMap.getUiSettings().setCompassEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.getMaxZoomLevel();
                                mMap.getMinZoomLevel();
                                mMap.getUiSettings();
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                                mMap.addMarker(new MarkerOptions()
                                        .position(
                                                new LatLng(latitude, longitude))
                                        .title("I am Here!!")
                                        .icon(BitmapDescriptorFactory
                                                .fromResource(R.mipmap.ic_pin)));

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(latitude, longitude), 14));

                                Iscamera = true;


                                String lat = String.valueOf(latitude);
                                String lang = String.valueOf(longitude);
                                userCodeReference.child("Latitude").setValue(lat);
                                userCodeReference.child("Longitude").setValue(lang);
                                userCodeReference.child("Status").setValue(status);
                                userCodeReference.child("Name").setValue(name);
                                userCodeReference.child("Key").setValue(email);
                            }
                        }, 3000);


                    }
                }, 4000);
                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


                // \n is for new line
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
    }
}
