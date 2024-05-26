package com.olongapogo;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.olongapogo.databinding.ActivityFormDestinationPickupBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FormDestinationPickup extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions pickupMarker, destinationMarker;
    private ActivityFormDestinationPickupBinding binding;
    String currentUser;
    TextView selectingLocationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFormDestinationPickupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        runOnUiThread(() -> {
            mapFragment.getMapAsync(this);
        });
        // Set the button click listener


        Intent i = getIntent();
        currentUser = i.getStringExtra("currentUser");
        binding.confirmButton.setOnClickListener(v -> {
            if (pickupMarker != null && destinationMarker != null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> pickupAddresses = geocoder.getFromLocation(pickupMarker.getPosition().latitude, pickupMarker.getPosition().longitude, 1);
                    List<Address> destinationAddresses = geocoder.getFromLocation(destinationMarker.getPosition().latitude, destinationMarker.getPosition().longitude, 1);
                    if (!pickupAddresses.isEmpty() && !destinationAddresses.isEmpty()) {
                        String pickupAddress = pickupAddresses.get(0).getAddressLine(0);
                        String destinationAddress = destinationAddresses.get(0).getAddressLine(0);
                        Intent intent = new Intent(this, Form.class);
                        intent.putExtra("pickupLat", String.valueOf(pickupMarker.getPosition().latitude));
                        intent.putExtra("pickupLng", String.valueOf(pickupMarker.getPosition().longitude));
                        intent.putExtra("destinationLat", String.valueOf(destinationMarker.getPosition().latitude));
                        intent.putExtra("destinationLng", String.valueOf(destinationMarker.getPosition().longitude));
                        intent.putExtra("pickupAddress", pickupAddress);
                        intent.putExtra("destinationAddress", destinationAddress);
                        intent.putExtra("currentUser", currentUser);

                        startActivity(intent);
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng olongapo = new LatLng(14.8292, 120.2827);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(olongapo, 15.0f));

        mMap.setOnMapClickListener(latLng -> {
            if (pickupMarker == null) {
                pickupMarker = new MarkerOptions().position(latLng).title("Pickup Location");

                mMap.addMarker(pickupMarker);
            } else if (destinationMarker == null) {
                destinationMarker = new MarkerOptions().position(latLng).title("Destination Location");

                mMap.addMarker(destinationMarker);
            } else {
                mMap.clear();
                pickupMarker = new MarkerOptions().position(latLng).title("Pickup Location");

                mMap.addMarker(pickupMarker);
                destinationMarker = null;
            }
        });
    }
}