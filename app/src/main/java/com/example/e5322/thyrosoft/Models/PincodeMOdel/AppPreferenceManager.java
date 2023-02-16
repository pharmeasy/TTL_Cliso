package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.app.Activity;
import android.content.Context;

import com.example.e5322.thyrosoft.HHHtest.Model.GetTestResponseModel;
import com.example.e5322.thyrosoft.Models.CovidAccessResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBaselineDetailsResponseModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.VersionResponseModel;
import com.google.gson.Gson;


public class AppPreferenceManager {

    private AppPreference appPreference;
    private String leaveFromDate = "leaveFromDate";
    private String isAppInBackground = "isAppInBackground";
    private String versionresponse = "versionresponse";
    private String pocttest = "pocttest";
    private String synProductCount = "synProductCount";
    private String CovidAcess = "CovidAccess";
    private String BaseLine = "BaseLine";
    private String Reward = "Reward";
    private String Terms = "Terms";
    private String TermsURL = "TermsURL";
    private String TermsFlag = "TermsFlag";
    private String BaselineSync = "BaselineSync";
    private String WoMasterSync = "WoMasterSync";
    private String ProductSyncIntent = "ProductSyncIntent";


    public AppPreferenceManager(Activity activity) {
        super();
        appPreference = AppPreference.getAppPreferences(activity);
    }

    public AppPreferenceManager(Context context) {
        appPreference = AppPreference.getAppPreferences(context);
    }

    public String getProductSyncIntent() {
        return appPreference.getString(ProductSyncIntent,"");
    }

    public void setProductSyncIntent(String productSyncIntent) {
        appPreference.putString(ProductSyncIntent,productSyncIntent);
    }

    public String getWoMasterSyncDate() {
        return appPreference.getString(WoMasterSync, "");
    }

    public void setWoMasterSyncDate(String date) {
        appPreference.putString(WoMasterSync, date);
    }

    public String getRewardPopUpDate() {
        return appPreference.getString(Reward, "");
    }

    public void setRewardPopUpDate(String date) {
        appPreference.putString(Reward, date);
    }

    public String getTermsSyncDate() {
        return appPreference.getString(Terms, "");
    }

    public void setTermsSyncDate(String date) {
        appPreference.putString(Terms, date);
    }

    public String getTermsUrl() {
        return appPreference.getString(TermsURL, "");
    }

    public void setTermsUrl(String date) {
        appPreference.putString(TermsURL, date);
    }

    public Boolean getTermsFlag() {
        return appPreference.getBoolean(TermsFlag, true);
    }

    public void setTermsFlag(Boolean value) {
        appPreference.putBoolean(TermsFlag, value);
    }

    public String getBaseLineSyncDate() {
        return appPreference.getString(BaselineSync, "");
    }

    public void setBaseLineSyncDate(String date) {
        appPreference.putString(BaselineSync, date);
    }

    public GetBaselineDetailsResponseModel getBaselineDetailsResponse() {
        String value = appPreference.getString(BaseLine, "");
        return new Gson().fromJson(value, GetBaselineDetailsResponseModel.class);
    }

    public void setBaselineDetailsResponse(GetBaselineDetailsResponseModel responseModel) {
        appPreference.putString(BaseLine, new Gson().toJson(responseModel));
    }

    public CovidAccessResponseModel getCovidAccessResponseModel() {
        String value = appPreference.getString(this.CovidAcess, "");
        return new Gson().fromJson(value, CovidAccessResponseModel.class);
    }


    public void setCovidAccessResponseModel(CovidAccessResponseModel responseModel) {
        appPreference.putString(this.CovidAcess, new Gson().toJson(responseModel));
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
        return appPreference.getInt(synProductCount, 0);
    }

    public void setSynProductCount(int count) {
        appPreference.putInt(synProductCount, count);
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

    public void clearAllPreferences() {
        appPreference.clearPreferences();
    }


}