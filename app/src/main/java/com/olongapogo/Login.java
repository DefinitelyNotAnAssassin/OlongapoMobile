package com.olongapogo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    // Hello Sherry

    EditText username;
    EditText password;
    ArrayList<Map<String, String>> rides = new ArrayList<>();
    Button loginBtn;

    String currentUser;

    ListView driverRides;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        username = (EditText) findViewById(R.id.etLoginUsername);
        password = (EditText) findViewById(R.id.etLoginPassword);
        loginBtn = (Button) findViewById(R.id.btnLogin);

        RequestQueue queue = Volley.newRequestQueue(this);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://192.168.1.4:8000/accounts/login";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            // Handle successful response

                            try {
                                JSONObject responseJSON = new JSONObject(response);
                                System.out.println(responseJSON);

                                if(responseJSON.getString("message").equals("Invalid credential")){
                                    Toast.makeText(getApplicationContext(), "Invalid Credentials. Please try again.", Toast.LENGTH_SHORT).show();
                                    password.setText("");
                                }
                                else if (responseJSON.getString("message").equals("Login successful")){
                                    currentUser = responseJSON.getString("currentUser");
                                    if (responseJSON.getString("is_driver").equals("true")){

                                        // iterate through the responseJSON rides and add them to the rides arraylist

                                        for (int i = 0; i < responseJSON.getJSONArray("driver_rides").length(); i++) {
                                            JSONObject ride = responseJSON.getJSONArray("driver_rides").getJSONObject(i);

                                            Map<String, String> rideMap = new HashMap<>();
                                            // [{"id":2,"owner_id":4,"driver_id":1,"destination":"Olongapo","required_arrival_time":"2024-05-07T10:13:00Z","passenger_number_from_owner":4,"passenger_number_in_total":4,"ride_status":"complete","requested_vehicle_type":"Sedan","special_request":"Nothing","can_be_shared":false,"sharer_id_and_passenger_number_pair":null}]}
                                            //2024-05-09 17:46:49.241 13684-13684 AndroidRuntime          com.olongapogo                       D  Shutting down VM

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

                                        goToDriver();
                                    }
                                    else{

                                        //
                                        // iterate through the responseJSON rides and add them to the rides arraylist
                                        // the keys are 'owner__first_name', 'owner__last_name', 'owner__username', 'destination', 'required_arrival_time', 'passenger_number_from_owner', 'passenger_number_in_total', 'ride_status', 'requested_vehicle_type', 'special_request', 'can_be_shared', 'sharers__first_name', 'sharers__last_name',  'id', 'driver_id', 'driver__first_name', 'driver__last_name', 'driver__username')
                                        //

                                        for (int i = 0; i < responseJSON.getJSONArray("user_rides").length(); i++) {
                                            JSONObject ride = responseJSON.getJSONArray("user_rides").getJSONObject(i);
                                            // access the owner json inside the ride

                                            JSONObject owner = ride.getJSONObject("owner");

                                            Map<String, String> rideMap = new HashMap<>();
                                            // [{"id":2,"owner_id":4,"driver_id":1,"destination":"Olongapo","required_arrival_time":"2024-05-07T10:13:00Z","passenger_number_from_owner":4,"passenger_number_in_total":4,"ride_status":"complete","requested_vehicle_type":"Sedan","special_request":"Nothing","can_be_shared":false,"sharer_id_and_passenger_number_pair":null}]}
                                            //2024-05-09 17:46:49.241 13684-13684 AndroidRuntime          com.olongapogo                       D  Shutting down VM

                                            rideMap.put("id", ride.getString("id"));
                                            rideMap.put("owner_id", owner.getString("first_name") + " " + owner.getString("last_name"));
                                            String driverFirstName = ride.optString("driver__first_name");
                                            String driverLastName = ride.optString("driver__last_name");

                                            String driverName = "No driver assigned";
                                            if (driverFirstName != null && !driverFirstName.isEmpty() && driverLastName != null && !driverLastName.isEmpty()) {
                                                driverName = driverFirstName + " " + driverLastName;
                                            }

                                            rideMap.put("driver_id", driverName);
                                            rideMap.put("destination", ride.getString("destination"));
                                            rideMap.put("required_arrival_time", ride.getString("required_arrival_time"));
                                            rideMap.put("passenger_number_from_owner", ride.getString("passenger_number_from_owner"));
                                            rideMap.put("passenger_number_in_total", ride.getString("passenger_number_in_total"));
                                            rideMap.put("ride_status", ride.getString("ride_status"));
                                            rideMap.put("requested_vehicle_type", ride.getString("requested_vehicle_type"));
                                            rideMap.put("special_request", ride.getString("special_request"));
                                            rides.add(rideMap);
                                        }




                                        goToUser();
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Invalid Username / Password", Toast.LENGTH_SHORT).show();
                                }

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
                        params.put("username", username.getText().toString());
                        params.put("password", password.getText().toString());
                        // Add more parameters as needed
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });

    }


    public void goToDriver(){

        Intent i = new Intent(this, Search.class);

        i.putExtra("currentUser", currentUser);
        startActivity(i);
    }

    public void goToUser(){
        System.out.println(rides);
        Intent i = new Intent(this, com.olongapogo.RequestActivity.class);

        i.putExtra("currentUser", currentUser);
        startActivity(i);
    }

    public void goToRegister(View v){
        Intent i = new Intent(this, Registration.class);
        startActivity(i);
    }
}

