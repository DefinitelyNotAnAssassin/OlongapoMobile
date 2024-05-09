package com.olongapogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class CustomOwnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, String>> rides;



    LayoutInflater inflater;


    public CustomOwnerAdapter(Context context, ArrayList<Map<String, String>> rides)
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

        view = inflater.inflate(R.layout.activity_custom_owner,null);

        Map<String, String> currentRide = rides.get(i);


        TextView tvId = (TextView) view.findViewById(R.id.tvIdOwner);
        TextView tvPassenger = (TextView) view.findViewById(R.id.tvOwnerPassenger);
        TextView tvDestination = (TextView) view.findViewById(R.id.tvDestinationOwner);
        TextView tvArrival = (TextView) view.findViewById(R.id.tvArrivalOwner);
        TextView tvDriver = (TextView) view.findViewById(R.id.tvDriverOwner);
        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatusOwner);

        tvId.setText(currentRide.get("id"));
        tvPassenger.setText(currentRide.get("owner_id"));
        tvDestination.setText(currentRide.get("destination"));

        // make the required arrival time format from 2024-05-07T19:10:44Z to something readable

        tvArrival.setText(currentRide.get("required_arrival_time").replace("T", " ").replace("Z", ""));
        if (currentRide.get("driver_id").equals("null null")){
            tvDriver.setText("No driver yet");
        }
        else{
            tvDriver.setText(currentRide.get("driver_id"));

        }
           tvStatus.setText(currentRide.get("ride_status"));


        return view;
    }
}