package com.rexxtechnologies.parkav;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DeliveryCollectionCutomerNameAdapter extends RecyclerView.Adapter<DeliveryCollectionCutomerNameAdapter.MyPendingHolder> {
    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<>();
    String id_value,id_name;


    public DeliveryCollectionCutomerNameAdapter(DeliveryCollection context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @NonNull
    @Override
    public MyPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.invoice_custom_list,parent,false);
        DeliveryCollectionCutomerNameAdapter.MyPendingHolder myHolder=new DeliveryCollectionCutomerNameAdapter.MyPendingHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPendingHolder holder, int position) {
        final SharedPreferences sharedPreferences=context.getSharedPreferences("return",MODE_PRIVATE);
        resultp=data.get(position);
        holder.name.setText(resultp.get("Customer_name"));
        holder.id.setText(resultp.get("id"));

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyPendingHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView id;
        public MyPendingHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.listview_product_name);
            id=itemView.findViewById(R.id.liss);
        }
    }
}
