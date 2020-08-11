package com.rexxtechnologies.parkav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class TodayTaskAdapter extends RecyclerView.Adapter<TodayTaskAdapter.MyPendingHolder> implements View.OnClickListener {
    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<>();
    ArrayAdapter<String> dataAdapter;


    public TodayTaskAdapter(TodayTask context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
       data = arraylist;
    }

//    private final OnClickListener mOnClickListener = new MyOnClickListener();
    @NonNull
    @Override
    public MyPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.activity_today_tasks,parent,false);
        v.setOnClickListener(this);
        MyPendingHolder myHolder=new MyPendingHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPendingHolder holder, int position) {
        resultp = data.get(position);
        holder.name.setText(resultp.get("Customer_name"));
        holder.phonenumber.setText(resultp.get("phone_number"));
        holder.address.setText(resultp.get("address"));
        holder.status.setText(resultp.get("status"));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class MyPendingHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phonenumber;

        TextView address;
        TextView status;


        public MyPendingHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phonenumber= (TextView) itemView.findViewById(R.id.ph_number);
//
            address = (TextView) itemView.findViewById(R.id.address);
            status = (TextView) itemView.findViewById(R.id.status);
//
        }
    }

}
