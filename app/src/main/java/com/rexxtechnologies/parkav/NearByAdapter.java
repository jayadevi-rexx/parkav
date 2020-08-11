package com.rexxtechnologies.parkav;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.MyPendingHolder> implements View.OnClickListener {
    Context context;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<>();
    public NearByAdapter(Nearby_shop context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @NonNull
    @Override
    public MyPendingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.activity_nearby_tasks,parent,false);
        NearByAdapter.MyPendingHolder myHolder=new NearByAdapter.MyPendingHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPendingHolder holder, int position) {
        resultp=data.get(position);
        holder.name.setText(resultp.get("Customer_name"));
        holder.time.setText(resultp.get("phone_number"));
        holder.contact.setText(resultp.get("address"));
        holder.doctor.setText(resultp.get("status"));
        final String id=resultp.get("id");
        holder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu=new PopupMenu(context,view);
                menu.inflate(R.menu.dropdown_menu_item);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.invoice:
                                Intent invoice=new Intent(context,AddProductsofInvoice.class);
                                invoice.putExtra("id",id);
                                context.startActivity(invoice);
                                break;
                            case R.id.closed:
                                //handle menu1 click
                                Intent closed=new Intent(context,Closed.class);
                                closed.putExtra("id",id);
                                closed.putExtra("name",resultp.get("Customer_name"));
                                context.startActivity(closed);
//                                context.startActivity(new Intent(context, Closed.class));
                                break;
                            case R.id.due:
                                //handle menu2 click
                                context.startActivity(new Intent(context, Closed.class));
                                break;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });
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
        TextView time;
        TextView contact;
        TextView doctor;
        ImageButton dropdown;
//        Spinner time;
        public MyPendingHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView2);
            time = (TextView) itemView.findViewById(R.id.textview5);
//            time = itemView.findViewById(R.id.spinner);
            contact = (TextView) itemView.findViewById(R.id.textView4);
            doctor = (TextView) itemView.findViewById(R.id.textView6);
            dropdown=itemView.findViewById(R.id.dropdown);
        }
    }
}
