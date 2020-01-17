package com.example.e5322.thyrosoft.startscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.VersionCheckAPIController;
import com.example.e5322.thyrosoft.DownloadInAppTask;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {
    private static final int MEGABYTE = 1024 * 1024;
    public static String str_login_test, profile_test;
    public static SharedPreferences sh, profile;
    public static SharedPreferences.Editor editor, editor1;
    static String path;
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
    String USER_CODE = "";
    ProgressDialog barProgressDialog;
    File ThyrosoftLite;
    Activity activity;
    String newToken, storetoken;
    boolean IsFromNotification;
    String user, passwrd;
    int SCRID;
    private int versionCode = 0;
    private String ApkUrl;
    private GlobalClass globalClass;
    private boolean firstRunSplash;
    private int WRITE_EXTERNAL_STORAGE = 123;

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
        activity = SplashScreen.this;
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



        if (getIntent().hasExtra("IsFromNotification")) {
            IsFromNotification = getIntent().getBooleanExtra("IsFromNotification", false);
            if (IsFromNotification) {
                if (getIntent().hasExtra("Screen_category")) {
                    SCRID = getIntent().getIntExtra("Screen_category", 0);
                    Log.e(TAG, "Screen ID ---->" + SCRID);
                }
            }
        }

        //  getversion();
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

        SharedPreferences prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        String access = prefs.getString("ACCESS_TYPE", null);
        String api_key = prefs.getString("API_KEY", null);
        USER_CODE = prefs.getString("USER_CODE", "");

        SharedPreferences fire_pref = getSharedPreferences(Constants.SH_FIRE, MODE_PRIVATE);
        storetoken = fire_pref.getString(Constants.F_TOKEN, "");

        if (cd.isConnectingToInternet()) {
            callAPICheckVersion();
        } else {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreen.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    newToken = instanceIdResult.getToken();
                    Log.e("NewToken----->", newToken);

                    if (user != null && passwrd != null) {
                        Intent intent = new Intent(SplashScreen.this, ManagingTabsActivity.class);
                        intent.putExtra("Screen_category", SCRID);
                        intent.putExtra(Constants.COMEFROM,true);
                        intent.putExtra(Constants.IsFromNotification, IsFromNotification);
                        startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                        builder.setMessage(ToastFile.intConnection)
                                .setCancelable(false)
                                .setPositiveButton("Retry",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {

                                                Intent intent = getIntent();
                                                finish();
                                                startActivity(intent);

                                            }
                                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                }
            });
        }
    }

    private void callAPICheckVersion() {
        try {
            if (ControllersGlobalInitialiser.versionCheckAPIController != null) {
                ControllersGlobalInitialiser.versionCheckAPIController = null;
            }

            ControllersGlobalInitialiser.versionCheckAPIController = new VersionCheckAPIController(SplashScreen.this, activity);
            ControllersGlobalInitialiser.versionCheckAPIController.checkVersion(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVersionResponse(final String apiVersion, final String apkUrl, String response) {
        if (apiVersion != null && response.equalsIgnoreCase("Success")) {
            globalClass.printLog("Error", TAG, "onResponse apiVersion: ", apiVersion);
            iv.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    String newVersion = apiVersion.replace(".", "");//api
                    String preversion = version.replace(".", "");//package

                    int intApiVersion = Integer.parseInt(newVersion);//api
                    int intPkgversion = Integer.parseInt(preversion);//pkg

                    if (intPkgversion < intApiVersion) {
                        ValidateInstallPermission();
                        displayUpdateDialog(activity, apkUrl);
                    } else {
                        callIntent();

                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            GlobalClass.showShortToast(activity, response);
            ActivityCompat.finishAffinity(activity);
        }
    }

    private void callIntent() {
        if (user != null && passwrd != null) {
            Intent prefe = new Intent(activity, ManagingTabsActivity.class);
            prefe.putExtra("Screen_category", SCRID);
            prefe.putExtra(Constants.COMEFROM,true);
            prefe.putExtra(Constants.IsFromNotification, IsFromNotification);
            activity.startActivity(prefe);
            ((Activity) activity).finish();
        } else {
            Intent i = new Intent(activity, Login.class);
            activity.startActivity(i);
            ((Activity) activity).finish();
        }
    }

    private void GetInstallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), 12);
            }
        }
    }

    private void displayUpdateDialog(final Activity mActivity, final String apkUrl) {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.updatedialog, viewGroup, false);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setView(dialogView);
        dialog.setCancelable(false);
        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        Button btnUpdate = (Button) dialogView.findViewById(R.id.btnUpdate);
        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ValidateInstallPermission()) {
                        alertDialog.dismiss();
                        callDownLoadData(apkUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
                alertDialog.dismiss();
            }
        });

    }


    public boolean ValidateInstallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                return false;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                GetInstallPermission();
                return false;
            }
        }
        return true;
    }

    private void callDownLoadData(String url) {
        ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("Downloading..");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);

        // execute this when the downloader must be fired
        final DownloadInAppTask downloadInAppTask = new DownloadInAppTask((SplashScreen) activity, activity, mProgressDialog, Constants.APK_NAME + ".apk");
        downloadInAppTask.execute(url);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                downloadInAppTask.cancel(true); //cancel the task
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}