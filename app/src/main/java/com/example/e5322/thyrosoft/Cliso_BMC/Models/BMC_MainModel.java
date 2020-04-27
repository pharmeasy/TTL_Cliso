package com.example.e5322.thyrosoft.Cliso_BMC.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BMC_MainModel implements Parcelable {

    public BMC_B2B_MASTERSModel B2B_MASTERS;
    String MASTERS, RESPONSE, RES_ID, USER_TYPE;
    int advancemax,basicmax;

    public BMC_MainModel() {
    }

    protected BMC_MainModel(Parcel in) {
        B2B_MASTERS = in.readParcelable(BMC_B2B_MASTERSModel.class.getClassLoader());
        MASTERS = in.readString();
        RESPONSE = in.readString();
        RES_ID = in.readString();
        USER_TYPE = in.readString();
        advancemax = in.readInt();
        basicmax = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(B2B_MASTERS, flags);
        dest.writeString(MASTERS);
        dest.writeString(RESPONSE);
        dest.writeString(RES_ID);
        dest.writeString(USER_TYPE);
        dest.writeInt(advancemax);
        dest.writeInt(basicmax);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BMC_MainModel> CREATOR = new Creator<BMC_MainModel>() {
        @Override
        public BMC_MainModel createFromParcel(Parcel in) {
            return new BMC_MainModel(in);
        }

        @Override
        public BMC_MainModel[] newArray(int size) {
            return new BMC_MainModel[size];
        }
    };

    public BMC_B2B_MASTERSModel getB2B_MASTERS() {
        return B2B_MASTERS;
    }

    public void setB2B_MASTERS(BMC_B2B_MASTERSModel b2B_MASTERS) {
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

    public int getAdvancemax() {
        return advancemax;
    }

    public void setAdvancemax(int advancemax) {
        this.advancemax = advancemax;
    }

    public int getBasicmax() {
        return basicmax;
    }

    public void setBasicmax(int basicmax) {
        this.basicmax = basicmax;
    }
}
