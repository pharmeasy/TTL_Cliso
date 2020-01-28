package com.example.e5322.thyrosoft.RateCalculatorForModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.e5322.thyrosoft.MainModelForAllTests.B2B_MASTERSMainModel;
import com.example.e5322.thyrosoft.MainModelForAllTests.MPL;
import com.example.e5322.thyrosoft.MainModelForAllTests.OUTLAB_TESTLIST_GETALLTESTS;
import com.example.e5322.thyrosoft.MainModelForAllTests.SAMPLE_TYPESList;
import com.example.e5322.thyrosoft.MainModelForAllTests.WHATER_TESTLIST;
import com.example.e5322.thyrosoft.Models.BaseModel;

import java.util.ArrayList;

/**
 * Created by Priyanka on 09-06-2018.
 */

public class RateCalB2B_MASTERS_Main_Model implements Parcelable{

    ArrayList<com.example.e5322.thyrosoft.MainModelForAllTests.MPL> MPL;
    ArrayList<OUTLAB_TESTLIST_GETALLTESTS> OUTLAB_TESTLIST;
    ArrayList<Base_Model_Rate_Calculator> POP ;
    ArrayList<Base_Model_Rate_Calculator> PROFILE;
    ArrayList<SAMPLE_TYPESList> SAMPLE_TYPES;
    ArrayList<Base_Model_Rate_Calculator>  TESTS;
    ArrayList<com.example.e5322.thyrosoft.MainModelForAllTests.WHATER_TESTLIST> WHATER_TESTLIST;


    protected RateCalB2B_MASTERS_Main_Model(Parcel in) {
    }

    public RateCalB2B_MASTERS_Main_Model() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RateCalB2B_MASTERS_Main_Model> CREATOR = new Parcelable.Creator<RateCalB2B_MASTERS_Main_Model>() {
        @Override
        public RateCalB2B_MASTERS_Main_Model createFromParcel(Parcel in) {
            return new RateCalB2B_MASTERS_Main_Model(in);
        }

        @Override
        public RateCalB2B_MASTERS_Main_Model[] newArray(int size) {
            return new RateCalB2B_MASTERS_Main_Model[size];
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

    public ArrayList<Base_Model_Rate_Calculator> getPOP() {
        return POP;
    }

    public void setPOP(ArrayList<Base_Model_Rate_Calculator> POP) {
        this.POP = POP;
    }

    public ArrayList<Base_Model_Rate_Calculator> getPROFILE() {
        return PROFILE;
    }

    public void setPROFILE(ArrayList<Base_Model_Rate_Calculator> PROFILE) {
        this.PROFILE = PROFILE;
    }

    public ArrayList<Base_Model_Rate_Calculator> getTESTS() {
        return TESTS;
    }

    public void setTESTS(ArrayList<Base_Model_Rate_Calculator> TESTS) {
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
