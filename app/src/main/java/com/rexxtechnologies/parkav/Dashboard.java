package com.rexxtechnologies.parkav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Dashboard extends AppCompatActivity {
    ImageView timer,profile;
    long timeValue;
    Chronometer timer_tv;
    Button today_task,nearbyshop;
    Button bootom_button,checkout;
    ImageView navi;
    private static Context ctx;
    String timerValue="",value,timerval,role,id,employee_name,employee_id,checkout_time,punch_date,valli;
    SharedPreferences sp,sp2,sp3,sp4,sp1;
//    int hour,min,sec;
    TextView employeename;
    private long startTime = 0L;
    int pStatus = 0;
    private Handler handler = new Handler();
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    long vallll;
    int secs;
    String date;
    public static final String PREFS_NAME = "LoginPrefs1";
    int d=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        employeename=(TextView)findViewById(R.id.employee_name);
        SharedPreferences set=getSharedPreferences(PREFS_NAME, 0);
        employee_name=set.getString("employee_name", "");
        employee_id=set.getString("employee_id", "");
        employeename.setText(employee_name);
        timer=findViewById(R.id.__page_punch__att_whi);
        timer_tv=findViewById(R.id.dashboard_timer);
        today_task=findViewById(R.id.dashboard_today_task);
        nearbyshop=findViewById(R.id.dashboard_nearbyshop);
        profile=findViewById(R.id.dashboard_profile);
        bootom_button=findViewById(R.id.dashboard_bootom_button);
        checkout=findViewById(R.id.dashboard_checkout);
        navi=findViewById(R.id.__page_dashboard___1__ic_view_headline_24px);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        sp = getSharedPreferences("result",MODE_PRIVATE);
        timerval=sp.getString("result",null);
        viewFunction();
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = SystemClock.elapsedRealtime();
                Intent i=new Intent(Dashboard.this,Punch.class);
                i.putExtra("role",role);
                i.putExtra("id",id);
                startActivity(i);
            }
        });
            today_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Dashboard.this,TodayTask.class);
                    startActivity(i);
                }
            });
            nearbyshop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(Dashboard.this, Nearby_shop.class);
                    startActivity(i);
                }
            });
            bootom_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow();

                }
            });
            navi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow();
                }
            });
            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkOut();
//                    Toast.makeText(getApplicationContext(),"Checked Out",Toast.LENGTH_SHORT).show();
                }
            });
              profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popupProfile();
            }
        });
    }

    private void viewFunction() {
        if (timerval==null){
            timer_tv.setVisibility(View.INVISIBLE);
            checkout.setVisibility(View.INVISIBLE);
            timer.setVisibility(View.VISIBLE);
        }
        else {
            timer_tv.setVisibility(View.VISIBLE);
            timer.setVisibility(View.INVISIBLE);
            checkout.setVisibility(View.INVISIBLE);
            timeValue=sp.getLong("timevalue",0);
            punch_date=sp.getString("date","");
            if (date.equals(punch_date)){
                vallll=sp.getLong("timeSwap",0);
                if (vallll==0){
                    TimerCount();
                }
                else {
                    valli=sp.getString("afterClickCheckedButton","");
                    if (valli.equals("afterClickCheckedButton")){
                        timer_tv.setVisibility(View.INVISIBLE);
                        checkout.setVisibility(View.INVISIBLE);
                        timer.setVisibility(View.VISIBLE);
                    }
                    else {
                        timer_tv.setVisibility(View.INVISIBLE);
                        timer.setVisibility(View.INVISIBLE);
                        checkout.setVisibility(View.VISIBLE);
                        handler.removeCallbacks(updateTimerThread);
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"You Already Checked Today",Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(updateTimerThread);
                timer_tv.setVisibility(View.INVISIBLE);
                checkout.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void checkOut() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        checkout_time = today.format("%k:%M:%S");
        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, URLs.URL_GETCUSTOMERSLIST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    if(status.equals("true")){
                        String msg = obj.getString("message");
                        timer_tv.setVisibility(View.INVISIBLE);
                        checkout.setVisibility(View.INVISIBLE);
                        timer.setVisibility(View.VISIBLE);
                        sp.edit().putString("afterClickCheckedButton","afterClickCheckedButton").apply();
                        Toast.makeText(Dashboard.this, msg+" "+date+" "+checkout_time+""+employee_id, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Dashboard.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date", date);
                params.put("employee_id", employee_id);
                params.put("checkout_time", checkout_time);
                //  params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void TimerCount() {
        customHandler.postDelayed(updateTimerThread, 0);
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
                SharedPrefManager.getInstance(Dashboard.this).logout();
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
                popupWindow.dismiss();
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

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - timeValue;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hour=mins/60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timer_tv.setText("" +  String.format("%02d", hour)+ ":" + String.format("%02d", mins)+ ":"
                    + String.format("%02d", secs));
            customHandler.postDelayed(this, 0);
            if (secs==30){
                timeSwapBuff += timeInMilliseconds;
                sp.edit().putLong("timeSwap",timeSwapBuff).apply();
                timer_tv.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.INVISIBLE);
                checkout.setVisibility(View.VISIBLE);
                handler.removeCallbacks(updateTimerThread);
            }
        }

    };
}

