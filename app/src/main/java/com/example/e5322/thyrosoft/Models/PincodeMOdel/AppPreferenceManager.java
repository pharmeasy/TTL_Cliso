package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.app.Activity;
import android.content.Context;

import com.example.e5322.thyrosoft.HHHtest.Model.GetTestResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.VersionResponseModel;
import com.google.gson.Gson;


public class AppPreferenceManager {

    private AppPreference appPreference;
    private String leaveFromDate = "leaveFromDate";
    private String isAppInBackground = "isAppInBackground";
    private String versionresponse = "versionresponse";
    private String pocttest = "pocttest";
    private String synProductCount="synProductCount";


    public AppPreferenceManager(Activity activity) {
        super();
        appPreference = AppPreference.getAppPreferences(activity);
    }

    public AppPreferenceManager(Context context) {
        appPreference = AppPreference.getAppPreferences(context);
    }



    public GetTestResponseModel getTestResponseModel() {
        String value = appPreference.getString(this.pocttest, "");
        return new Gson().fromJson(value, GetTestResponseModel.class);
    }


    public void setTestResponseModel(GetTestResponseModel responseModel) {
        appPreference.putString(this.pocttest, new Gson().toJson(responseModel));
    }


    public VersionResponseModel getVersionResponseModel() {
        String value = appPreference.getString(this.versionresponse, "");
        return new Gson().fromJson(value, VersionResponseModel.class);
    }


    public void setVersionResponseModel(VersionResponseModel responseModel) {
        appPreference.putString(this.versionresponse, new Gson().toJson(responseModel));
    }


    public int getSynProductCount() {
        return appPreference.getInt(synProductCount,0);
    }

    public void setSynProductCount(int count) {
        appPreference.putInt(synProductCount,count);
    }


    public String getLeaveFromDate() {
        return appPreference.getString(leaveFromDate, "");
    }

    public void setLeaveFromDate(String leaveFromDate) {
        appPreference.putString(this.leaveFromDate, leaveFromDate);
    }

    public boolean isAppInBackground() {
        return appPreference.getBoolean(this.isAppInBackground, false);
    }

    public void setIsAppInBackground(boolean isAppInBackground) {
        appPreference.putBoolean(this.isAppInBackground, isAppInBackground);
    }







}