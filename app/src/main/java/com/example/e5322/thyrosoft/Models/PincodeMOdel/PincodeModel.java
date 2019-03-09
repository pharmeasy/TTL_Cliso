package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by E5560 on 5/29/2018.
 */

public class PincodeModel implements Parcelable{

    private ArrayList<ResultModel> results;
    private String status;

    protected PincodeModel(Parcel in) {
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PincodeModel> CREATOR = new Creator<PincodeModel>() {
        @Override
        public PincodeModel createFromParcel(Parcel in) {
            return new PincodeModel(in);
        }

        @Override
        public PincodeModel[] newArray(int size) {
            return new PincodeModel[size];
        }
    };

    public ArrayList<ResultModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultModel> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PincodeModel() {
    }
}
