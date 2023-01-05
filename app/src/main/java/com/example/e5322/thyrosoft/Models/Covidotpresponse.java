package com.example.e5322.thyrosoft.Models;

public class Covidotpresponse {


    /**
     * Mobile : 7020602182
     * Otp : 3733
     * resId : RES0000
     * response : OTP inserted successfully
     */

    private String Mobile;
    private String Otp;
    private String resId;
    private String response;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String Otp) {
        this.Otp = Otp;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
