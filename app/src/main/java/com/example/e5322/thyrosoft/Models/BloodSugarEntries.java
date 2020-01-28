package com.example.e5322.thyrosoft.Models;

public class BloodSugarEntries {

    private String Name, EmailId, Age, Gender, Mobile, Test, testValue, FileUrl, AmountCollected, SBP, DBP;

    public BloodSugarEntries() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }

    public String getAmountCollected() {
        return AmountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        AmountCollected = amountCollected;
    }

    public String getSBP() {
        return SBP;
    }

    public void setSBP(String SBP) {
        this.SBP = SBP;
    }

    public String getDBP() {
        return DBP;
    }

    public void setDBP(String DBP) {
        this.DBP = DBP;
    }
}
