package com.example.e5322.thyrosoft.startscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
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
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.VersionCheckAPIController;
import com.example.e5322.thyrosoft.DownloadInAppTask;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.AppuserReq;
import com.example.e5322.thyrosoft.Models.AppuserResponse;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.PostAPIInteface;
import com.example.e5322.thyrosoft.Retrofit.RetroFit_APIClient;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.GsonBuilder;

import java.io.File;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private static final int MEGABYTE = 1024 * 1024;
    private static final int REQUEST_READ_PHONE_STATE = 110;
    public static String str_login_test, profile_test, IMEI;
    public static SharedPreferences sh, profile;
    public static SharedPreferences.Editor editor, editor1;
    SharedPreferences shr_user_log;
    String androidOS, shr_osversion, shr_versionname;
    static String path;

    private static String TAG = SplashScreen.class.getSimpleName();
    AlertDialog alert;
    String strmodtype;
    String version, islogin;
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
    private boolean isfirsttime=true;
    private String ApkUrl, username;
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


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
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

//        shr_user_log = getSharedPreferences(Constants.SHR_USERLOG, MODE_PRIVATE);
//
//
//        try {
//            isfirsttime=shr_user_log.getBoolean("my_first_time",true);
//            shr_osversion = shr_user_log.getString(Constants.SHR_OS, null);
//            shr_versionname = shr_user_log.getString(Constants.SHR_VERSION, null);
//            username = shr_user_log.getString(Constants.SHR_USERNAME, null);
//            androidOS = Build.VERSION.RELEASE;
//            if (shr_osversion != null && !androidOS.equalsIgnoreCase(shr_osversion)) {
//                taguser("osupdated");
//            }
//
//            try {
//                if (shr_versionname != null && !version.equalsIgnoreCase(shr_versionname)) {
//                    taguser("updated");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        SharedPreferences prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        String access = prefs.getString("ACCESS_TYPE", null);
        String api_key = prefs.getString("API_KEY", null);
        USER_CODE = prefs.getString("USER_CODE", "");

        SharedPreferences fire_pref = getSharedPreferences(Constants.SH_FIRE, MODE_PRIVATE);
        storetoken = fire_pref.getString(Constants.F_TOKEN, "");
        CallFirebaseDynamicLinkListeners();
    }

    private void CallFirebaseDynamicLinkListeners() {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {

                            deepLink = pendingDynamicLinkData.getLink();
                            System.out.println("Dynamic Link : " + deepLink.toString());
                            String SrcID = String.valueOf(deepLink.getQueryParameter("ScreenID"));
                            int ScreenID = 0;
                            try {
                                ScreenID = Integer.parseInt(SrcID);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            IsFromNotification = true;
                            SCRID = ScreenID;

                            GoAhead();
                        } else {
                            GoAhead();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        GoAhead();
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });

    }

    private void GoAhead() {
        if (cd.isConnectingToInternet()) {
           // if (!isfirsttime){
                callAPICheckVersion();
            //}
        } else {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreen.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    newToken = instanceIdResult.getToken();
                    Log.e("NewToken----->", newToken);

                    if (user != null && passwrd != null) {
                        Intent intent = new Intent(SplashScreen.this, ManagingTabsActivity.class);
                        intent.putExtra("Screen_category", SCRID);
                        intent.putExtra(Constants.COMEFROM, true);
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

//    private void taguser(String modtype) {
//
//
//        strmodtype = modtype;
//        androidOS = Build.VERSION.RELEASE;
//        if (strmodtype.equalsIgnoreCase("install")) {
//            islogin = "N";
//        } else {
//            islogin = "Y";
//        }
//
//        IMEI = getDeviceIMEI();
//        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(Api.THYROCARE).create(PostAPIInteface.class);
//        AppuserReq appuserReq = new AppuserReq();
//        appuserReq.setAppId(Constants.USER_APPID);
//        appuserReq.setIMIENo(IMEI);
//        appuserReq.setIslogin(islogin);
//        appuserReq.setModType(strmodtype);
//        appuserReq.setOS(androidOS);
//        appuserReq.setToken("");
//        if (islogin.equalsIgnoreCase("Y")) {
//            appuserReq.setUserID(username);
//        } else {
//            appuserReq.setUserID("");
//        }
//
//        appuserReq.setVersion(version);
//
//        Call<AppuserResponse> appuserResponseCall = postAPIInteface.PostUserLog(appuserReq);
//        Log.e(TAG, "TAG USER API ---->" + appuserResponseCall.request().url());
//        Log.e(TAG, "REQ Body ---->" + new GsonBuilder().create().toJson(appuserReq));
//
//        appuserResponseCall.enqueue(new Callback<AppuserResponse>() {
//            @Override
//            public void onResponse(Call<AppuserResponse> call, Response<AppuserResponse> response) {
//                try {
//                    if (response.body().getRES_ID().equalsIgnoreCase(Constants.RES0000)) {
//
//                        Log.e(TAG, "RES---->" + response.body().getRESPONSE());
//
//                        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHR_USERLOG, 0).edit();
//                        editor.putInt(Constants.SHR_APPID, Constants.USER_APPID);
//                        editor.putString(Constants.SHR_IMEI, IMEI);
//                        editor.putString(Constants.SHR_ISLOGIN, islogin);
//                        editor.putString(Constants.SHR_MODTYPE, strmodtype);
//                        editor.putString(Constants.SHR_OS, androidOS);
//                        editor.putString(Constants.SHR_VERSION, version);
//                        if (islogin.equalsIgnoreCase("Y")) {
//                            editor.putString(Constants.UserName, username)
//                            ;
//                        }
//                        editor.commit();
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<AppuserResponse> call, Throwable t) {
//
//            }
//        });
//
//    }

    private void callIntent() {
        if (user != null && passwrd != null) {
            Intent prefe = new Intent(activity, ManagingTabsActivity.class);
            prefe.putExtra("Screen_category", SCRID);
            prefe.putExtra(Constants.COMEFROM, true);
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
        TextView txt_browser = dialogView.findViewById(R.id.txt_browser);
        txt_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(apkUrl));
                startActivity(intent);
                finish();
            }
        });

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


    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            Log.e(TAG, "Permision not granted");
        } else {
            Log.e(TAG, "Permision  granted");

        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_READ_PHONE_STATE:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    Log.e(TAG, "PERMISITON GRANTED");
//                    if (shr_user_log.getBoolean("my_first_time", true)) {
//                        //the app is being launched for first time, do something
//                        Log.e("Comments", "First time");
//
//                        // first time task
//
//                        // record the fact that the app has been started at least once
//                        shr_user_log.edit().putBoolean("my_first_time", false).commit();
//                        taguser("install");
//                        isfirsttime=shr_user_log.getBoolean("my_first_time",true);
//                        if(!isfirsttime){
//                            if (cd.isConnectingToInternet()){
//                                callAPICheckVersion();
//                            }
//                        }
//
//                    }
//
//                }
//                break;
//        }
//    }

//    public String getDeviceIMEI() {
//        String deviceUniqueIdentifier = null;
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        if (null != tm) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                //return false;
//            }
//            deviceUniqueIdentifier = tm.getDeviceId();
//        }
//
//        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
//            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        }
//
//        return deviceUniqueIdentifier;
//    }

}