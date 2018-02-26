package com.abc.pprat.locator;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class PartnerLocation extends FragmentActivity {

    private GoogleMap mMap;
    private FirebaseDatabase firebase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebase.getReference("users");
    Double lat;
    Double lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_location);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        String userKey = getIntent().getStringExtra("User");
        Toast.makeText(PartnerLocation.this, "" + userKey, Toast.LENGTH_SHORT).show();

        DatabaseReference codeReference = databaseReference.child(userKey).child("Code");

        codeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String code = dataSnapshot.getValue(String.class);


               // Toast.makeText(PartnerLocation.this, ""+code, Toast.LENGTH_SHORT).show();
                DatabaseReference codesRef=firebase.getReference("Codes").child(code);
                codesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,String> map=(Map<String, String>) dataSnapshot.getValue();
                        String latitude = map.get("Latitude");
                        String longitude = map.get("Longitude");
                        lat=Double.parseDouble(latitude);
                        lang=Double.parseDouble(longitude);
                        //Toast.makeText(PartnerLocation.this, "" + lat + "test" + lang, Toast.LENGTH_SHORT).show();

                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                mMap = googleMap;
                                LatLng sydney = new LatLng(lat, lang);
                                mMap.addMarker(new MarkerOptions().position(sydney).title("I'm here"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                mMap.getUiSettings().setCompassEnabled(true);
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.getMaxZoomLevel();
                                mMap.getMinZoomLevel();
                                mMap.getUiSettings();
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
