package com.rexxtechnologies.parkav;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Closed extends AppCompatActivity {
    String customer_id,updated_by="1",remarks;
    ImageView captured_image,open_camera;
    Button submit;
    private static final int pic_id = 123;
    ImageView profile;
    Button bootom_button;
    Bitmap bitmap;
    EditText remark;
    ImageView backbutton;
    String employee_name,employee_id;
    EditText eremark;
    String rootUrl,imageUri,customer_name,image;
    String date;
    public static final String PREFS_NAME = "LoginPrefs1";
    Uri imagePath;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_closed);
        submit=findViewById(R.id.close_submit);
        captured_image=findViewById(R.id.closed_camera);
        open_camera=findViewById(R.id.closed_camera_open);
        eremark=findViewById(R.id.closed_remarks);
        SharedPreferences set=getSharedPreferences(PREFS_NAME, 0);
        employee_name=set.getString("employee_name", "");
        employee_id=set.getString("employee_id"," ");
        rootUrl="//marketing.rexxtechnologies.com/parkav/";
        customer_id=getIntent().getExtras().getString("id");
        customer_name=getIntent().getExtras().getString("name");
        profile=findViewById(R.id.imageNotification);
        bootom_button=findViewById(R.id.dashboard_bootom_button);
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
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                remarks=eremark.getText().toString();
                showProgressDialog();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SHOPCLOSED, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            String msg = obj.getString("message");
                            if (status.equals("true")){
                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Dashboard.class));
                            }

                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        params.put("customer_id", customer_id);
                        params.put("employee_id", employee_id);
                        params.put("image", image);
                        params.put("remarks", remarks);
                        return params;
                    }
                };

                VolleySingleton.getInstance(Closed.this).addToRequestQueue(stringRequest);
            }
        });
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera_intent, pic_id);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        captured_image.setImageBitmap(photo);
        open_camera.setVisibility(View.INVISIBLE);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG,64,byteArrayOutputStream);
        File destination = new File(Environment.getExternalStorageDirectory(),System.currentTimeMillis() + ".png");
        System.out.println("FILE LOCATION"+destination);
        image=destination.toString();
        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(byteArrayOutputStream.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   private void showProgressDialog() {

       progressDialog=new ProgressDialog(Closed.this);
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
                SharedPrefManager.getInstance(Closed.this).logout();
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
                popupWindow.dismiss();
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
}
