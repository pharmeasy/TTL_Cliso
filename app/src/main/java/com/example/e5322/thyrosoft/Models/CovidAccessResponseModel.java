package com.example.e5322.thyrosoft.Models;

public class CovidAccessResponseModel {

    private  String response;
    private  String resId;
    private  boolean Rat;
    private  boolean Srf;
    private  boolean CovidRegistration;


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public boolean isRat() {
        return Rat;
    }

    public void setRat(boolean rat) {
        Rat = rat;
    }

    public boolean isSrf() {
        return Srf;
    }

    public void setSrf(boolean srf) {
        Srf = srf;
    }

    public boolean isCovidRegistration() {
        return CovidRegistration;
    }

    public void setCovidRegistration(boolean covidRegistration) {
        CovidRegistration = covidRegistration;
    }
}
