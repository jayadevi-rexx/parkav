package com.rexxtechnologies.parkav;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryCollection extends AppCompatActivity {
    Button fromdate,todate;
    Button delivenable,unvisitdisable;
    TextView fromdateanswer,todateanswer;
    String from,to;
    DatePickerDialog datepicker;
    String Employee_id;
    public static final String PREFS_NAME = "LoginPrefs1";
    Spinner spinner;
    List<String> CustomerName;
    ArrayAdapter<String> dataAdapter;
    ProgressDialog progressDialog;
    private DeliveryCollectionCutomerNameAdapter adapter;
    private DeliveryAdapter adapter2;
    ImageView backbutton;
    ArrayList<HashMap<String, String>> arraylist=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> arraylist2=new ArrayList<HashMap<String, String>>();
    AlertDialog dialog;
    TextView choose;
    RecyclerView rv,d_recycler;
    String customer_name,getId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delivery_collection);
        SharedPreferences set=getSharedPreferences(PREFS_NAME, 0);
        Employee_id=set.getString("employee_id", "");
        delivenable=(Button)findViewById(R.id.delivenable);
        unvisitdisable=(Button)findViewById(R.id.unvisitdisable);
        choose=findViewById(R.id.deliverchoosecustomer);
//        spinner=(Spinner)findViewById(R.id.customerlists);
        CustomerName=new ArrayList<>();
        Deliverycollectionspinner();
        fromdate=(Button)findViewById(R.id.fromdate);
        todate=(Button)findViewById(R.id.todate);
        fromdateanswer=(TextView)findViewById(R.id.fromdateanswer);
        todateanswer=(TextView)findViewById(R.id.todateanswer);
        backbutton=(ImageView) findViewById(R.id.img_Toolbar_menu);
        d_recycler=findViewById(R.id.deliveryrecycle);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        unvisitdisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UnVisitedShop.class));
            }
        });
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(DeliveryCollection.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fromdateanswer.setText(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth);
                            }
                        }, year, month, day);
                datepicker.show();

            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(DeliveryCollection.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                todateanswer.setText(year+"-"+(monthOfYear + 1)+"-"+dayOfMonth);
                                getList();
                            }
                        }, year, month, day);
                datepicker.show();

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 /*LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);*/
                AlertDialog.Builder alertDialog = new
                        AlertDialog.Builder(DeliveryCollection.this);
                View popupView = getLayoutInflater().inflate(R.layout.custom_recycler, null);
                /*popupWindow = new PopupWindow(popupView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);*/
                //  popupView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                rv=popupView.findViewById(R.id.recycle);
                adapter = new DeliveryCollectionCutomerNameAdapter(DeliveryCollection.this, arraylist);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(DeliveryCollection.this));
//                popupWindow.showAsDropDown(chose, 20, -5);
                alertDialog.setView(popupView);
                dialog = alertDialog.create();
                dialog.show();
            }
        });
    }

    private void Deliverycollectionspinner() {

//        showProgressDialog();
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, URLs.URL_GETCUSTOMERSLIST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("true")){

                        JSONArray jArray = jsonObject.getJSONArray("data");
                        int length = jArray.length();
                        for(int i=0;i<length;i++) {
                            JSONObject obj1 = jArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("Customer_name", obj1.getString("name"));
                            map.put("id", String.valueOf(obj1.getInt("id")));
                            arraylist.add(map);
                        }
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliveryCollection.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("employee_id", Employee_id);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    @Override
    public void onBackPressed() {

        startActivity(new Intent(DeliveryCollection.this, Dashboard.class));
        finish();

    }
    private void showProgressDialog() {

        progressDialog=new ProgressDialog(DeliveryCollection.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);

    }
    public void foo(String id_value, String name) {
        getId=id_value;
        dialog.dismiss();
    }

    private void getList() {
        from=fromdateanswer.getText().toString();
        to=todateanswer.getText().toString();
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, URLs.URL_GET_COLLECTION_REPORT, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if(status.equals("true")){

                        JSONArray jArray = jsonObject.getJSONArray("data");
                        int length = jArray.length();
                        for(int i=0;i<length;i++) {
                            JSONObject obj1 = jArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("customer_name", obj1.getString("customer_name"));
                            map.put("invoice_no", obj1.getString("invoice_no"));
                            map.put("amount", String.valueOf(obj1.getInt("amount")));
//                            map.put("total", String.valueOf(obj1.getInt("total")));
                            arraylist2.add(map);
                        }
                        adapter2=new DeliveryAdapter(DeliveryCollection.this,arraylist2);
                        d_recycler.setAdapter(adapter2);
                        d_recycler.setLayoutManager(new LinearLayoutManager(DeliveryCollection.this));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliveryCollection.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("employee_id", Employee_id);
                params.put("from_date", from);
                params.put("to_date", to);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}