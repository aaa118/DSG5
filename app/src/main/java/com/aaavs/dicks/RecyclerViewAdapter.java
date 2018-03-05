package com.aaavs.dicks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by aaavs on 3/3/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    Context context;
    ArrayList<String> allData = new ArrayList<>();

    public RecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public RecyclerViewAdapter(Context context, ArrayList<String> allData) {
        this.context = context;
        this.allData = allData;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;
        TextView distanceTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.tv_address);
            distanceTextView = itemView.findViewById(R.id.tv_distance);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.from(context).inflate(R.layout.simple_list_item_1,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.addressTextView.setText(allData.get(position));
//        Model model = allData.get(position)
//        holder.addressTextView.setText(allData.get(position));

    }

    @Override
    public int getItemCount() {
        return allData.size();
    }
}
