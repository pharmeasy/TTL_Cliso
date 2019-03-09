package com.example.e5322.thyrosoft.MainModelForAllTests;

import com.example.e5322.thyrosoft.Models.BaseModel;

import java.util.ArrayList;

/**
 * Created by E5322 on 07-06-2018.
 */

public class Product_Rate_MasterModel {

    private String testType;
    private ArrayList<BaseModel> testRateMasterModels;

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public ArrayList<BaseModel> getTestRateMasterModels() {
        return testRateMasterModels;
    }

    public void setTestRateMasterModels(ArrayList<BaseModel> testRateMasterModels) {
        this.testRateMasterModels = testRateMasterModels;
    }

    public Product_Rate_MasterModel() {
    }

    public Product_Rate_MasterModel(String testType, ArrayList<BaseModel> testRateMasterModels) {
        this.testType = testType;
        this.testRateMasterModels = testRateMasterModels;
    }
}


