package com.example.e5322.thyrosoft.Controller;

import androidx.multidex.MultiDexApplication;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.example.e5322.thyrosoft.Cliso_BMC.GetAvailableStockController;
import com.example.e5322.thyrosoft.Cliso_BMC.UpdateStockController;

public class ControllersGlobalInitialiser extends MultiDexApplication {

    public static GetHealthTipsController GetHealthTipsController;
    public static Covidmultipart_controller covidmultipart_controller;
    public static GetClientDetail_Controller getClientDetail_controller;
    public static LogUserActivityController logUserActivityController;
    public static ValidateMob_Controller validateMob_controller;
    public static EmailValidationController emailValidationController;
    public static VerifyotpController verifyotpController;
    public static GetAvailableStockController getAvailableStockController;
    public static UpdateStockController updateStockController;
    public static GetOTPController getOTPController;
    public static GETValidateBSOTPController getValidateBSOTPController;
    public static BloodSugarMISController bloodSugarMISController;
    public static GenerateTokenController generateTokenController;
    public static POSTBookLeadController POSTBookLeadController;
    public static VersionCheckAPIController versionCheckAPIController;
    public static getSCPSRFAPIController getSCPSRFAPIController;
    public static GetOTPCreditMISController getOTPCreditMISController;
    public static Viewgenhandbill_Contoller viewgenhandbill_contoller;
    public static Handbilltemplate_Controller handbilltemplate_controller;
    public static UploadDoc_Controller uploadDoc_controller;
    public static GetBroadcastsListController getBroadcastsListController;
    public static AckBroadcastMsgController ackBroadcastMsgController;


    @Override
    public void onCreate() {

        ActivityLifecycleCallback.register(this);
        super.onCreate();
    }
}
