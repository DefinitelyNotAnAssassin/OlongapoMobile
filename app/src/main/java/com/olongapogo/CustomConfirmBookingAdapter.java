package com.olongapogo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomConfirmBookingAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, String>> rides;
    String currentUser;



    LayoutInflater inflater;


    public CustomConfirmBookingAdapter(Context context, ArrayList<Map<String, String>> rides, String currentUser)
    {
        this.context = context;
        this.rides = rides;
        this.currentUser = currentUser;

        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return rides.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.activity_custom_confirmbooking,null);

        Map<String, String> currentRide = rides.get(i);
        System.out.println(currentRide);

        TextView tvIdAccept = view.findViewById(R.id.tvIdAccept);
        TextView tvVehicleType = view.findViewById(R.id.tvVehicleType);
        TextView tvArrivalAccept = view.findViewById(R.id.tvArrivalAccept);
        TextView tvPassengersAccept = view.findViewById(R.id.tvPassengersAccept);
        TextView tvRequestAccept  = view.findViewById(R.id.tvRequestAccept);
        Button btnAcceptBooking = view.findViewById(R.id.btnAcceptBooking);


        tvIdAccept.setText(currentRide.get("owner_id"));
        tvVehicleType.setText(currentRide.get("requested_vehicle_type"));
        tvArrivalAccept.setText(currentRide.get("required_arrival_time"));
        tvPassengersAccept.setText(currentRide.get("passenger_number_from_owner"));
        tvRequestAccept.setText(currentRide.get("special_request"));



        btnAcceptBooking.setOnClickListener(v -> {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "http://192.168.1.4:8000/rides/acceptRide";

            // post a request to the url with the ride id and the currentUser


            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    response -> {
                        Intent intent = new Intent(context, Search.class);
                        intent.putExtra("currentUser", currentUser);
                        context.startActivity(intent);
                        Log.d("Response", response);
                    },
                    error -> {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("ride_id", currentRide.get("id"));
                    params.put("currentUser", currentUser);
                    return params;
                }
            };
            requestQueue.add(postRequest);



        });

        return view;
    }
}