package com.olongapogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class CustomDriverAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, String>> rides;



    LayoutInflater inflater;


    public CustomDriverAdapter(Context context, ArrayList<Map<String, String>> rides)
    {
        this.context = context;
        this.rides = rides;

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

          owner.setText(currentRide.get("owner_id"));
        destination.setText(currentRide.get("destination"));
        arrival.setText(currentRide.get("required_arrival_time"));
        driver.setText(currentRide.get("driver_id"));
        status.setText(currentRide.get("ride_status"));


        return view;
    }
}