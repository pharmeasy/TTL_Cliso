package com.example.e5322.thyrosoft.Models;

public class VerifyotpModel {


    /**
     * Mobile : 7878789000
     * OTP : 2468
     * Response : OTP Validated Successfully
     * ResponseId : RES0001
     * UserCode : TAM03
     */

    private String Mobile;
    private String OTP;
    private String Response;
    private String ResponseId;
    private String UserCode;

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

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }
}
