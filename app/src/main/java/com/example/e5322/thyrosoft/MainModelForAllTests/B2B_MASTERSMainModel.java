package com.example.e5322.thyrosoft.MainModelForAllTests;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.e5322.thyrosoft.Models.BaseModel;

import java.util.ArrayList;

/**
 * Created by E5322 on 07-06-2018.
 */

public class B2B_MASTERSMainModel implements Parcelable{
    ArrayList<MPL> MPL;
    ArrayList<OUTLAB_TESTLIST_GETALLTESTS> OUTLAB_TESTLIST;
    ArrayList<BaseModel> POP ;
    ArrayList<BaseModel> PROFILE;
    ArrayList<SAMPLE_TYPESList> SAMPLE_TYPES;
    ArrayList<BaseModel>  TESTS;
    ArrayList<BaseModel> SMT;
    ArrayList<WHATER_TESTLIST> WHATER_TESTLIST;


    protected B2B_MASTERSMainModel(Parcel in) {
    }

    public B2B_MASTERSMainModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<B2B_MASTERSMainModel> CREATOR = new Creator<B2B_MASTERSMainModel>() {
        @Override
        public B2B_MASTERSMainModel createFromParcel(Parcel in) {
            return new B2B_MASTERSMainModel(in);
        }

        @Override
        public B2B_MASTERSMainModel[] newArray(int size) {
            return new B2B_MASTERSMainModel[size];
        }
    };

    public ArrayList<com.example.e5322.thyrosoft.MainModelForAllTests.MPL> getMPL() {
        return MPL;
    }

    public void setMPL(ArrayList<com.example.e5322.thyrosoft.MainModelForAllTests.MPL> MPL) {
        this.MPL = MPL;
    }

    public ArrayList<OUTLAB_TESTLIST_GETALLTESTS> getOUTLAB_TESTLIST() {
        return OUTLAB_TESTLIST;
    }

    public void setOUTLAB_TESTLIST(ArrayList<OUTLAB_TESTLIST_GETALLTESTS> OUTLAB_TESTLIST) {
        this.OUTLAB_TESTLIST = OUTLAB_TESTLIST;
    }

    public ArrayList<BaseModel> getPOP() {
        return POP;
    }

    public ArrayList<BaseModel> getSMT() {
        return SMT;
    }

    public void setSMT(ArrayList<BaseModel> SMT) {
        this.SMT = SMT;
    }

    public void setPOP(ArrayList<BaseModel> POP) {
        this.POP = POP;
    }

    public ArrayList<BaseModel> getPROFILE() {
        return PROFILE;
    }

    public void setPROFILE(ArrayList<BaseModel> PROFILE) {
        this.PROFILE = PROFILE;
    }

    public ArrayList<BaseModel> getTESTS() {
        return TESTS;
    }

    public void setTESTS(ArrayList<BaseModel> TESTS) {
        this.TESTS = TESTS;
    }

    public ArrayList<com.example.e5322.thyrosoft.MainModelForAllTests.WHATER_TESTLIST> getWHATER_TESTLIST() {
        return WHATER_TESTLIST;
    }

    public void setWHATER_TESTLIST(ArrayList<com.example.e5322.thyrosoft.MainModelForAllTests.WHATER_TESTLIST> WHATER_TESTLIST) {
        this.WHATER_TESTLIST = WHATER_TESTLIST;
    }

    public ArrayList<SAMPLE_TYPESList> getSAMPLE_TYPES() {
        return SAMPLE_TYPES;
    }

    public void setSAMPLE_TYPES(ArrayList<SAMPLE_TYPESList> SAMPLE_TYPES) {
        this.SAMPLE_TYPES = SAMPLE_TYPES;
    }
}
