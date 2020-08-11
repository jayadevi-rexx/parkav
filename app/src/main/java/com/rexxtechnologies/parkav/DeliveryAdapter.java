package com.rexxtechnologies.parkav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.MyPendingHolder> {
    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<>();
    String id_value,id_name;
    public DeliveryAdapter(DeliveryCollection deliveryCollection, ArrayList<HashMap<String, String>> arraylist2) {
        this.context = context;
        data = arraylist2;
    }

    @NonNull
    @Override
    public MyPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.delivery_custom_list,parent,false);
        MyPendingHolder myHolder=new MyPendingHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPendingHolder holder, int position) {
        resultp = data.get(position);
        if (resultp==null){
            Toast.makeText(context,"No Data.............",Toast.LENGTH_LONG).show();
        }else {
            holder.name.setText(resultp.get("customer_name"));
            holder.invoice.setText(resultp.get("invoice_no"));
            holder.amount.setText(resultp.get("amount"));
//            holder.total.setText(resultp.get("total"));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyPendingHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView invoice;

        TextView amount;
//        TextView total;
        public MyPendingHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.pro_name);
            invoice= (TextView) itemView.findViewById(R.id.invoice_no);
//
            amount = (TextView) itemView.findViewById(R.id.delivery_amount);
//            total = (TextView) itemView.findViewById(R.id.status);
        }
    }
}
