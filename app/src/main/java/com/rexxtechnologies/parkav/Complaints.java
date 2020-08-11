package com.rexxtechnologies.parkav;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Complaints extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView profile,back_button;
    Spinner complaint_spinner;
    TextView timer;
    Button buttom,submit;
    EditText postal_code,eremarks;
    Spinner customer_list;
    LinearLayout visi;
    ArrayAdapter<String> dataAdapter;
    SharedPreferences sp;
    String employee_id,customer_id,productName,qunty,prices,total,name,customer_name,getId,getproductId,complaint_status,kg_size,remarks;
    List<String> getCustomer_list_name;
    List<String> getCustomer_list_id;
    TextView chose,c_name,pos_code,kg;
    RecyclerView rv,comp_recycle;
    private CustomRecycleAdapter adapter;
    private ComplaintRecycleAdapter adapter2;
    ArrayList<HashMap<String, String>> arraylist=new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> arraylist2=new ArrayList<HashMap<String, String>>();
//    String[] data = { "DATA 1", "DATA 2", "DATA 3", "DATA 4", "DATA 5", "DATA 6" };
String[] status = { "Pricing", "Return"};
    SharedPreferences sharedPreferences;
    PopupWindow popupWindow;
    AlertDialog dialog;

    List<String> product_id;
    public static final String PREFS_NAME = "LoginPrefs1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        getCustomer_list_name=new ArrayList<>();
        getCustomer_list_id=new ArrayList<>();
        product_id= new ArrayList<>();
        sharedPreferences=getSharedPreferences("return",MODE_PRIVATE);
//        customer_list=findViewById(R.id.compliaint_spinner);
        chose=findViewById(R.id.choose_customer);
        c_name=findViewById(R.id.cmoplant_customer_name);
//        c_name=findViewById(R.id.cmoplant_customer_name);
        comp_recycle=findViewById(R.id.complaint_recycle);
        back_button=findViewById(R.id.complaint_back_button);
        profile=findViewById(R.id.complaint_profile);
        timer=findViewById(R.id.complaint_timer);
        buttom=findViewById(R.id.complaint_bootom_button);
        submit=findViewById(R.id.complaint_submit);
        postal_code=findViewById(R.id.postalcode);
        eremarks=findViewById(R.id.coremarks);
        visi=findViewById(R.id.kg_size);
        complaint_spinner=findViewById(R.id.compliaint_spinner);
        pos_code=findViewById(R.id.postalcode_text);
        kg=findViewById(R.id.kg);
        SharedPreferences set=getSharedPreferences(PREFS_NAME, 0);
        employee_id=set.getString("employee_id", "");
        Thread myThread = null;
        Runnable myRunnableThread = new Complaints.CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();
        complaint_spinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,status);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        complaint_spinner.setAdapter(aa);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupProfile();
            }
        });
        buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComplaints();
//                Toast.makeText(getApplicationContext(),product_id.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        loadSpinnerData();
        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);*/
                AlertDialog.Builder alertDialog = new
                        AlertDialog.Builder(Complaints.this);
                View popupView = getLayoutInflater().inflate(R.layout.custom_recycler, null);
                /*popupWindow = new PopupWindow(popupView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,true);*/
                //  popupView.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                rv=popupView.findViewById(R.id.recycle);
                adapter = new CustomRecycleAdapter(Complaints.this, arraylist);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(Complaints.this));
