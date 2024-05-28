package com.olongapogo;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.olongapogo.databinding.ActivityDriverRideMapBinding;

public class DriverRideMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String destination_latitude;
    String destination_longitude;
    String pickup_latitude;
    String pickup_longitude;
    private ActivityDriverRideMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDriverRideMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent i = getIntent();
        destination_latitude = i.getStringExtra("destination_lat");
        destination_longitude = i.getStringExtra("destination_long");
        pickup_latitude = i.getStringExtra("pickup_lat");
        pickup_longitude = i.getStringExtra("pickup_long");


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
        Intent i = getIntent();
        // Add a marker in Sydney and move the camera
        destination_latitude = i.getStringExtra("destination_lat");
        destination_longitude = i.getStringExtra("destination_long");
        pickup_latitude = i.getStringExtra("pickup_lat");
        pickup_longitude = i.getStringExtra("pickup_long");


        System.out.println("Destination Latitude: " + destination_latitude);
        System.out.println("Destination Longitude: " + destination_longitude);
        System.out.println("Pickup Latitude: " + pickup_latitude);
        System.out.println("Pickup Longitude: " + pickup_longitude);
        // Add a marker in Sydney and move the camera
        LatLng olongapo = new LatLng(14.8292, 120.2827);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(olongapo, 15.0f));

        LatLng destination = new LatLng(Double.parseDouble(destination_latitude), Double.parseDouble(destination_longitude));
        LatLng pickup = new LatLng(Double.parseDouble(pickup_latitude), Double.parseDouble(pickup_longitude));
        mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
        mMap.addMarker(new MarkerOptions().position(pickup).title("Pickup"));

    }
}