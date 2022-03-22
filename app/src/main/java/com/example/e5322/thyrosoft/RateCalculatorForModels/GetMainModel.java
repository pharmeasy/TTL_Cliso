package com.example.e5322.thyrosoft.RateCalculatorForModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.e5322.thyrosoft.Models.IsnhlmasterDTO;

import java.util.ArrayList;

/**
 * Created by Priyanka on 09-06-2018.
 */


public class GetMainModel implements Parcelable {

    public RateCalB2B_MASTERS_Main_Model B2B_MASTERS;
    String MASTERS, RESPONSE, RES_ID, USER_TYPE;
    private ArrayList<IsnhlmasterDTO> isnhlmaster;
    private int bsmin, bsmax, bpmin, bpmax;
    private String bsmsg, bpmsg;

    public GetMainModel() {
    }

    protected GetMainModel(Parcel in) {
        MASTERS = in.readString();
        RESPONSE = in.readString();
        RES_ID = in.readString();
        USER_TYPE = in.readString();
        bsmin = in.readInt();
        bsmax = in.readInt();
        bpmin = in.readInt();
        bpmax = in.readInt();
        bsmsg = in.readString();
        bpmsg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(B2B_MASTERS, flags);
        dest.writeString(MASTERS);
        dest.writeString(RESPONSE);
        dest.writeString(RES_ID);
        dest.writeString(USER_TYPE);
        dest.writeInt(bsmin);
        dest.writeInt(bsmax);
        dest.writeInt(bpmin);
        dest.writeInt(bpmax);
        dest.writeString(bsmsg);
        dest.writeString(bpmsg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetMainModel> CREATOR = new Creator<GetMainModel>() {
        @Override
        public GetMainModel createFromParcel(Parcel in) {
            return new GetMainModel(in);
        }

        @Override
        public GetMainModel[] newArray(int size) {
            return new GetMainModel[size];
        }
    };

    public RateCalB2B_MASTERS_Main_Model getB2B_MASTERS() {
        return B2B_MASTERS;
    }

    public void setB2B_MASTERS(RateCalB2B_MASTERS_Main_Model b2B_MASTERS) {
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

    public int getBsmin() {
        return bsmin;
    }

    public void setBsmin(int bsmin) {
        this.bsmin = bsmin;
    }

    public int getBsmax() {
        return bsmax;
    }

    public void setBsmax(int bsmax) {
        this.bsmax = bsmax;
    }

    public int getBpmin() {
        return bpmin;
    }

    public void setBpmin(int bpmin) {
        this.bpmin = bpmin;
    }

    public int getBpmax() {
        return bpmax;
    }

    public void setBpmax(int bpmax) {
        this.bpmax = bpmax;
    }

    public String getBsmsg() {
        return bsmsg;
    }

    public void setBsmsg(String bsmsg) {
        this.bsmsg = bsmsg;
    }

    public String getBpmsg() {
        return bpmsg;
    }

    public void setBpmsg(String bpmsg) {
        this.bpmsg = bpmsg;
    }
}


