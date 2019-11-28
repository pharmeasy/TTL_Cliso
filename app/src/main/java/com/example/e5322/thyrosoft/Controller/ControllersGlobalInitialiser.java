package com.example.e5322.thyrosoft.Controller;

import android.support.multidex.MultiDexApplication;

import com.example.e5322.thyrosoft.Cliso_BMC.GetAvailableStockController;
import com.example.e5322.thyrosoft.Cliso_BMC.UpdateStockController;

public class ControllersGlobalInitialiser extends MultiDexApplication {

    public static GetHealthTipsController GetHealthTipsController;
    public static GetClientDetail_Controller getClientDetail_controller;
    public static ValidateMob_Controller validateMob_controller;
    public static VerifyotpController verifyotpController;
    public static GetAvailableStockController getAvailableStockController;
    public static UpdateStockController updateStockController;
    public static GetOTPController getOTPController;
    public static GETValidateBSOTPController getValidateBSOTPController;
    public static BloodSugarMISController bloodSugarMISController;
    public static GenerateTokenController generateTokenController;
    public static POSTBookLeadController POSTBookLeadController;
}
