package com.bougainvillea.sanchit.easeparking.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bougainvillea.sanchit.easeparking.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Calendar;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;
    private static EditText info_window_name, info_window_booking_id, info_window_no_plate, info_window_time, info_window_req_time;

    public CustomInfoWindowAdapter(Context context, String vehicleNo) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
        info_window_name = mWindow.findViewById(R.id.info_window_name);
        info_window_booking_id = mWindow.findViewById(R.id.info_window_booking_id);
        info_window_no_plate = mWindow.findViewById(R.id.info_window_no_plate);
        info_window_time = mWindow.findViewById(R.id.info_window_time);
        try {
            SharedPreferences preferencesReader = mContext.getSharedPreferences("Customer", Context.MODE_PRIVATE);
            String customer_name = preferencesReader.getString("Customer Name", null);
            info_window_name.setText(customer_name);
            info_window_booking_id.setText("4357682930");
            info_window_no_plate.setText(vehicleNo);
            info_window_time.setText(Calendar.getInstance().getTime().toString());
        } catch (Exception e) {

        }
    }

    public static void setBookingId() {
        try {
            info_window_booking_id.setText("4352398129");
        } catch (Exception e) {
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mWindow;
    }
}
