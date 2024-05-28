package com.olongapogo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity {

    ArrayList<Map<String, String>> rides = new ArrayList<Map<String, String>>();
    String currentUser;
    ListView driverRides;
    Button searchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://olongapogo.com/rides/getRides";


        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                response -> {
                    // Handle successful response

                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        System.out.println(responseJSON);
                        System.out.println("^^ From Search");

                        for (int i = 0; i < responseJSON.getJSONArray("driver_rides").length(); i++) {
                            JSONObject ride = responseJSON.getJSONArray("driver_rides").getJSONObject(i);
                            Map<String, String> rideMap = new HashMap<>();
                            // [{"id":2,"owner_id":4,"driver_id":1,"destination":"Olongapo","required_arrival_time":"2024-05-07T10:13:00Z","passenger_number_from_owner":4,"passenger_number_in_total":4,"ride_status":"complete","requested_vehicle_type":"Sedan","special_request":"Nothing","can_be_shared":false,"sharer_id_and_passenger_number_pair":null}]}
                            //2024-05-09 17:46:49.241 13684-13684 AndroidRuntime          com.olongapogo                       D  Shutting down VM

                            rideMap.put("id", ride.getString("id"));
                            rideMap.put("owner_id", ride.getString("owner_id"));
                            rideMap.put("driver_id", ride.getString("driver_id"));
                            rideMap.put("destination", ride.getString("destination"));
                            rideMap.put("pickup_lat", ride.getString("pickup_latitude"));
                            rideMap.put("pickup_long", ride.getString("pickup_longitude"));
                            rideMap.put("destination_lat", ride.getString("destination_latitude"));
                            rideMap.put("destination_long", ride.getString("destination_longitude"));
                            rideMap.put("required_arrival_time", ride.getString("required_arrival_time"));
                            rideMap.put("passenger_number_from_owner", ride.getString("passenger_number_from_owner"));
                            rideMap.put("passenger_number_in_total", ride.getString("passenger_number_in_total"));
                            rideMap.put("ride_status", ride.getString("ride_status"));
                            rideMap.put("requested_vehicle_type", ride.getString("requested_vehicle_type"));
                            rideMap.put("special_request", ride.getString("special_request"));
                            rides.add(rideMap);
                        }




                        driverRides = findViewById(R.id.listViewDriverRides);
                        CustomDriverAdapter customDriverAdapter = new CustomDriverAdapter(this, rides, currentUser);
                        driverRides.setAdapter(customDriverAdapter);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },
                error -> {
                    // Handle error
                    Log.e("MyActivity", "Error fetching data: " + error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("currentUser", currentUser);
                // Add more parameters as needed
                return params;
            }
        };

        queue.add(stringRequest);

        System.out.println(rides);


        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");


        searchBtn = findViewById(R.id.btnSearch);

        searchBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(Search.this, DriverPage.class);
            intent1.putExtra("currentUser", currentUser);
            startActivity(intent1);
        });


    }
}