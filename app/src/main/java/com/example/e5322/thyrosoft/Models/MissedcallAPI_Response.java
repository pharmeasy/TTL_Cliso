package com.example.e5322.thyrosoft.Models;

public class MissedcallAPI_Response {

    /**
     * Mobile : 02230900040
     * Mobile1 : 02267123450
     * resId : RES0000
     * response : Success
     */

    private String Mobile;
    private String Mobile1;
    private String resId;
    private String response;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getMobile1() {
        return Mobile1;
    }

    public void setMobile1(String Mobile1) {
        this.Mobile1 = Mobile1;
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
