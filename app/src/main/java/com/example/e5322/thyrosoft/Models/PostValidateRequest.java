package com.example.e5322.thyrosoft.Models;

public class PostValidateRequest {


    /**
     * UserCode : TAM03
     * Domain : Cli-So
     * Purpose : Offer
     * Mobile : 9137985277
     */

    private String UserCode;
    private String Domain;
    private String Purpose;
    private String Mobile;

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String Domain) {
        this.Domain = Domain;
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
