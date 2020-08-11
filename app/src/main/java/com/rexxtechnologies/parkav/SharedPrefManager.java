package com.rexxtechnologies.parkav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String CUSTOMERID = "customer_id";
    private static final String UPDATEDBY= "updated_by";
    private static final String KEY_PHONENUMBER="phone_number";
    private static final String KEY_DEPARTMENT_ID="department_id";
    private static final String KEY_ZONE_ID="zone_id";
    private static final String KEY_ADDRESS="address";
    private static final String KEY_CITY="city";
    private static final String KEY_COUNTRY="country";
    public  static final String KEY_PASSWORD="password";
    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USERNAME, user.getUser_name());
        editor.putString(KEY_PASSWORD,user.getPassword());
        editor.putString(KEY_EMAIL, user.getPassword());
        editor.putString(CUSTOMERID, user.getCustomer_id());
        editor.putString(UPDATEDBY, user.getUpdated_by());
        editor.putString(KEY_PHONENUMBER,user.getPhone_number());
        editor.putString(KEY_DEPARTMENT_ID,user.getDepartmentId());
        editor.putString(KEY_ZONE_ID,user.getZone_id());
        editor.putString(KEY_ADDRESS,user.getAddress());
        editor.putString(KEY_CITY,user.getCity());
        editor.putString(KEY_COUNTRY,user.getCountry());
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }
    public String isCustomerId(){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String customer_id=sharedPreferences.getString(CUSTOMERID,null);
        return customer_id;
    }
    public String isUpdatedBy(){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String updated_by=sharedPreferences.getString(UPDATEDBY,null);
        return updated_by;
    }
    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(

                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASSWORD,null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(CUSTOMERID,null),
                sharedPreferences.getString(UPDATEDBY,null),
                sharedPreferences.getString(KEY_PHONENUMBER,null),

                sharedPreferences.getString(KEY_DEPARTMENT_ID,null),
                sharedPreferences.getString(KEY_ZONE_ID,null),
                sharedPreferences.getString(KEY_ADDRESS,null),
                sharedPreferences.getString(KEY_CITY,null),
                sharedPreferences.getString(KEY_COUNTRY,null)


        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, Login.class));
    }

    public void checkin(){

    }
}

