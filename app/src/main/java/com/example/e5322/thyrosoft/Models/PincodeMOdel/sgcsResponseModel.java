package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class sgcsResponseModel implements Parcelable {
    private ArrayList<sgcdatamodel> sgclist;
    private String ResId;
    private String Response;


    protected sgcsResponseModel(Parcel in) {
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

    public static final Creator<sgcsResponseModel> CREATOR = new Creator<sgcsResponseModel>() {
        @Override
        public sgcsResponseModel createFromParcel(Parcel in) {
            return new sgcsResponseModel(in);
        }

        @Override
        public sgcsResponseModel[] newArray(int size) {
            return new sgcsResponseModel[size];
        }
    };

    public ArrayList<sgcdatamodel> getSgclist() {
        return sgclist;
    }

    public void setSgclist(ArrayList<sgcdatamodel> sgclist) {
        this.sgclist = sgclist;
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
