package com.example.dimas.komentar;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.MarkerOptions;




public class ClassMaps extends FragmentActivity implements  OnMapReadyCallback {

    private static final LatLng gedung_a = new LatLng(-6.974910, 107.631852);
    private static final LatLng gedung_n = new LatLng(-6.977188, 107.629737);
    private static final LatLng telu = new LatLng(-6.973974, 107.630274);

    private Marker mGedung_a;
    private Marker mGedung_n;

    private GoogleMap mMap;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        Button histori = (Button).findViewById(R.id.histori);
        Button histori = (Button)findViewById(R.id.histori);

        histori.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View arg0) {
                Intent info = new Intent(getApplicationContext(), ClassBacaKomentar.class);
                startActivity(info);
            }
        });
    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(telu);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(14);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{this, Manifest.permission.ACCESS_FINE_LOCATION});
            }

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        // Add some markers to the map, and add a data object to each marker.
        mGedung_a = mMap.addMarker(new MarkerOptions()
                .position(gedung_a)
                .title("Parkir Gedung A"));
        mGedung_a.setTag(0);
        mGedung_n = mMap.addMarker(new MarkerOptions()
                .position(gedung_n)
                .title("Parkir Gedung N"));
        mGedung_n.setTag(0);

        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(Marker marker){
                if(marker.getTitle().equals("Parkir Gedung A")){

                    Intent info = new Intent(getApplicationContext(), ClassDenahParkir.class);
                    startActivity(info);
                }else
                if(marker.getTitle().equals("Parkir Gedung N")){
                    Intent info = new Intent(getApplicationContext(), ClassDenahParkir2.class);
                    startActivity(info);
                }else{
                    Log.e("Bebas",""+marker.getTitle());
                }
            }
        });



        // Set a listener for marker click.

    }

    }