package com.rexxtechnologies.parkav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TodayTask extends AppCompatActivity {
    ArrayList<HashMap<String, String>> arraylist;
    private TodayTaskAdapter adapter;
    RecyclerView cusappoint;
    ImageView backbutton,profile;
    TextView timer;
    Button bootom_button;
    public static final String PREFS_NAME = "LoginPrefs1";
    String employee_id;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_today_task);
        profile=findViewById(R.id.today_task_profile);
        timer=findViewById(R.id.today_task_timer);
        bootom_button=findViewById(R.id.dashboard_bootom_button);
        SharedPreferences set=getSharedPreferences(PREFS_NAME, 0);
        employee_id=set.getString("employee_id", "");
        backbutton=(ImageView) findViewById(R.id.img_Toolbar_menu);
        Thread myThread = null;

        Runnable myRunnableThread = new TodayTask.CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();

            }
        });
        cusappoint=(RecyclerView) findViewById(R.id.cusrecycle);
        TodayTaskAssignment();
        bootom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow();

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popupProfile();

            }
        });
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
    private void TodayTaskAssignment() {
        showProgressDialog();
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, URLs.URL_GETCUSTOMERSLIST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject obj = new JSONObject(response);
                    arraylist = new ArrayList<HashMap<String, String>>();
                    String status = obj.getString("status");
                    if(status.equals("true")){
                        String msg = obj.getString("message");

                        JSONArray jArray = obj.getJSONArray("data");
                        int length = jArray.length();
                        for(int i=0;i<length;i++) {
                            HashMap<String, String> map = new HashMap<String, String>();

                            JSONObject obj1 = jArray.getJSONObject(i);
                            map.put("Customer_name", obj1.getString("name"));
                            map.put("phone_number", obj1.getString("phone_number"));
                            map.put("address", obj1.getString("address"));
                            map.put("status","progressing");
                            arraylist.add(map);

                        }
                        adapter = new TodayTaskAdapter(TodayTask.this, arraylist);
                        cusappoint.setAdapter(adapter);
                        cusappoint.setLayoutManager(new LinearLayoutManager(TodayTask.this));
                        dismissProgressDialog();
                    }
                    else {
                        Toast.makeText(TodayTask.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TodayTask.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TodayTask.this, Dashboard.class));
        finish();
    }
    private void showProgressDialog() {
//        ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog=new ProgressDialog(TodayTask.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_layout);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(3000);

    }
    private void dismissProgressDialog(){

        progressDialog.dismiss();
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
                SharedPrefManager.getInstance(TodayTask.this).logout();
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
                popupWindow.dismiss();


            }
        });
        popupWindow.getContentView().findViewById(R.id.popup_complan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Dashboard.class));

            }
        });
        popupWindow.getContentView().findViewById(R.id.popup_dissmiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

}