package com.example.e5322.thyrosoft.MainModelForAllTests;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.e5322.thyrosoft.Models.IsnhlmasterDTO;

import java.util.ArrayList;

/**
 * Created by E5322 on 07-06-2018.
 */

public class MainModel implements Parcelable {

    public  B2B_MASTERSMainModel  B2B_MASTERS;
    String MASTERS,RESPONSE,RES_ID,USER_TYPE;
    private ArrayList<IsnhlmasterDTO> isnhlmaster;


    protected MainModel(Parcel in) {
        MASTERS = in.readString();
        RESPONSE = in.readString();
        RES_ID = in.readString();
        USER_TYPE = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MASTERS);
        dest.writeString(RESPONSE);
        dest.writeString(RES_ID);
        dest.writeString(USER_TYPE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainModel> CREATOR = new Creator<MainModel>() {
        @Override
        public MainModel createFromParcel(Parcel in) {
            return new MainModel(in);
        }

        @Override
        public MainModel[] newArray(int size) {
            return new MainModel[size];
        }
    };

    public MainModel() {
    }

    public B2B_MASTERSMainModel getB2B_MASTERS() {
        return B2B_MASTERS;
    }

    public void setB2B_MASTERS(B2B_MASTERSMainModel b2B_MASTERS) {
        B2B_MASTERS = b2B_MASTERS;
    }

    public String getMASTERS() {
        return MASTERS;
    }

    public void setMASTERS(String MASTERS) {
        this.MASTERS = MASTERS;
    }

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getUSER_TYPE() {
        return USER_TYPE;
    }

    public void setUSER_TYPE(String USER_TYPE) {
        this.USER_TYPE = USER_TYPE;
    }

    public ArrayList<IsnhlmasterDTO> getIsnhlmaster() {
        return isnhlmaster;
    }

    public void setIsnhlmaster(ArrayList<IsnhlmasterDTO> isnhlmaster) {
        this.isnhlmaster = isnhlmaster;
    }



}
