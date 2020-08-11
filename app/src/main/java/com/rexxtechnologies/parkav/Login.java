package com.rexxtechnologies.parkav;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnlogin;
    EditText etName, etPassword;
    ProgressDialog progressDialog;
    SharedPreferences sp;

    private BroadcastReceiver MyReceiver = null;
    String user_role,user_id;
    public static final String PREFS_NAME = "LoginPrefs1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, Dashboard.class));
        }
        sp = getSharedPreferences("checkin",MODE_PRIVATE);


        etName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etUserPassword);



        //calling the method userLogin() for login the user
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    userLogin();

                } else {
                    Toast.makeText(getApplicationContext(), "Please check the Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    private void userLogin() {
        //first getting the values
        final String username = etName.getText().toString();
        final String password = etPassword.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(username)) {
            etName.setError("Please enter your username");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }
        showProgressDialog();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN, new com.android.volley.Response.Listener<String>() {
            @Override

            public void onResponse(String response) {


                try {

                    JSONObject obj = new JSONObject(response);

                    String status = obj.getString("status");
                    if (status.equals("true")){
                        String msg = obj.getString("message");
                        String role = obj.getString("role");
                        JSONArray jArray = obj.getJSONArray("data");
                        int length = jArray.length();
                        for(int i=0;i<length;i++) {
                            JSONObject obj1 = jArray.getJSONObject(i);
                            String id = obj1.getString("id");
                            int is_active=obj1.getInt("is_active");
                            String name = obj1.getString("name");
                            String email = obj1.getString("email");
                            String phone_number = obj1.getString("phone_number");
                            String department_id = obj1.getString("department_id");
                            String zone_id = obj1.getString("zone_id");
                            String image = obj1.getString("image");
                            String address = obj1.getString("address");
                            String city = obj1.getString("city");
                            String country = obj1.getString("country");



                            User user = new User(

                                    obj1.getString("name"),
                                    obj1.getString("password"),
                                    obj1.getString("id"),
                                    obj.getString("role"),
                                    obj1.getString("email"),

                                    obj1.getString("phone_number"),
                                    obj1.getString("department_id"),
                                    obj1.getString("zone_id"),
                                    obj1.getString("address"),
                                    obj1.getString("city"),
                                    obj1.getString("country")
                            );

                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("employee_id",id);
                            editor.putString("employee_role",role);
                            editor.putString("employee_name",name);
                            editor.putString("employee_email",email);
                            editor.putString("employee_phone_number",phone_number);
                            editor.putString("employee_department_id",department_id);
                            editor.putString("employee_zoneid",zone_id);
                            editor.putString("employee_address",address);
                            editor.putString("employee_city",city);
                            editor.putString("employee_country",country);
                            editor.apply();
                            finish();
                        }
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                    }
                    else if (status.equals("false")) {
                        Toast.makeText(getApplicationContext(), "Username or password is mismatched!", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private void showProgressDialog() {

     progressDialog=new ProgressDialog(Login.this);
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
    public void goToMainActivity(){
        Intent i = new Intent(this,Dashboard.class);
        startActivity(i);
    }
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}


