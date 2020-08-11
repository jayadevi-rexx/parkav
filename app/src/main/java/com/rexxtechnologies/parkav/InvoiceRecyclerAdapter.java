package com.rexxtechnologies.parkav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class InvoiceRecyclerAdapter extends RecyclerView.Adapter<InvoiceRecyclerAdapter.MyPendingHolder> {

    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<>();
    String id_value,id_name,id_cost,id_code,id_qyt;
//    private MyInterface listener;
    public InvoiceRecyclerAdapter(InvoiceActivity context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @NonNull
    @Override
    public MyPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.invoice_custom_list,parent,false);
        InvoiceRecyclerAdapter.MyPendingHolder myHolder=new InvoiceRecyclerAdapter.MyPendingHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPendingHolder holder, int position) {
        resultp=data.get(position);
        holder.name.setText(resultp.get("Customer_name"));
        holder.code.setText(resultp.get("code"));
        holder.cost.setText(resultp.get("cost"));
        holder.qty.setText(resultp.get("qty"));
        holder.id.setText(resultp.get("id"));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_name=holder.name.getText().toString();
                id_code=holder.code.getText().toString();
//                id_code=resultp.get("code");
                id_cost=holder.cost.getText().toString();
//                id_cost=resultp.get("cost");
                id_qyt=holder.qty.getText().toString();
//                id_qyt=holder.qty.getText().toString();
//                id_qyt=resultp.get("qty");
                id_value=holder.id.getText().toString();
//                id_value=resultp.get("id");
                ((InvoiceActivity)context).foo(id_name,id_cost,id_code,id_qyt,id_value);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyPendingHolder extends RecyclerView.ViewHolder {
        TextView name,id,code,cost,qty;
        public MyPendingHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.listview_product_name);
            id=itemView.findViewById(R.id.liss);
            code=itemView.findViewById(R.id.code);
            cost=itemView.findViewById(R.id.amount);
            qty=itemView.findViewById(R.id.qut);
        }
    }

}
