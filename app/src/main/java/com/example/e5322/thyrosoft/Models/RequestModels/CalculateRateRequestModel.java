package com.example.e5322.thyrosoft.Models.RequestModels;

public class CalculateRateRequestModel {
    private String apikey,tests;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }
}
