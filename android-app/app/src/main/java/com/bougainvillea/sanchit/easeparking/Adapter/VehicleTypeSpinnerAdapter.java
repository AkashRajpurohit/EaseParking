package com.bougainvillea.sanchit.easeparking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bougainvillea.sanchit.easeparking.Beans.VehicleTypeSpinnerFormat;
import com.bougainvillea.sanchit.easeparking.R;

import java.util.ArrayList;

public class VehicleTypeSpinnerAdapter extends ArrayAdapter<VehicleTypeSpinnerFormat> {

    int groupid;
    Activity context;
    ArrayList<VehicleTypeSpinnerFormat> list;
    LayoutInflater inflater;

    public VehicleTypeSpinnerAdapter(Activity context, int groupid, ArrayList<VehicleTypeSpinnerFormat> list){
        super(context,groupid,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView= itemView.findViewById(R.id.vehicle_type_img);
        imageView.setImageResource(list.get(position).getImg());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);
    }
}
