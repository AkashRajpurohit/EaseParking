package com.bougainvillea.sanchit.easeparking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bougainvillea.sanchit.easeparking.Beans.SpinnerFormat;
import com.bougainvillea.sanchit.easeparking.R;
import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerFormat> {

    int groupid;
    Activity context;
    ArrayList<SpinnerFormat> list;

    LayoutInflater inflater;

    public SpinnerAdapter(Activity context, int groupid, int id, ArrayList<SpinnerFormat> list){
        super(context,id,list);
        this.list=list;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid=groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent ){
        View itemView=inflater.inflate(groupid,parent,false);
        ImageView imageView= itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImg());
        TextView textView= itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getTxt());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent){
        return getView(position,convertView,parent);

    }
}
