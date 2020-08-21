package com.example.e5322.thyrosoft.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by e5426@thyrocare.com on 17/5/18.
 */

public class billingDetailsModel  implements Parcelable {


    String barcode;
    String billedAmount;
    String collectedAmount;
    String patient;
    String tests;
    String woetype;


    String refId;

    public billingDetailsModel(Parcel in) {
        barcode = in.readString();
        billedAmount = in.readString();
        collectedAmount = in.readString();
        patient = in.readString();
        tests = in.readString();
        woetype = in.readString();
        refId = in.readString();
    }

    public billingDetailsModel(){

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barcode);
        dest.writeString(billedAmount);
        dest.writeString(collectedAmount);
        dest.writeString(patient);
        dest.writeString(tests);
        dest.writeString(woetype);
        dest.writeString(refId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<billingDetailsModel> CREATOR = new Creator<billingDetailsModel>() {
        @Override
        public billingDetailsModel createFromParcel(Parcel in) {
            return new billingDetailsModel(in);
        }

        @Override
        public billingDetailsModel[] newArray(int size) {
            return new billingDetailsModel[size];
        }
    };

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
