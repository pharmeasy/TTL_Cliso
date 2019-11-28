package com.example.e5322.thyrosoft.startscreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.example.e5322.thyrosoft.Activity.INAPP_UPDATE;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {
    public static String str_login_test, profile_test;
    public static SharedPreferences sh, profile;
    public static SharedPreferences.Editor editor, editor1;
    private static String TAG = SplashScreen.class.getSimpleName();
    AlertDialog alert;
    String version;
    Boolean isInternetPresent = false;  // flag for Internet connection status
    ConnectionDetector cd;   // Connection detector class
    String z = "";
    Animation anim;
    String version1;
    String response1;
    String resId;
    ImageView iv;
    static String path;
    String USER_CODE = "";
    private int versionCode = 0;
    private String ApkUrl;
    private GlobalClass globalClass;
    private boolean firstRunSplash;
    ProgressDialog barProgressDialog;
    File ThyrosoftLite;
    private static final int MEGABYTE = 1024 * 1024;

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


    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash_screen);
        anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.translate);
        iv = (ImageView) findViewById(R.id.logo);

        Fabric.with(this, new Crashlytics());

        if (globalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }


        //   getversion();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        version = pInfo.versionName;
        versionCode = pInfo.versionCode;
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
        }

        new INAPP_UPDATE(SplashScreen.this);

    }


    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}