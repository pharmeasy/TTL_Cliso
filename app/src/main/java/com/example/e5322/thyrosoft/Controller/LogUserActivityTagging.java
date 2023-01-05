package com.example.e5322.thyrosoft.Controller;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.AppuserReq;
import com.google.firebase.iid.FirebaseInstanceId;

public class LogUserActivityTagging {
    String User, token, str_modType;
    SharedPreferences sharedPreferences;

    public LogUserActivityTagging(Activity activity, String screen, String remark) {
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

            Log.e("ISFIRSTINSTALL---", "" + isFirstInstall);

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


            if (!GlobalClass.isNull(User)) {
                editorUserActivity.putString(Constants.SHR_USERNAME, User);
            } else {
                editorUserActivity.putString(Constants.SHR_USERNAME, "");
            }

            if (!GlobalClass.isNull(screen) && screen.equalsIgnoreCase(Constants.SHLOGIN)) {
                editorUserActivity.putString(Constants.SHR_MODTYPE, Constants.SHLOGIN);
                editorUserActivity.putString(Constants.SHR_ISLOGIN, "Y");
            } else if (!GlobalClass.isNull(screen) && screen.equalsIgnoreCase(Constants.LOGOUT)) {
                editorUserActivity.putString(Constants.SHR_MODTYPE, Constants.LOGOUT);
                editorUserActivity.putString(Constants.SHR_ISLOGIN, "N");
            } else if (!GlobalClass.isNull(screen)) {
                str_modType = screen;
            }
            editorUserActivity.apply();

            AppuserReq requestModel = new AppuserReq();
            requestModel.setAppId(Constants.USER_APPID);
            requestModel.setIMIENo(GlobalClass.getIMEINo(activity));
            requestModel.setIslogin(sharedPreferencesUserActivity.getString(Constants.SHR_ISLOGIN, ""));
            if (!GlobalClass.isNull(str_modType)) {
                requestModel.setModType(str_modType);
            } else if (!GlobalClass.isNull(str_modType) && GlobalClass.checkEqualIgnoreCase(str_modType, Constants.REPORT_DOWNLOAD)) {
                requestModel.setModType(Constants.REPORT_DOWNLOAD);
            } else {
                requestModel.setModType(sharedPreferencesUserActivity.getString(Constants.SHR_MODTYPE, ""));
            }
            requestModel.setOS(sharedPreferencesUserActivity.getString(Constants.SHR_OS, ""));
            requestModel.setVersion(sharedPreferencesUserActivity.getString(Constants.SHR_VERSION, ""));
            requestModel.setUserID(sharedPreferencesUserActivity.getString(Constants.SHR_USERNAME, ""));
            requestModel.setToken(sharedPreferencesUserActivity.getString(Constants.SHR_TOKEN, ""));
            requestModel.setLat(Global.getCurrentLatLong(activity).getmLatitude());
            requestModel.setLongi(Global.getCurrentLatLong(activity).getmLongitude());
            requestModel.setIpadd(Global.getIPAddress(true));
            requestModel.setMacadd(Global.getMACAddress());
            if (!GlobalClass.isNull(remark)) {
                requestModel.setRemark(remark);
            } else {
                requestModel.setRemark("");
            }
            try {
                if (ControllersGlobalInitialiser.logUserActivityController != null) {
                    ControllersGlobalInitialiser.logUserActivityController = null;
                }
                if (GlobalClass.isNetworkAvailable(activity)) {
                    ControllersGlobalInitialiser.logUserActivityController = new LogUserActivityController(activity);
                    ControllersGlobalInitialiser.logUserActivityController.updateUserActivity(requestModel);
                }/* else {
                    Toast.makeText(activity, MessageConstants.CHECK_INTERNET_CONN, Toast.LENGTH_SHORT).show();
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
