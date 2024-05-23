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
                String url = "http://olongapogo.com/accounts/login";

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


                                        goToDriver();
                                    }
                                    else{

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

