package com.olongapogo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverPage extends AppCompatActivity {

    ListView listViewDriverPage;
    String currentUser;
    ArrayList<Map<String, String>> rides = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        currentUser = intent.getStringExtra("currentUser");

        listViewDriverPage = findViewById(R.id.listViewDriverPage);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://olongapogo.com/rides/search";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // convert response to json object

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(jsonObject);
                    for (int i = 0; i < jsonObject.getJSONArray("driver_rides").length(); i++) {
                        JSONObject ride = jsonObject.getJSONArray("driver_rides").getJSONObject(i);
                        Map<String, String> rideMap = new HashMap<>();
                        // [{"id":2,"owner_id":4,"driver_id":1,"destination":"Olongapo","required_arrival_time":"2024-05-07T10:13:00Z","passenger_number_from_owner":4,"passenger_number_in_total":4,"ride_status":"complete","requested_vehicle_type":"Sedan","special_request":"Nothing","can_be_shared":false,"sharer_id_and_passenger_number_pair":null}]}
                        //2024-05-09 17:46:49.241 13684-13684 AndroidRuntime          com.olongapogo                       D  Shutting down VM

                        rideMap.put("id", ride.getString("id"));
                        rideMap.put("owner_id", ride.getString("owner_id"));
                        rideMap.put("destination", ride.getString("destination"));
                        rideMap.put("required_arrival_time", ride.getString("required_arrival_time"));
                        rideMap.put("passenger_number_from_owner", ride.getString("passenger_number_from_owner"));
                        rideMap.put("passenger_number_in_total", ride.getString("passenger_number_in_total"));
                        rideMap.put("ride_status", ride.getString("ride_status"));
                        rideMap.put("requested_vehicle_type", ride.getString("requested_vehicle_type"));
                        rideMap.put("special_request", ride.getString("special_request"));
                        rideMap.put("pickup_long", ride.getString("pickup_longitude"));
                        rideMap.put("pickup_lat", ride.getString("pickup_latitude"));
                        rideMap.put("destination_long", ride.getString("destination_longitude"));
                        rideMap.put("destination_lat", ride.getString("destination_latitude"));
                        rides.add(rideMap);
                    }

                    CustomConfirmBookingAdapter customDriverPageAdapter = new CustomConfirmBookingAdapter(DriverPage.this, rides, currentUser);
                    listViewDriverPage.setAdapter(customDriverPageAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DriverPage.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("currentUser", currentUser);
                params.put("search_as", "driver");

                return params;
            }
        };
        queue.add(stringRequest);
    }
}