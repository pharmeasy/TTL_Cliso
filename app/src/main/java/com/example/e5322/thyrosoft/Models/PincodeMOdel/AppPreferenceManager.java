package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.app.Activity;
import android.content.Context;


public class AppPreferenceManager {

    private AppPreference appPreference;

    private String AppVersion = "app_version";
    private String APISessionKey = "ApiSessionKey";
    private String ChatPassword = "ChatPassword";
    private String loginTime = "loginTime";
    private String isAfterLogin = "isAfterLogin";
    private String leaveFromDate = "leaveFromDate";
    private String leaveToDate = "leaveToDate";
    private String isAChatRgister = "isAChatRgister";
    private String isAppInBackground = "isAppInBackground";
    private String are_terms_and_conditions_accepted = "are_terms_and_conditions_accepted";
    private String loginResponse = "loginResponse";
    private String Role="Role";


    public AppPreferenceManager(Activity activity) {
        super();
        appPreference = AppPreference.getAppPreferences(activity);
    }

    public AppPreferenceManager(Context context) {
        appPreference = AppPreference.getAppPreferences(context);
    }


    public String getAppVersion() {
        return appPreference.getString(AppVersion, "");
    }

    public void setAppVersion(String appVersion) {
        appPreference.putString(this.AppVersion, appVersion);
    }

    public String getAPISessionKey() {
        return appPreference.getString(APISessionKey, "");
    }

    public void setAPISessionKey(String aPISessionKey) {
        appPreference.putString(this.APISessionKey, aPISessionKey);
    }


    public String getChatPassword() {
        return appPreference.getString(ChatPassword, "");
    }

    public void setChatPassword(String password) {
        appPreference.putString(this.ChatPassword, password);
    }

    public String getLoginTime() {
        return appPreference.getString(loginTime, "");
    }

    public void setLoginTime(String loginTime) {
        appPreference.putString(this.loginTime, loginTime);
    }
    public String getLeaveFromDate() {
        return appPreference.getString(leaveFromDate, "");
    }

    public void setLeaveFromDate(String leaveFromDate) {
        appPreference.putString(this.leaveFromDate, leaveFromDate);
    }

    public String getLeaveToDate() {
        return appPreference.getString(leaveToDate, "");
    }

    public void setLeaveToDate(String leaveToDate) {
        appPreference.putString(this.leaveToDate, leaveToDate);
    }
    public void clearAllPreferences() {
        boolean termsAndConditionsAccepted = appPreference.getBoolean(this.are_terms_and_conditions_accepted, false);
        appPreference.clearPreferences();
        appPreference.putBoolean(this.are_terms_and_conditions_accepted, termsAndConditionsAccepted);
    }

    public boolean isAfterLogin() {
        return appPreference.getBoolean(this.isAfterLogin, false);
    }

    public void setIsAfterLogin(boolean value) {
        appPreference.putBoolean(this.isAfterLogin, value);
    }
    public boolean isChatRegister() {
        return appPreference.getBoolean(this.isAChatRgister, false);
    }

    public void setChatRegister(boolean value) {
        appPreference.putBoolean(this.isAChatRgister, value);
    }

    public boolean isAppInBackground() {
        return appPreference.getBoolean(this.isAppInBackground, false);
    }

    public void setIsAppInBackground(boolean isAppInBackground) {
        appPreference.putBoolean(this.isAppInBackground, isAppInBackground);
    }

    public boolean areTermsAndConditionsAccepted() {
        return appPreference.getBoolean(this.are_terms_and_conditions_accepted, false);
    }

    public void setAreTermsAndConditionsAccepted(boolean areTermsAndConditionsAccepted) {
        appPreference.putBoolean(this.are_terms_and_conditions_accepted, areTermsAndConditionsAccepted);
    }




}