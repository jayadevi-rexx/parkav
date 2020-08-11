package com.rexxtechnologies.parkav;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String customer_id,updated_by="1",remarks;
    String product_name,product_cost,product_code,product_qty,product_id;
    ImageView backbutton;
    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    List<String> CustomerName;
    TextView choose_product;
    EditText amount,quantity,code,remark;
    PopupWindow popupWindow;
    RecyclerView rv;
    private InvoiceRecyclerAdapter adapter;
    ArrayList<HashMap<String, String>> arraylist=new ArrayList<HashMap<String, String>>();
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_invoice);

        choose_product=findViewById(R.id.invoice_choose_product);
        amount=findViewById(R.id.amount);
        quantity=findViewById(R.id.quantity);
        code=findViewById(R.id.productcode);
        remark=findViewById(R.id.remarks);
        //customer_id=getIntent().getExtras().getString("id");
        backbutton=(ImageView) findViewById(R.id.img_Toolbar_menu);
        CustomerName=new ArrayList<>();
//        spinner=(Spinner)findViewById(R.id.productspinner);
        loadSpinnerData();
        choose_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
//                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                AlertDialog.Builder alertDialog = new
                        AlertDialog.Builder(InvoiceActivity.this);
                View popupView = getLayoutInflater().inflate(R.layout.invoice_custom_recycler, null);
              /*  popupWindow = new PopupWindow(popupView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);*/
                //  popupView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                rv=popupView.findViewById(R.id.invoice_recycler);
                adapter = new InvoiceRecyclerAdapter(InvoiceActivity.this, arraylist);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));
//                popupWindow.showAsDropDown(choose_product, 20, -5);
                alertDialog.setView(popupView);
                dialog = alertDialog.create();
                dialog.show();
            }
        });
        backbutton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

    }
    private void loadSpinnerData() {
        // RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_GETPRODUCTLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("true")){
                        //  String msg = jsonObject.getString("message");
                        JSONArray jArray = jsonObject.getJSONArray("data");
                        int length = jArray.length();
                        for(int i=0;i<length;i++) {
                            HashMap<String, String> map = new HashMap<String, String>();

                            JSONObject obj1 = jArray.getJSONObject(i);
                            map.put("Customer_name", obj1.getString("name"));
                            map.put("code", obj1.getString("code"));
                            map.put("cost", obj1.getString("cost"));
                            map.put("qty",obj1.getString("qty"));
                            map.put("id", String.valueOf(obj1.getInt("id")));
                            arraylist.add(map);

                            /*JSONObject obj1 = jArray.getJSONObject(i);

                            String name=obj1.getString("name");
                           // zone_id=id;
                            CustomerName.add(name);*/
                        }
//                        Log.e("msg",ZoneName.toString());
                        /*dataAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,CustomerName);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);*/

                    }


                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void foo(String id_name, String id_cost, String id_code, String id_qyt, String id_value){
        choose_product.setText(id_name);
        amount.setText(id_cost);
        code.setText(id_code);
        quantity.setText(id_qyt);
//        popupWindow.dismiss();
        dialog.dismiss();
    }
}