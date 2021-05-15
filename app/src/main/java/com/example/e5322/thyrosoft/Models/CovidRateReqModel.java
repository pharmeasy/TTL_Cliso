package com.example.e5322.thyrosoft.Models;

public class CovidRateReqModel {


    /**
     * usercode : TAM03
     */

    private String usercode;
    private String APIKEY;

    public String getAPIKEY() {
        return APIKEY;
    }

    public void setAPIKEY(String APIKEY) {
        this.APIKEY = APIKEY;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
}
