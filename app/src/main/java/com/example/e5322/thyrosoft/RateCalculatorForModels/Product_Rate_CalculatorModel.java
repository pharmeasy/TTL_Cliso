package com.example.e5322.thyrosoft.RateCalculatorForModels;

/**
 * Created by Priyanka on 09-06-2018.
 */


import java.util.ArrayList;
/**
 * Created by E5322 on 07-06-2018.
 */

public class Product_Rate_CalculatorModel {

    private String testType;
    private ArrayList<Base_Model_Rate_Calculator> testRateMasterModels;

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public ArrayList<Base_Model_Rate_Calculator> getTestRateMasterModels() {
        return testRateMasterModels;
    }

    public void setTestRateMasterModels(ArrayList<Base_Model_Rate_Calculator> testRateMasterModels) {
        this.testRateMasterModels = testRateMasterModels;
    }

    public Product_Rate_CalculatorModel() {
    }

    public Product_Rate_CalculatorModel(String testType, ArrayList<Base_Model_Rate_Calculator> testRateMasterModels) {
        this.testType = testType;
        this.testRateMasterModels = testRateMasterModels;
    }


}



