package com.example.e5322.thyrosoft.Models;

public class BSTestDataModel {

    private int minVal;
    private int maxVal;
    private String RangeVal;
    private String TestName;

    public String getTestName() {
        return TestName;
    }

    public void setTestName(String testName) {
        TestName = testName;
    }

    public int getMinVal() {
        return minVal;
    }

    public void setMinVal(int minVal) {
        this.minVal = minVal;
    }

    public int getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(int maxVal) {
        this.maxVal = maxVal;
    }

    public String getRangeVal() {
        return RangeVal;
    }

    public void setRangeVal(String rangeVal) {
        RangeVal = rangeVal;
    }

    public BSTestDataModel()
    {

    }

    public BSTestDataModel(String name) {
        this.TestName = name;
    }

    @Override
    public String toString() {
        return TestName;
    }

}
