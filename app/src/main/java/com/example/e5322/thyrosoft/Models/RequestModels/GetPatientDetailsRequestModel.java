package com.example.e5322.thyrosoft.Models.RequestModels;

import java.io.Serializable;

public class GetPatientDetailsRequestModel implements Serializable {


    private String Api_key;
    private String Scode;
    private String Mobile;

    public String getApi_key() {
        return Api_key;
    }

    public void setApi_key(String Api_key) {
        this.Api_key = Api_key;
    }

    public String getScode() {
        return Scode;
    }

    public void setScode(String Scode) {
        this.Scode = Scode;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }
}
