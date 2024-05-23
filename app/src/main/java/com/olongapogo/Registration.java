package com.olongapogo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    EditText etFnameRegister, etLnameRegister, etGmailRegister,  etPasswordRegister, etUsernameRegister;
    Button btnRegister;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFnameRegister = findViewById(R.id.etFnameRegister);
        etLnameRegister = findViewById(R.id.etLnameRegister);
        etGmailRegister = findViewById(R.id.etGmailRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
        etUsernameRegister = findViewById(R.id.etUsernameRegister);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://olongapogo.com/accounts/register";

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {


            String fname = etFnameRegister.getText().toString();
            String lname = etLnameRegister.getText().toString();
            String gmail = etGmailRegister.getText().toString();
            String password = etPasswordRegister.getText().toString();
            String username = etUsernameRegister.getText().toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // convert response to json object

                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        currentUser = jsonObject.getString("currentUser");
                        goToUser();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }   
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Registration.this, "Username Already Exists", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("fname", fname);
                    params.put("lname", lname);
                    params.put("gmail", gmail);
                    params.put("password", password);
                    params.put("username", username);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        });
    }

    public void goToUser(){
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}