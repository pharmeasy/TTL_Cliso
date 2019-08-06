package com.example.e5322.thyrosoft.Models;

public class VerifyotpRequest {


    /**
     * Domain : Cli-So
     * UserCode : TAM03
     * OTP : 8382
     * Purpose : Offer
     * Mobile : 9137985277
     */

    private String Domain;
    private String UserCode;
    private String OTP;
    private String Purpose;
    private String Mobile;

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String Domain) {
        this.Domain = Domain;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String Purpose) {
        this.Purpose = Purpose;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }
}
