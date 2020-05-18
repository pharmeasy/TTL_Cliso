package com.example.e5322.thyrosoft.Models;

public class ValidateOTPmodel {


    /**
     * Mobile : 7020602182
     * OTP : 8648
     * Response : OTP Send Successfully
     * ResponseId : RES0000
     */

    private String Mobile;
    private String OTP;
    private String Response;
    private String ResponseId;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String ResponseId) {
        this.ResponseId = ResponseId;
    }
}
