package com.example.e5322.thyrosoft.Models.RequestModels;

public class GenerateOTPRequestModel {
    private String api_key,mobile,type,AccessToken,ReqId;

    public String getReqId() {
        return ReqId;
    }

    public void setReqId(String reqId) {
        ReqId = reqId;
    }

    public String getApi_key() {
        return api_key;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
