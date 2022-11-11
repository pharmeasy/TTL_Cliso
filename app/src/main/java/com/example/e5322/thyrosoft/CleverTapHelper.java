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
    DatabaseHelper databaseHelper;
    ArrayList<GetProductsRecommendedResModel.ProductListDTO> recoList;


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
            recoShownData.put("Target_Test", TargetTest);
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

    public void woeFailureEvent(String message, String status, String barcode_id, String tests) {
        try {
            HashMap<String, Object> woeFailureData = new HashMap<>();
            woeFailureData.put("message", message);
            woeFailureData.put("Status", status);
            woeFailureData.put("Barcode_Id", barcode_id);
            woeFailureData.put("Tests", tests);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("WOE_FAILURE", woeFailureData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void NullSearchEvent(String header, String patientdetails, String Temp_wo_id, String search_term, int searchTermCount) {
        try {
            HashMap<String, Object> nullSearchData = new HashMap<>();
            nullSearchData.put("Header", header);
            nullSearchData.put("PatientDetails", patientdetails);
            nullSearchData.put("Temp_wo_id", Temp_wo_id);
            nullSearchData.put("Search_Term", search_term);
            nullSearchData.put("SearchTermCount", searchTermCount);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("NULL_SEARCH", nullSearchData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}