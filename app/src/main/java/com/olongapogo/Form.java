package com.olongapogo;

import static java.lang.String.format;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Form extends AppCompatActivity {
    String currentUser;
    String pickupAddress, destinationAddress;
    String pickupLat, pickupLng, destinationLat, destinationLng;
    EditText etAddressForm, etDateForm, etTimeForm, etPassengerForm, etVehicleTypeForm, etNotesForm;
    Button btnMakeReqForm;
    ImageButton calendarBtn;
    ImageButton mapBtn, clockBtn;

    int year, month, day;




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
        calendarBtn = findViewById(R.id.calendarBtn);
        clockBtn = findViewById(R.id.clockBtn);



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

        Calendar calendar = Calendar.getInstance();
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Form.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                        String formattedDate = sdf.format(calendar.getTime());
                        etDateForm.setText(sdf.format(calendar.getTime()));
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });

        clockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(Form.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String formattedTime = sdf.format(calendar.getTime());
                        etTimeForm.setText(formattedTime);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
    }


    public void goToUser(View v){
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}