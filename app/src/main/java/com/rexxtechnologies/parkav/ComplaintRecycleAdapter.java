package com.rexxtechnologies.parkav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ComplaintRecycleAdapter extends RecyclerView.Adapter<ComplaintRecycleAdapter.MyPendingHolder> {
    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<>();
    String id,name,state;
    Boolean checked;
    public ComplaintRecycleAdapter(Complaints context, ArrayList<HashMap<String, String>> arraylist2) {
        this.context = context;
        data = arraylist2;
    }

    @NonNull
    @Override
    public MyPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.productlistcustom,parent,false);
        ComplaintRecycleAdapter.MyPendingHolder myHolder=new ComplaintRecycleAdapter.MyPendingHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPendingHolder holder, int position) {
        resultp=data.get(position);
        holder.name.setText(resultp.get("product_name"));
        holder.qty.setText(resultp.get("qty"));
        holder.price.setText(resultp.get("net_unit_price"));
        holder.total.setText(resultp.get("total"));
//        holder.checkBox.setChecked(true);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()){
                    id=resultp.get("id");
                    name=holder.name.getText().toString();
                    state=holder.total.getText().toString();
                    ((Complaints)context).foo(id);
//                    ((Complaints)context).foo(name);
//                    Toast.makeText(context,id+"  "+name+"  "+state,Toast.LENGTH_SHORT).show();
                }

                holder.checkBox.setEnabled(false);

            }
        });

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyPendingHolder extends RecyclerView.ViewHolder {
        TextView name,qty,price,total;
        CheckBox checkBox;
        public MyPendingHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.product_name);
            qty=itemView.findViewById(R.id.product_qty);
            price=itemView.findViewById(R.id.product_price);
            total=itemView.findViewById(R.id.product_total);
            checkBox=itemView.findViewById(R.id.checkBox);
        }
    }
}
