package com.olongapogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Form extends AppCompatActivity {
    String currentUser;
    String pickupAddress, destinationAddress;
    String pickupLat, pickupLng, destinationLat, destinationLng;
    EditText etAddressForm, etDateForm, etTimeForm, etPassengerForm, etVehicleTypeForm, etNotesForm;
    Button btnMakeReqForm;
    ImageButton mapBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RequestQueue queue = Volley.newRequestQueue(this);


        etAddressForm = findViewById(R.id.etAddressForm);
        etDateForm = findViewById(R.id.etDateForm);
        etTimeForm = findViewById(R.id.etTimeForm);
        etPassengerForm = findViewById(R.id.etPassengerForm);
        etVehicleTypeForm = findViewById(R.id.etVehicleTypeForm);
        etNotesForm = findViewById(R.id.etNotesForm);
        btnMakeReqForm = findViewById(R.id.btnMakeReqForm);
        mapBtn = findViewById(R.id.mapBtn);



        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");
        pickupAddress = intent.getStringExtra("pickupAddress");
        destinationAddress = intent.getStringExtra("destinationAddress");
        pickupLat = intent.getStringExtra("pickupLat");
        pickupLng = intent.getStringExtra("pickupLng");
        destinationLat = intent.getStringExtra("destinationLat");
        destinationLng = intent.getStringExtra("destinationLng");


        // Initial Load Shall be Null
        System.out.println(pickupLat);
        System.out.println(pickupLng);
        System.out.println(destinationLat);
        System.out.println(destinationLng);

        etAddressForm.setText(destinationAddress);
        etNotesForm.setText(pickupAddress);
        String url = "http://olongapogo.com/rides/create";
        btnMakeReqForm.setOnClickListener(v -> {
            String address = etAddressForm.getText().toString();
            String date = etDateForm.getText().toString();
            String time = etTimeForm.getText().toString();
            String passenger = etPassengerForm.getText().toString();
            String vehicleType = etVehicleTypeForm.getText().toString();
            String notes = etNotesForm.getText().toString();

            // make a post request to the server

            // if the request is successful, go to the request activity

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                    response -> {
                        // Handle successful response
                        Intent intent1 = new Intent(Form.this, RequestActivity.class);
                        intent1.putExtra("currentUser", currentUser);
                        startActivity(intent1);
                    },
                    error -> {
                        // Handle error
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("destination", address);
                    // format the date and time into this format "%Y-%m-%dT%H:%M"
                    params.put("arrival_time", date + "T" + time);
                    params.put("number_of_passengers", passenger);
                    params.put("vehicle_type", vehicleType);
                    params.put("special_request", notes);
                    params.put("currentUser", currentUser);
                    params.put("pickup_latitude", pickupLat);
                    params.put("pickup_longitude", pickupLng);
                    params.put("destination_latitude", destinationLat);
                    params.put("destination_longitude", destinationLng);
                    return params;
                }

            };

            queue.add(stringRequest);

        });


        mapBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(Form.this, FormDestinationPickup.class);
            intent1.putExtra("currentUser", currentUser);
            startActivity(intent1);
        });
    }


    public void goToUser(View v){
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}