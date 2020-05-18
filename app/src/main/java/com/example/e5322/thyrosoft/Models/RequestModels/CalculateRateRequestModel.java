package com.example.e5322.thyrosoft.Models.RequestModels;

public class CalculateRateRequestModel {
    private String apikey,tests,brand;

    public String getApikey() {
        return apikey;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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
