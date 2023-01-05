package com.example.e5322.thyrosoft.Models;

public class Covid_validateotp_req {

    /**
     * Mobile : 9137985277
     * Api_key : nVBoST2makVq5DlPRE@WVj3M5WU4NaGo)bsELEn8lFY=
     * Otp : 3521
     */

    private String Mobile;
    private String Api_key;
    private String Otp;
    private String Scode;

    public String getScode() {
        return Scode;
    }

    public void setScode(String scode) {
        Scode = scode;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getApi_key() {
        return Api_key;
    }

    public void setApi_key(String Api_key) {
        this.Api_key = Api_key;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }
}
