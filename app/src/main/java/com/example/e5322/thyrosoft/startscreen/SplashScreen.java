package com.example.e5322.thyrosoft.startscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.LogUserActivityTagging;
import com.example.e5322.thyrosoft.Controller.VersionCheckAPIController;
import com.example.e5322.thyrosoft.DownloadInAppTask;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CovidAccessReq;
import com.example.e5322.thyrosoft.Models.CovidAccessResponseModel;
import com.example.e5322.thyrosoft.Models.FirebaseModel;
import com.example.e5322.thyrosoft.Models.Firebasepost;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.Retrofit.APIInteface;
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
import com.scottyab.rootbeer.RootBeer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private static final int APPPERMISSTION = 110;
    public static SharedPreferences profile;
    public static SharedPreferences.Editor editor;
    String strtoken = "";
    private static String TAG = SplashScreen.class.getSimpleName();
    String version;
    ConnectionDetector cd;
    Animation anim;
    ImageView iv;
    String USER_CODE = "";
    Activity activity;
    String newToken, storetoken;
    boolean IsFromNotification;
    String user, passwrd;
    AppPreferenceManager appPreferenceManager;
    int SCRID;
    private int WRITE_EXTERNAL_STORAGE = 123;
    private SharedPreferences prefs_CovidSync;
    boolean isemulator = false;


    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        try {
            activity = SplashScreen.this;
//            FirebaseApp.initializeApp(SplashScreen.this);
            prefs_CovidSync = getSharedPreferences("CovidAccess_sync", 0);
            anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.translate);
            iv = (ImageView) findViewById(R.id.logo);
            appPreferenceManager = new AppPreferenceManager(activity);
            GlobalClass.setStatusBarcolor(activity);
            if (getIntent().getBooleanExtra("Exit me", false)) {
                finish();
                return;
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

            isemulator = GlobalClass.buildModelContainsEmulatorHints(Build.MODEL);
            Log.e(TAG, "isemulator -->" + isemulator);

            RootBeer rootBeer = new RootBeer(activity);
            if (rootBeer.isRooted()) {
                Log.e(TAG, "DEVICE ROOTED-->" + rootBeer.isRooted());
                if (isemulator) {
                    getAllpermission();
                }
            } else {
                getAllpermission();
            }

            version = GlobalClass.getversion(activity);


            TextView versionText = (TextView) findViewById(R.id.version);
            versionText.setText(version);

            cd = new ConnectionDetector(getApplicationContext());


            SharedPreferences prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
            user = prefs.getString("Username", "");
            passwrd = prefs.getString("password", "");
            USER_CODE = prefs.getString("USER_CODE", "");

            SharedPreferences fire_pref = getSharedPreferences(Constants.SH_FIRE, MODE_PRIVATE);
            storetoken = fire_pref.getString(Constants.F_TOKEN, "");
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getAllpermission() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(SplashScreen.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_NETWORK_STATE,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.WAKE_LOCK,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.WRITE_CONTACTS,
                                    Manifest.permission.INTERNET,
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            APPPERMISSTION);


                } else {
                    CallFirebaseDynamicLinkListeners();
                }
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case APPPERMISSTION: {
                if (grantResults.length > 0) {
                    boolean allGranted = true;
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            allGranted = false;
                            break;
                        }
                    }
                    if (!allGranted) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("Please provide the permissions requested. Application will shutdown now.")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        activity.finish();
                                    }
                                });
                        builder.show();
                    } else {
                        CallFirebaseDynamicLinkListeners();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("Failed to recognize the permissions. Please restart the app and try again")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    activity.finish();
                                }
                            });
                    builder.create().
                            show();
                }
                return;
            }

        }
    }


    private void CallFirebaseDynamicLinkListeners() {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
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

                        }
                        GoAhead();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        GoAhead();
                    }
                });

    }

    public void pushToken() {

        APIInteface apiInterface = RetroFit_APIClient.getInstance().getClient(activity, Api.PushToken).create(APIInteface.class);
        Firebasepost firebasepost = new Firebasepost();
        firebasepost.setClient_Id(user);
        firebasepost.setAppName(Constants.APPNAME);
        firebasepost.setEnterBy(user);
        firebasepost.setToken(strtoken);
        firebasepost.setTopic(Constants.TOPIC);


        Call<FirebaseModel> responseCall = apiInterface.pushtoken(firebasepost);
        Log.e("TAG", "PUSH TOKEN --->" + new GsonBuilder().create().toJson(firebasepost));
        responseCall.enqueue(new Callback<FirebaseModel>() {
            @Override
            public void onResponse(Call<FirebaseModel> call, Response<FirebaseModel> response) {
                try {
                    if (response.body().getResponseId().equalsIgnoreCase(Constants.RES0000)) {
                        Intent prefe = new Intent(activity, ManagingTabsActivity.class);
                        prefe.putExtra("Screen_category", SCRID);
                        prefe.putExtra(Constants.COMEFROM, true);
                        prefe.putExtra(Constants.IsFromNotification, IsFromNotification);
                        activity.startActivity(prefe);
                        ((Activity) activity).finish();
                        Constants.PUSHNOT_FLAG = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<FirebaseModel> call, Throwable t) {
                Log.e(TAG, "o n E r r o r ---->" + t.getLocalizedMessage());
            }
        });
    }

    private void GoAhead() {

        if (cd.isConnectingToInternet()) {
            callAPICheckVersion();
        } else {
            GlobalClass.showCustomToast(activity, ToastFile.intConnection, 1);
        }
    }


    private void callAPICheckVersion() {
        try {
            if (ControllersGlobalInitialiser.versionCheckAPIController != null) {
                ControllersGlobalInitialiser.versionCheckAPIController = null;
            }


            String sourcecode = "";
            if (!GlobalClass.isNull(user)) {
                sourcecode = user;
            } else {
                sourcecode = "";
            }

            ControllersGlobalInitialiser.versionCheckAPIController = new VersionCheckAPIController(SplashScreen.this, activity, sourcecode);
            ControllersGlobalInitialiser.versionCheckAPIController.checkVersion(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVersionResponse(final String apiVersion, final String apkUrl, String response) {
        if (apiVersion != null && response.equalsIgnoreCase("Success")) {
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
        new LogUserActivityTagging(activity, "","");

        if (!GlobalClass.isNull(user) && !GlobalClass.isNull(passwrd) ) {
            long PreviousTime = prefs_CovidSync.getLong("PreivousTimeOfSync", 0);
            long currenttime = System.currentTimeMillis();
            long differ_millis = currenttime - PreviousTime;
            int days = (int) (differ_millis / (1000 * 60 * 60 * 24));

            if (days >= 1) {
                if (GlobalClass.isNetworkAvailable(activity)) {
                    checkcovidaccess();
                } else
                    GlobalClass.showCustomToast(SplashScreen.this, MessageConstants.CHECK_INTERNET_CONN, 0);
            } else {
                Log.e(TAG, " Caching :Less than 1 Day");
            }

            Log.e(TAG, "Constants.PUSHNOT_FLAG---->" + Constants.PUSHNOT_FLAG);

            if (Constants.PUSHNOT_FLAG) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreen.this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        strtoken = instanceIdResult.getToken();
                        Log.e("strtoken----->", strtoken);
                        pushToken();
                    }
                });
            } else {
                Intent prefe = new Intent(activity, ManagingTabsActivity.class);
                prefe.putExtra("Screen_category", SCRID);
                prefe.putExtra(Constants.COMEFROM, true);
                prefe.putExtra(Constants.IsFromNotification, IsFromNotification);
                activity.startActivity(prefe);
                ((Activity) activity).finish();
            }


        } else {
            Intent i = new Intent(activity, Login.class);
            activity.startActivity(i);
            ((Activity) activity).finish();
        }
    }

    private void checkcovidaccess() {

        PostAPIInteface postAPIInteface = RetroFit_APIClient.getInstance().getClient(activity, Api.Cloud_base).create(PostAPIInteface.class);
        CovidAccessReq covidAccessReq = new CovidAccessReq();
        covidAccessReq.setSourceCode(user);
        Call<CovidAccessResponseModel> covidaccessResCall = postAPIInteface.checkcovidaccess(covidAccessReq);
        covidaccessResCall.enqueue(new Callback<CovidAccessResponseModel>() {
            @Override
            public void onResponse(Call<CovidAccessResponseModel> call, retrofit2.Response<CovidAccessResponseModel> response) {
                try {
                    SharedPreferences.Editor editor = getSharedPreferences("CovidAccess_sync", 0).edit();
                    editor.clear();
                    editor.putLong("PreivousTimeOfSync", System.currentTimeMillis()); // add this line and comment below line for cache
                    editor.commit();

                    if (response != null && response.body() != null) {
                        appPreferenceManager.setCovidAccessResponseModel(response.body());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CovidAccessResponseModel> call, Throwable t) {

            }
        });
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