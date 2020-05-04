package com.example.e5322.thyrosoft.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.example.e5322.thyrosoft.Controller.Log;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.AppuserReq;
import com.google.firebase.iid.FirebaseInstanceId;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELEPHONY_SERVICE;

public class LogUserActivityTagging {
    String
            User, token;
    SharedPreferences sharedPreferences;

    public LogUserActivityTagging(Activity activity, String screen) {
        try {
            sharedPreferences = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
            User = sharedPreferences.getString("Username", "");
            String appversion = GlobalClass.getversion(activity);
            String os = "ANDROID " + Build.VERSION.RELEASE;
            token = FirebaseInstanceId.getInstance().getToken();
            Log.e("TAG", "TOKEN---->" + token);
            SharedPreferences sharedPreferencesUserActivity = activity.getSharedPreferences(Constants.SHR_USERLOG, MODE_PRIVATE);
            SharedPreferences.Editor editorUserActivity = sharedPreferencesUserActivity.edit();
            Boolean isFirstInstall = sharedPreferencesUserActivity.getBoolean(Constants.SHR_FIRSTINSTALL, true);

            Log.e("ISFIRSTINSTALL---",""+isFirstInstall);

            if (isFirstInstall) {
                editorUserActivity.putBoolean(Constants.SHR_FIRSTINSTALL, false);
                editorUserActivity.putString(Constants.SHR_MODTYPE, "INSTALL");
                editorUserActivity.putString(Constants.SHR_ISLOGIN, "N");
                editorUserActivity.putString(Constants.SHR_OS, os);
                editorUserActivity.putString(Constants.SHR_VERSION, appversion);
                editorUserActivity.putString(Constants.SHR_TOKEN, token);
            } else {
                if (!os.equalsIgnoreCase(sharedPreferencesUserActivity.getString(Constants.SHR_OS, ""))) {
                    editorUserActivity.putString(Constants.SHR_OS, os);
                    editorUserActivity.putString(Constants.SHR_MODTYPE, "OS UPDATED");
                }
                if (!appversion.equalsIgnoreCase(sharedPreferencesUserActivity.getString(Constants.SHR_VERSION, ""))) {
                    editorUserActivity.putString(Constants.SHR_VERSION, appversion);
                    editorUserActivity.putString(Constants.SHR_MODTYPE, "VERSION UPDATED");
                }
            }


            if (!TextUtils.isEmpty(User)) {
                editorUserActivity.putString(Constants.SHR_USERNAME,User);
            } else {
                editorUserActivity.putString(Constants.SHR_USERNAME, "");
            }

            if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase(Constants.SHLOGIN)) {
                editorUserActivity.putString(Constants.SHR_MODTYPE, Constants.SHLOGIN);
                editorUserActivity.putString(Constants.SHR_ISLOGIN, "Y");
            } else if (!TextUtils.isEmpty(screen) && screen.equalsIgnoreCase(Constants.LOGOUT)) {
                editorUserActivity.putString(Constants.SHR_MODTYPE, Constants.LOGOUT);
                editorUserActivity.putString(Constants.SHR_ISLOGIN, "N");
            }

            editorUserActivity.apply();

            String imeiNo = null;
            try {
                TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    imeiNo = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                } else {
                    if (telephonyManager.getDeviceId() != null) {
                        imeiNo = telephonyManager.getDeviceId();
                    } else {
                        imeiNo = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            AppuserReq requestModel = new AppuserReq();
            requestModel.setAppId(Constants.USER_APPID);
            requestModel.setIMIENo(imeiNo);
            requestModel.setIslogin(sharedPreferencesUserActivity.getString(Constants.SHR_ISLOGIN, ""));
            requestModel.setModType(sharedPreferencesUserActivity.getString(Constants.SHR_MODTYPE, ""));
            requestModel.setOS(sharedPreferencesUserActivity.getString(Constants.SHR_OS, ""));
            requestModel.setVersion(sharedPreferencesUserActivity.getString(Constants.SHR_VERSION, ""));
            requestModel.setUserID(sharedPreferencesUserActivity.getString(Constants.SHR_USERNAME, ""));
            requestModel.setToken(sharedPreferencesUserActivity.getString(Constants.SHR_TOKEN, ""));


            try {
                if (ControllersGlobalInitialiser.logUserActivityController != null) {
                    ControllersGlobalInitialiser.logUserActivityController = null;
                }
                if (GlobalClass.isNetworkAvailable(activity)) {
                    ControllersGlobalInitialiser.logUserActivityController = new LogUserActivityController(activity);
                    ControllersGlobalInitialiser.logUserActivityController.updateUserActivity(requestModel);
                } else {
                    Toast.makeText(activity, MessageConstants.CHECK_INTERNET_CONN, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
