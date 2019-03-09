package com.example.e5322.thyrosoft.startscreen;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {
    private static String TAG = SplashScreen.class.getSimpleName();
    AlertDialog alert;
    public static String str_login_test, profile_test;
    String version;
    private int versionCode = 0;
    public static SharedPreferences sh, profile;
    public static SharedPreferences.Editor editor, editor1;
    Boolean isInternetPresent = false;  // flag for Internet connection status
    ConnectionDetector cd;   // Connection detector class
    String z = "";
    private String ApkUrl;
    Animation anim;
    String version1;
    String response1;
    String resId;
    ImageView iv;
    private GlobalClass globalClass;
    private boolean firstRunSplash;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_screen);
        anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.translate);
        iv = (ImageView) findViewById(R.id.logo);

        Fabric.with(this, new Crashlytics());

        if (globalClass.checkForApi21()) {    Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));}

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        firstRunSplash = p.getBoolean("PREFERENCE_FIRSTSPLASH_RUN", false);

        if (firstRunSplash == false) {
            p.edit().putBoolean("PREFERENCE_FIRSTSPLASH_RUN", true).commit();
            clearApplicationData();
        }else {
            Log.e(TAG, "deleteDir: cache is not cleraed " );
        }
        getversion();
        myprif();
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //get the app version Name for display
        version = pInfo.versionName;
        //get the app version Code for checking
        versionCode = pInfo.versionCode;
        //display the current version in a TextView
        TextView versionText = (TextView) findViewById(R.id.version);
        versionText.setText(version);
        cd = new ConnectionDetector(getApplicationContext());

        if (!isNetworkAvailable()) {

            SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
            String user = prefs.getString("Username", null);
            String passwrd = prefs.getString("password", null);
            String access = prefs.getString("ACCESS_TYPE", null);
            String api_key = prefs.getString("API_KEY", null);
            if (user != null && passwrd != null) {
                Intent prefe = new Intent(SplashScreen.this, ManagingTabsActivity.class);
                startActivity(prefe);
                finish();
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Set the Alert Dialog Message
                builder.setMessage(ToastFile.intConnection)
                        .setCancelable(false)
                        .setPositiveButton("Retry",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        // Restart the Activity
                                        Intent intent = getIntent();
                                        finish();
                                        startActivity(intent);
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

            // Create an Alert Dialog

        }

    }

    public void clearApplicationData() {

//        Log.e(TAG, "<< Clear App Data >>");
        File cacheDirectory = getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }
        return deletedAll;
    }

    private void clearAppData() {
//        Log.e(TAG, "<< Clear App Data >>");
        try {
// clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear " + packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void getversion() {
        RequestQueue requestQueuepoptestILS = Volley.newRequestQueue(SplashScreen.this);
        JsonObjectRequest jsonObjectRequestPop = new JsonObjectRequest(Request.Method.GET, Api.checkVersion, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: response" + response);
                    Log.e(TAG, "RESPONSE" + String.valueOf(response));
                    version1 = response.getString("Version");
                    ApkUrl = response.getString("url");
                    response1 = response.getString("response");
                    resId = response.getString("resId");

                    if (version1 != null) {
                        Log.e(TAG, "onResponse version1: " + version1);
                        iv.startAnimation(anim);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                String newVersion = version1.replace(".", "");//api
                                String preversion = version.replace(".", "");//package

                                int intApiVersion = Integer.parseInt(newVersion);//api
                                int intPkgversion = Integer.parseInt(preversion);//pkg
                                //46>47intNewVersion
                                if (intPkgversion >= intApiVersion) {//intPreversion<intNewVersion

                                    SharedPreferences prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
                                    String user = prefs.getString("Username", null);
                                    String passwrd = prefs.getString("password", null);
                                    String access = prefs.getString("ACCESS_TYPE", null);
                                    String api_key = prefs.getString("API_KEY", null);
                                    if (user != null && passwrd != null) {
                                        Intent prefe = new Intent(SplashScreen.this, ManagingTabsActivity.class);
                                        startActivity(prefe);
                                        finish();
                                    } else {
                                        Intent i = new Intent(SplashScreen.this, Login.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } else {
                                    //oh yeah we do need an upgrade, let the user know send an alert message
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                                    builder.setCancelable(false);
                                    builder.setMessage(ToastFile.newer_version)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                //if the user agrees to upgrade
                                                public void onClick(DialogInterface dialog, int id) {

                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.charbi.com/CDN/Applications/Android/ThyrosoftLite.Apk"));
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                                public void onClick(DialogInterface dialog, int id) {
                                                    finishAffinity();
                                                }
                                            });
                                    builder.create().show();
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });
        requestQueuepoptestILS.add(jsonObjectRequestPop);
        Log.e(TAG, "getversion URL: " + jsonObjectRequestPop);

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void myprif() {

    }


}

