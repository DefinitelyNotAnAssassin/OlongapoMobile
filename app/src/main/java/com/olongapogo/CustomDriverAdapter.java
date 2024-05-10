package com.olongapogo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomDriverAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, String>> rides;
    String currentUser;



    LayoutInflater inflater;


    public CustomDriverAdapter(Context context, ArrayList<Map<String, String>> rides, String currentUser)
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

        view = inflater.inflate(R.layout.activity_custom_driver,null);

        Map<String, String> currentRide = rides.get(i);
        System.out.println(currentRide);
        TextView id = view.findViewById(R.id.tvId);
        TextView owner = view.findViewById(R.id.tvOwner);
        TextView destination = view.findViewById(R.id.tvDestination);
        TextView arrival = view.findViewById(R.id.tvArrival);
        TextView driver = view.findViewById(R.id.tvDriver);
        TextView status = view.findViewById(R.id.tvStatus);
        Button btnCompleteDriver = view.findViewById(R.id.btnCompleteDriver);

        if (currentRide.get("ride_status").equals("complete")) {
            btnCompleteDriver.setVisibility(View.GONE);
        }
        else{
            btnCompleteDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // send request to server to update ride status to complete
                    // update the list view

                    RequestQueue queue = Volley.newRequestQueue(context);

                    String url = "http://olongapogo.pythonanywhere.com/rides/updateRideStatus";

                    StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                            response -> {
                                // Handle successful response
                                System.out.println(response);
                                Intent intent = new Intent(context, Search.class);
                                intent.putExtra("currentUser", currentUser);
                                context.startActivity(intent);
                            },
                            error -> {
                                // Handle error
                                System.out.println(error);
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("ride_id", currentRide.get("id"));
                            return params;
                        }
                    };
                    queue.add(stringRequest);


                }
            });
        }

        owner.setText(currentRide.get("owner_id"));
        destination.setText(currentRide.get("destination"));
        arrival.setText(currentRide.get("required_arrival_time"));
        driver.setText(currentRide.get("driver_id"));
        status.setText(currentRide.get("ride_status"));



        return view;
    }
}