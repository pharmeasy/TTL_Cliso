package com.example.e5322.thyrosoft.Models.RequestModels;

public class PaytmVerifyChecksumRequestModel {

    private String api_key;
    private String ORDERID;
    private String mID;
    private String Company;


    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getORDERID() {
        return ORDERID;
    }

    public void setORDERID(String ORDERID) {
        this.ORDERID = ORDERID;
    }

    public String getMID() {
        return mID;
    }

    public void setMID(String mID) {
        this.mID = mID;
    }
}