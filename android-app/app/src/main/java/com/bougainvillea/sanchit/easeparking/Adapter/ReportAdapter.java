package com.bougainvillea.sanchit.easeparking.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bougainvillea.sanchit.easeparking.Beans.Report;
import com.bougainvillea.sanchit.easeparking.R;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    private List<Report> reportList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_content, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.title.setText(report.getText());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
