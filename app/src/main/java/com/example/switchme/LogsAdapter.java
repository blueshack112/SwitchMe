package com.example.switchme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder> {

    // Variables
    private ArrayList<LogsModel> mDataset;

    // Default code
    public LogsAdapter(ArrayList<LogsModel> dataset) {
        mDataset = dataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.logs_list_item, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.roomTextview.setText("Room  : " + mDataset.get(position).roomID);
        holder.energyTextview.setText("Energy: " + mDataset.get(position).energyUsed + " Watts");
        holder.startedTextview.setText("From: " + mDataset.get(position).startedAt);
        holder.endedTextview.setText("To  : " + mDataset.get(position).endedAt);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Extra code
    public void addToDataset(LogsModel x) {
        mDataset.add(x);
        notifyDataSetChanged();
    }

    // View holder class
    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView roomTextview;
        public TextView energyTextview;
        public TextView startedTextview;
        public TextView endedTextview;

        public MyViewHolder(View v) {
            super(v);
            roomTextview = v.findViewById(R.id.room_id_text);
            energyTextview = v.findViewById(R.id.energy_used_text);
            startedTextview = v.findViewById(R.id.started_at_text);
            endedTextview = v.findViewById(R.id.ended_at_text);
        }
    }
}
