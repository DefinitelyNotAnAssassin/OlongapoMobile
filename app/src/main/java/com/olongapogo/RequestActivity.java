package com.olongapogo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class RequestActivity extends AppCompatActivity {
    ListView listViewOwnerRides;
    Button searchBtn;
    ArrayList<Map<String, String>> rides = new ArrayList<>();

    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://olongapogo.pythonanywhere.com/rides/getRides";


        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                response -> {
                    // Handle successful response

                    try {
                        JSONObject responseJSON = new JSONObject(response);
                        System.out.println(responseJSON);
                        System.out.println("^^ From RequestActivity");

                        for (int i = 0; i < responseJSON.getJSONArray("user_rides").length(); i++) {
                            JSONObject ride = responseJSON.getJSONArray("user_rides").getJSONObject(i);
                            Map<String, String> rideMap = new HashMap<>();

                            rideMap.put("id", ride.getString("id"));
                            rideMap.put("owner_id", ride.getString("owner__first_name") + " " + ride.getString("owner__last_name"));
                            rideMap.put("driver_id", ride.getString("driver__first_name") + " " + ride.getString("driver__last_name"));
                            rideMap.put("destination", ride.getString("destination"));
                            rideMap.put("required_arrival_time", ride.getString("required_arrival_time"));
                            rideMap.put("passenger_number_from_owner", ride.getString("passenger_number_from_owner"));
                            rideMap.put("passenger_number_in_total", ride.getString("passenger_number_in_total"));
                            rideMap.put("ride_status", ride.getString("ride_status"));
                            rideMap.put("requested_vehicle_type", ride.getString("requested_vehicle_type"));
                            rideMap.put("special_request", ride.getString("special_request"));
                            rides.add(rideMap);
                        }


                        listViewOwnerRides = findViewById(R.id.listViewOwnerRides);
                        CustomOwnerAdapter customOwnerAdapter = new CustomOwnerAdapter(this, rides);
                        listViewOwnerRides.setAdapter(customOwnerAdapter);

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


        searchBtn = findViewById(R.id.btnRequest);

        searchBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(RequestActivity.this, Form.class);
            intent1.putExtra("currentUser", currentUser);
            startActivity(intent1);
        });



}
}