package com.example.e5322.thyrosoft.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class tspspinnerResponseModel implements Parcelable {
    private ArrayList<tspsinnerdatamodel> tsplist;
    private String ResId;
    private String Response;


    protected tspspinnerResponseModel(Parcel in) {
        ResId = in.readString();
        Response = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ResId);
        dest.writeString(Response);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<tspspinnerResponseModel> CREATOR = new Creator<tspspinnerResponseModel>() {
        @Override
        public tspspinnerResponseModel createFromParcel(Parcel in) {
            return new tspspinnerResponseModel(in);
        }

        @Override
        public tspspinnerResponseModel[] newArray(int size) {
            return new tspspinnerResponseModel[size];
        }
    };

    public ArrayList<tspsinnerdatamodel> getTsplist() {
        return tsplist;
    }

    public void setTsplist(ArrayList<tspsinnerdatamodel> tsplist) {
        this.tsplist = tsplist;
    }

    public String getResId() {
        return ResId;
    }

    public void setResId(String resId) {
        ResId = resId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
