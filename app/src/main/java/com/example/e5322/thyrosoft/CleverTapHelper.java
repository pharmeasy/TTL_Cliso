package com.example.e5322.thyrosoft;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Models.GetProductsRecommendedResModel;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CleverTapHelper {
    Activity mActivity;
    SharedPreferences pref;
    ArrayList<GetProductsRecommendedResModel.ProductListDTO> recoList;
    DatabaseHelper databaseHelper;

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

    public void recoShown_Event(String BaseTest, String BaseRate, String TargetTest, String TargetRate, String OrderDetails) {
        try {
            databaseHelper = new DatabaseHelper(mActivity);
            recoList = new ArrayList<>();


            HashMap<String, Object> recoShownData = new HashMap<>();
            recoShownData.put("Base_Test", BaseTest);
            recoShownData.put("Base_Rate", BaseRate);
            recoShownData.put("Target_Test", TargetTest + "," + BaseTest);
            recoShownData.put("Target_Rate", TargetRate);
            recoShownData.put("Order_Details", OrderDetails);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("reco_shown", recoShownData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void reconextclick_Event(boolean recoSelected, String selectedTests, String selectedTestValue) {
        try {
            HashMap<String, Object> recoselectedData = new HashMap<>();
            recoselectedData.put("Reco_selected ", recoSelected);
            recoselectedData.put("Selected_Tests", selectedTests);
            recoselectedData.put("Select_Tests_value", selectedTestValue);
            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("reco_next_click", recoselectedData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void woeFailureEvent(String message, String status, String barcode_id, String patientId) {
        try {
            HashMap<String, Object> woeFailureData = new HashMap<>();
            woeFailureData.put("message", message);
            woeFailureData.put("Status", status);
            woeFailureData.put("Barcode_Id", barcode_id);
            woeFailureData.put("Patient_Id", patientId);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("WOE_FAILURE", woeFailureData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
