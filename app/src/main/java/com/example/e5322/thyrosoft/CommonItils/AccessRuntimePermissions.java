package com.example.e5322.thyrosoft.CommonItils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AccessRuntimePermissions {

    public static boolean checkLocationPerm(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestLocationPerm(Activity activity) {
        int LOCATION_PERMISSION_REQUEST_CODE = 199;
        ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    public static boolean checkcameraPermission(Activity mActivity) {
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCamerapermission(Activity activity) {
        int cameracode = 110;
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, cameracode);
    }

    public static boolean checkExternalPerm(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestExternalpermission(Activity activity) {
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

}
