package com.example.e5322.thyrosoft.Cliso_BMC.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BMC_B2B_MASTERSModel implements Parcelable {
    ArrayList<BMC_BaseModel> POP;
    ArrayList<BMC_BaseModel> PROFILE;
    ArrayList<BMC_BaseModel> TESTS;
    ArrayList<BMC_BaseModel> TTLOthers;

    public BMC_B2B_MASTERSModel() {
    }

    protected BMC_B2B_MASTERSModel(Parcel in) {
        POP = in.createTypedArrayList(BMC_BaseModel.CREATOR);
        PROFILE = in.createTypedArrayList(BMC_BaseModel.CREATOR);
        TESTS = in.createTypedArrayList(BMC_BaseModel.CREATOR);
        TTLOthers = in.createTypedArrayList(BMC_BaseModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(POP);
        dest.writeTypedList(PROFILE);
        dest.writeTypedList(TESTS);
        dest.writeTypedList(TTLOthers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BMC_B2B_MASTERSModel> CREATOR = new Creator<BMC_B2B_MASTERSModel>() {
        @Override
        public BMC_B2B_MASTERSModel createFromParcel(Parcel in) {
            return new BMC_B2B_MASTERSModel(in);
        }

        @Override
        public BMC_B2B_MASTERSModel[] newArray(int size) {
            return new BMC_B2B_MASTERSModel[size];
        }
    };

    public ArrayList<BMC_BaseModel> getPOP() {
        return POP;
    }

    public void setPOP(ArrayList<BMC_BaseModel> POP) {
        this.POP = POP;
    }

    public ArrayList<BMC_BaseModel> getPROFILE() {
        return PROFILE;
    }

    public void setPROFILE(ArrayList<BMC_BaseModel> PROFILE) {
        this.PROFILE = PROFILE;
    }

    public ArrayList<BMC_BaseModel> getTESTS() {
        return TESTS;
    }

    public void setTESTS(ArrayList<BMC_BaseModel> TESTS) {
        this.TESTS = TESTS;
    }

    public ArrayList<BMC_BaseModel> getTTLOthers() {
        return TTLOthers;
    }

    public void setTTLOthers(ArrayList<BMC_BaseModel> TTLOthers) {
        this.TTLOthers = TTLOthers;
    }
}
