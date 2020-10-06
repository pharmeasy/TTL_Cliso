package com.example.e5322.thyrosoft.Models;

/**
 * Created by e5426@thyrocare.com on 17/5/18.
 */

public class billingDetailsModel {


    String barcode;
    String billedAmount;
    String collectedAmount;
    String patient;
    String tests;
    String woetype;


    String refId;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(String billedAmount) {
        this.billedAmount = billedAmount;
    }

    public String getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(String collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getWoetype() {
        return woetype;
    }

    public void setWoetype(String woetype) {
        this.woetype = woetype;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

}
