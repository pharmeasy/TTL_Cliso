package com.example.e5322.thyrosoft.Models;

public class CovidAccessResponseModel {

    private  String response;
    private  String resId;
    private  boolean Rat;
    private  boolean Srf;
    private  boolean CovidRegistration;
    private  boolean DRC;
    private  boolean CovidOtpAllow;

    public boolean isCovidOtpAllow() {
        return CovidOtpAllow;
    }

    public void setCovidOtpAllow(boolean covidOtpAllow) {
        CovidOtpAllow = covidOtpAllow;
    }

    public boolean isDRC() {
        return DRC;
    }

    public void setDRC(boolean DRC) {
        this.DRC = DRC;
    }

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