//                popupWindow.showAsDropDown(chose, 20, -5);
                alertDialog.setView(popupView);
                dialog = alertDialog.create();
                dialog.show();
            }
        });

    }

    private void addComplaints() {
        if (complaint_status.equals("Pricing")){
            visi.setVisibility(View.INVISIBLE);
            kg_size="0";
        }
        else {
            kg_size=postal_code.getText().toString();
        }
        remarks=eremarks.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADDCOMPLAINTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
//                    arraylist = new ArrayList<HashMap<String, String>>();
                    String status = obj.getString("status");
                    if(status.equals("true")){
                        String msg = obj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //  Toast.makeText(TodayTask.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(TodayTask.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        dismissProgressDialog();
//                        pd.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("complaint_status", complaint_status);
                params.put("product_id", String.valueOf(product_id));
                params.put("kg_size", kg_size);
                params.put("remarks", remarks);
                params.put("employee_id", employee_id);
                //  params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(Complaints.this, Dashboard.class));
        finish();

    }
    private void popupProfile() {
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view1=layoutInflater.inflate(R.layout.popup_menu,null);
        int width = LinearLayout.LayoutParams.FILL_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(view1, width, height, focusable);
        popupWindow.showAtLocation(view1, Gravity.TOP, 0, 0);
        popupWindow.getContentView().findViewById(R.id.x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.getContentView().findViewById(R.id.top_pop_notificaion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        popupWindow.getContentView().findViewById(R.id.top_popup_report).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(),DeliveryCollection.class));

                    }
                });
        popupWindow.getContentView().findViewById(R.id.top_popup_signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(Complaints.this).logout();
            }
        });
    }
    private void popupWindow() {
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View view1=layoutInflater.inflate(R.layout.activity_navi,null);
        int width = LinearLayout.LayoutParams.FILL_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(view1, width, height, focusable);
        popupWindow.showAtLocation(view1, Gravity.BOTTOM, 0, 0);
        popupWindow.getContentView().findViewById(R.id.popup_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                //  Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_LONG).show();
            }
        });
        popupWindow.getContentView().findViewById(R.id.popup_addcustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),AddCustomer.class));

            }
        });
        popupWindow.getContentView().findViewById(R.id.popup_nearby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Nearby_shop.class));

            }
        });
        popupWindow.getContentView().findViewById(R.id.popup_today).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TodayTask.class));

            }
        });
        popupWindow.getContentView().findViewById(R.id.popup_complan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Complaints.class));

            }
        });
        popupWindow.getContentView().findViewById(R.id.popup_dissmiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }
    private void loadSpinnerData() {
        // RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETCUSTOMERSLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
//                    arraylist = new ArrayList<HashMap<String, String>>();
                    String status = obj.getString("status");
                    if(status.equals("true")){
                        String msg = obj.getString("message");

                        JSONArray jArray = obj.getJSONArray("data");
                        int length = jArray.length();
                        for(int i=0;i<length;i++) {
                            JSONObject obj1 = jArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();

//                            JSONObject obj1 = jArray.getJSONObject(i);
                            map.put("Customer_name", obj1.getString("name"));
//                            map.put("phone_number", obj1.getString("phone_number"));
//                            map.put("address", obj1.getString("address"));
//                            map.put("status",obj1.getString("expense"));
                            map.put("id", String.valueOf(obj1.getInt("id")));
                            arraylist.add(map);

                        }


//                        adapter=new CustomRecycleAdapter(Complaints.this,arraylist);
                    }
                    else {
                        //  Toast.makeText(TodayTask.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(TodayTask.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        dismissProgressDialog();
//                        pd.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("employee_id", employee_id);
                //  params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void getInvoiceList(String id) {
        arraylist2.clear();
        customer_id=id;
//        customer_id=sharedPreferences.getString("customer_id",null);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_GETINVOICELIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);

                    String status = obj.getString("status");
                    if(status.equals("true")){
                        String msg = obj.getString("message");

                        JSONArray jArray = obj.getJSONArray("data");
                        int length = jArray.length();
                        for(int i=0;i<length;i++) {
                            JSONObject obj1 = jArray.getJSONObject(i);
                            JSONArray jsonArray=obj1.getJSONArray("product_details");
                            int length2=jsonArray.length();
                            for (int j=0;j<length2;j++){
                                JSONObject obj2 = jsonArray.getJSONObject(j);
                                HashMap<String, String> map = new HashMap<String, String>();

//                            JSONObject obj1 = jArray.getJSONObject(i);
                                map.put("product_name", obj2.getString("product_name"));
                                map.put("qty", obj2.getString("qty"));
                                map.put("net_unit_price", obj2.getString("net_unit_price"));
                                map.put("total",obj2.getString("total"));
                                map.put("id", String.valueOf(obj2.getInt("product_id")));
                                arraylist2.add(map);
                            }

                        }
                        adapter2=new ComplaintRecycleAdapter(Complaints.this,arraylist2);
                        comp_recycle.setAdapter(adapter2);
                        comp_recycle.setLayoutManager(new LinearLayoutManager(Complaints.this));
//                        Toast.makeText(getApplicationContext(),productName+"/n"+qunty+"/n"+prices+"/n"+total,Toast.LENGTH_LONG).show();
                    }
                    else {
                        //  Toast.makeText(TodayTask.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  Toast.makeText(TodayTask.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        dismissProgressDialog();
//                        pd.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", customer_id);
                //  params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void foo(String id_value, String name) {
        getId=id_value;
        customer_name=name;
        if (customer_name==null)
        {
            c_name.setVisibility(View.INVISIBLE);
        }
        else {
            c_name.setVisibility(View.VISIBLE);
            c_name.setText(customer_name);
            getInvoiceList(getId);
        }
        dialog.dismiss();
    }
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    TextView txtCurrentTime = (TextView) findViewById(R.id.mtextTimer);
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    String curTime = hours + ":" + minutes + ":" + seconds;
                    timer.setText(curTime);
                    Date d2 = new Date( dt.getTime() + 10 * 60 * 60 * 1000 );

                    System.out.println(d2);
                    Toast.makeText(getApplicationContext(), (CharSequence) d2, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        complaint_status=status[i];
//        Toast.makeText(getApplicationContext(),complaint_status,Toast.LENGTH_SHORT).show();
        if (complaint_status.equals("Return")){
            visi.setVisibility(View.VISIBLE);
        }else if (complaint_status.equals("Pricing")){
            visi.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000); // Pause of 1 Second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
    public void foo(String value){
        getproductId=value;
        product_id.add(getproductId);
//        Toast.makeText(getApplicationContext(), (CharSequence) product_id,Toast.LENGTH_SHORT).show();
    }
}


