package com.rexxtechnologies.parkav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Punch extends AppCompatActivity {
    TextView timer, txtLocationResult,punch_timer;
    Button Done;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    String passValue,date;
    SharedPreferences sp;
    ImageView profile;

    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private TextView mLatitudeText;
    private TextView mLongitudeText;
    private FusedLocationProviderClient mFusedLocationClient;
    Button bootom_button;
    ImageView backbutton;
    private static final String TAG = Punch.class.getSimpleName();
    public static final String PREFS_NAME = "LoginPrefs1";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    String customer_id, updated_by, lat, lng, checkin_time, role, id,employee_id;
    User user;
    PopupWindow popupWindow;

//    SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_punch);
        timer = findViewById(R.id.timers);
        punch_timer = findViewById(R.id.punch_timer);
        Done = findViewById(R.id.done);
        backbutton = findViewById(R.id.img_Toolbar_menu);
        txtLocationResult = findViewById(R.id.__hey___employee_name);
        bootom_button = findViewById(R.id.punch_bootom_button);
        profile=findViewById(R.id.punch_profile);
        SharedPreferences set=getSharedPreferences(PREFS_NAME, 0);
        employee_id=set.getString("employee_id", "");
        sp = getSharedPreferences("result", MODE_PRIVATE);
        id = SharedPrefManager.getInstance(this).isCustomerId();
//        popup();
        mLongitudeLabel = "longitude";
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        txtLocationResult=findViewById(R.id.__hey___employee_name);
        sp = getSharedPreferences("result", MODE_PRIVATE);
        /*Thread myThread = null;

        Runnable myRunnableThread = new Punch.CountDownRunner();
        myThread = new Thread(myRunnableThread);
        myThread.start();*/
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("result", "timer").apply();
                sp.edit().putLong("timevalue", startTime).apply();
                sp.edit().putString("date", date).apply();
                Intent passTime = new Intent(Punch.this, Dashboard.class);
                startActivity(passTime);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupProfile();
            }
        });
        bootom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow();

            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Punch.this, Dashboard.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkin_page, menu);
        return true;
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
                SharedPrefManager.getInstance(Punch.this).logout();
            }
        });
    }
    private void popup() {
        final CharSequence[] options = {"New Customer", "Exit Customer"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Punch.this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals("New Customer")) {
                    Intent intent = new Intent(Punch.this, AddCustomer.class);
                    startActivity(intent);

                } else if (options[item].equals("Existing Customer")) {

                    dialogInterface.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    private void checkInmethod() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CHECKIN, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    String status = obj.getString("status");
                    if (status.equals("true")) {
                        sp.edit().putString("result", "timer").apply();
                        Intent passTime = new Intent(Punch.this, Dashboard.class);
                        startActivity(passTime);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", customer_id);
                params.put("updated_by", updated_by);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("date", date);
                params.put("checkin_time", checkin_time);
                return params;
            }
        };
        VolleySingleton.getInstance(Punch.this).addToRequestQueue(stringRequest);

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
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
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
        ActivityCompat.requestPermissions(Punch.this,
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

                           /* mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLatitudeLabel,
                                    mLastLocation.getLatitude()));
                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",
                                    mLongitudeLabel,
                                    mLastLocation.getLongitude()));*/
                            /*txtLocationResult.setText(
                                    mLastLocation.getLatitude()+":"+
                                    mLastLocation.getLongitude());*/
                            lat = String.valueOf(mLastLocation.getLatitude());
                            lng = String.valueOf(mLastLocation.getLongitude());
                            Time today = new Time(Time.getCurrentTimezone());
                            today.setToNow();
                            txtLocationResult.setText(today.format("%k:%M:%S"));
                            checkin_time = today.format("%k:%M:%S");
                            customer_id = "6";
                            updated_by = "user";

                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());

                        }
                    }
                });
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hour=mins/60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timer.setText("" + hour+ ":" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };
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
                    punch_timer.setText(curTime);
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
