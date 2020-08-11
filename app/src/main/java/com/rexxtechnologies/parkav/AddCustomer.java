package com.rexxtechnologies.parkav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCustomer extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    //    View submit;
    Spinner spinner;
    // String URL="http://techiesatish.com/demo_api/spinner.php";
//    ArrayList<String> ZoneName;
    List<String> ZoneName;
    String Zone;
    ImageView backbutton, profile;
    Button bootom_button;
    ArrayAdapter<String> dataAdapter;
    TextView zone_name, timer;
    Button submit;
    ImageButton zone_list;
    EditText ename, ecompany_name, eemail, ephone_number, ealternative_numver, etax_no, eaddress, ecity, estate, epostal_code, ecountry;
    String balance_limit = "0", customer_group_id = "1", zone_id, lat, lng, name, company_name, email, phone_number, alternative_number, tax_no, address, city, state, postal_code, country;
    View progress;
    protected Location mLastLocation;
    ProgressDialog progressDialog;
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final String TAG = Punch.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        profile = findViewById(R.id.imageNotification);
        bootom_button = findViewById(R.id.dashboard_bootom_button);
        ZoneName = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinner);
        ename = findViewById(R.id.txtIsbn);
        ecompany_name = findViewById(R.id.companyname);
        eemail = findViewById(R.id.email);
        ephone_number = findViewById(R.id.contact_number);
        ealternative_numver = findViewById(R.id.alternative_number);
        etax_no = findViewById(R.id.gstnumber);
        eaddress = findViewById(R.id.address);
        ecity = findViewById(R.id.city);
        estate = findViewById(R.id.state);
        epostal_code = findViewById(R.id.postalcode);
        ecountry = findViewById(R.id.country);
        submit = findViewById(R.id.rectangle_88);
        timer = findViewById(R.id.add_customer_timer);
        mLatitudeLabel = "latitude";
        mLongitudeLabel = "longitude";
        Thread myThread = null;
        Runnable myRunnableThread = new AddCustomer.CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();
        backbutton = (ImageView) findViewById(R.id.img_Toolbar_menu);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        loadSpinnerData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected()) {
                    addCustomer();

                } else {
                    Toast.makeText(getApplicationContext(), "Please check the Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void addCustomer() {
        name = ename.getText().toString();
        company_name = ecompany_name.getText().toString();
        email = eemail.getText().toString();
        phone_number = ephone_number.getText().toString();
        alternative_number = ealternative_numver.getText().toString();
        tax_no = etax_no.getText().toString();
        address = eaddress.getText().toString();
        city = ecity.getText().toString();
        state = estate.getText().toString();
        postal_code = epostal_code.getText().toString();
        country = ecountry.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ename.setError("Please enter customername");
            ename.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(company_name)) {
            ecompany_name.setError("Please enter companyname");
            ecompany_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            eemail.setError("Please enter email");
            eemail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone_number)) {
            ephone_number.setError("Please enter phone number");
            ephone_number.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(alternative_number)) {
            ealternative_numver.setError("Please enter Alternate number");
            ealternative_numver.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(tax_no)) {
            etax_no.setError("Please enter Tax no");
            etax_no.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            eaddress.setError("Please enter customer address");
            eaddress.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(city)) {
            ecity.setError("Please enter city");
            ecity.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(state)) {
            estate.setError("Please enter state");
            estate.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(postal_code)) {
            epostal_code.setError("Please enter postal code");
            epostal_code.requestFocus();
            return;
        }

        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ADD_CUSTOMER, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);

                try {

                    JSONObject obj = new JSONObject(response);

                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equals("true")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    } else if (status.equals("false")) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_group_id", customer_group_id);
                params.put("name", name);
                params.put("company_name", company_name);
                params.put("email", email);
                params.put("phone_number", phone_number);
                params.put("alternative_number", alternative_number);
                params.put("balance_limit", balance_limit);
                params.put("tax_no", tax_no);
                params.put("zone_id", zone_id);
                params.put("address", address);
                params.put("city", city);
                params.put("state", state);
                params.put("postal_code", postal_code);
                params.put("country", country);
                params.put("lat", lat);
                params.put("lng", lng);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }


    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {

                showSnackbar(R.string.textwarn, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(AddCustomer.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();


                            lat = String.valueOf(mLastLocation.getLatitude());
                            lng = String.valueOf(mLastLocation.getLongitude());
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());

                        }
                    }
                });
    }


    private void loadSpinnerData() {
        showProgressDialog();
        // RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_GETZONE_LIST, new Response.Listener<String>() {
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
                            JSONObject obj1 = jArray.getJSONObject(i);
                            String id=obj1.getString("id");
                            String zone=obj1.getString("name");
                            zone_id=id;
                            ZoneName.add(zone);
                        }
//
                        dataAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,ZoneName);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(dataAdapter);
                        dismissProgressDialog();
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
    private void showProgressDialog() {
//        ProgressDialog progressDialog=new ProgressDialog(getApplicationContext());
        progressDialog=new ProgressDialog(AddCustomer.this);
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
                SharedPrefManager.getInstance(AddCustomer.this).logout();
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
    }

