package com.example.e5322.thyrosoft;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Constants;

import java.util.HashMap;

public class CleverTapHelper {
    Activity mActivity;
    SharedPreferences pref;

    public CleverTapHelper(Activity mActivity) {
        this.mActivity = mActivity;
        try {
            pref = mActivity.getSharedPreferences("savePatientDetails", Context.MODE_PRIVATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void woeSubmitEvent(String usercode, String category, String test, String timestamp) {
        try {
            HashMap<String, Object> woeSubmitData = new HashMap<>();
            woeSubmitData.put("Source_code", usercode);
            woeSubmitData.put("Category", category);
            woeSubmitData.put("Product", test);
            woeSubmitData.put("TimeStamp", timestamp);
            woeSubmitData.put("Tenant", timestamp);
            woeSubmitData.put("Platform", Constants.CLISO_ANDROID_APP);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("WOE_SUBMIT", woeSubmitData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
