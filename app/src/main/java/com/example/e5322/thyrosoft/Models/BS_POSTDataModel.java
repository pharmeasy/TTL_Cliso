package com.example.e5322.thyrosoft.Models;

import java.io.File;

public class BS_POSTDataModel {
    private String user;
    private String mobile;
    private String landLine;
    private String name;
    private String age;
    private String value;
    private String test;
    private String email_id;
    private String latitude;
    private String longitude;
    private String gender;
    private String range;
    private String collAmount;
    private String Pincode;
    private File file;
    private String SBP, DBP;

    public BS_POSTDataModel() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getCollAmount() {
        return collAmount;
    }

    public void setCollAmount(String collAmount) {
        this.collAmount = collAmount;
    }

    public String getLandLine() {
        return landLine;
    }

    public void setLandLine(String landLine) {
        this.landLine = landLine;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
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
