package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.util.ArrayList;

public class TestDetailsResponseModel {
    private String response;
    private ArrayList<String> TestName;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<String> getTestName() {
        return TestName;
    }

    public void setTestName(ArrayList<String> testName) {
        TestName = testName;
    }
}
