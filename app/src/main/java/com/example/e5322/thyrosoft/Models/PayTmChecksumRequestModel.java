package com.example.e5322.thyrosoft.Models;

public class PayTmChecksumRequestModel {


    /**
     * api_key : CYs3zmAdtXVFUa4tSwpzD)zi2HE8OK8g0z3fvmKQjiw=
     * type : GENCHECKSUM
     * txtORDID : FDD8BC
     * txtAmount : 1200
     * txtCallBack : https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp
     * txtMobile : 8655004899
     * txtEmail : hellboy.hule@gmail.com
     * txtWebsite : APP_STAGING
     * txtCustID : 8655004899
     * txtINDID : Retail
     */

    private String api_key;
    private String type;
    private String txtORDID;
    private String txtAmount;
    private String txtCallBack;
    private String txtMobile;
    private String txtEmail;
    private String txtWebsite;
    private String txtCustID;
    private String txtINDID;

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxtORDID() {
        return txtORDID;
    }

    public void setTxtORDID(String txtORDID) {
        this.txtORDID = txtORDID;
    }

    public String getTxtAmount() {
        return txtAmount;
    }

    public void setTxtAmount(String txtAmount) {
        this.txtAmount = txtAmount;
    }

    public String getTxtCallBack() {
        return txtCallBack;
    }

    public void setTxtCallBack(String txtCallBack) {
        this.txtCallBack = txtCallBack;
    }

    public String getTxtMobile() {
        return txtMobile;
    }

    public void setTxtMobile(String txtMobile) {
        this.txtMobile = txtMobile;
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(String txtEmail) {
        this.txtEmail = txtEmail;
    }

    public String getTxtWebsite() {
        return txtWebsite;
    }

    public void setTxtWebsite(String txtWebsite) {
        this.txtWebsite = txtWebsite;
    }

    public String getTxtCustID() {
        return txtCustID;
    }

    public void setTxtCustID(String txtCustID) {
        this.txtCustID = txtCustID;
    }

    public String getTxtINDID() {
        return txtINDID;
    }

    public void setTxtINDID(String txtINDID) {
        this.txtINDID = txtINDID;
    }
}
