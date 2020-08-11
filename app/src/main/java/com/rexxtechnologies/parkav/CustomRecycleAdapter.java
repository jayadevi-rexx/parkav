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

import static android.content.Context.MODE_PRIVATE;

class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.MyPendingHolder> implements View.OnClickListener {
    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<>();
    String id_value,id_name;
    private MyInterface listener;

    public CustomRecycleAdapter(Complaints context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
//        listener = foo;
    }

   /* public CustomRecycleAdapter(Complaints context, ArrayList<HashMap<String, String>> arraylist, void foo) {
    }*/

    /*public CustomRecycleAdapter(Complaints context, ArrayList<HashMap<String, String>> arraylist, void foo) {
    }*/

    @NonNull
    @Override
    public MyPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.invoice_custom_list,parent,false);
        CustomRecycleAdapter.MyPendingHolder myHolder=new CustomRecycleAdapter.MyPendingHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPendingHolder holder, int position) {
        final SharedPreferences sharedPreferences=context.getSharedPreferences("return",MODE_PRIVATE);
        resultp=data.get(position);
        holder.name.setText(resultp.get("Customer_name"));
        holder.id.setText(resultp.get("id"));
//        id_value=resultp.get("id");
      /*  id_value=holder.id.getText().toString();
        id_name=holder.name.getText().toString();
        sharedPreferences.edit().putString("customer_id",id_value).apply();
        sharedPreferences.edit().putString("customer_name",id_name).apply();*/

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_value=holder.id.getText().toString();
                id_name=holder.name.getText().toString();
//                id_value=resultp.get("id");
                sharedPreferences.edit().putString("customer_id",id_value).apply();
                sharedPreferences.edit().putString("customer_name",id_name).apply();
//                Toast.makeText(view.getContext(),id_value,Toast.LENGTH_SHORT).show();
                ((Complaints)context).foo(id_value,id_name);
                /*if (!id_value.isEmpty()&&!id_name.isEmpty()){

                }*/

//                listener.foo();
                /*Intent intent=new Intent(context,Complaints.class);
                context.startActivity(intent);*/
            }
        });
        /*holder.id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),id_value,Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View view) {
//        Toast.makeText(view.getContext(),id_value,Toast.LENGTH_LONG).show();
//        ((Complaints)context).foo(id_value,id_value);
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