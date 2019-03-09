package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class NedResponseModel implements Parcelable {
    private ArrayList<Neddatamodel> nedlist;
    private String ResId;
    private String Response;


    protected NedResponseModel(Parcel in) {
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

    public static final Creator<NedResponseModel> CREATOR = new Creator<NedResponseModel>() {
        @Override
        public NedResponseModel createFromParcel(Parcel in) {
            return new NedResponseModel(in);
        }

        @Override
        public NedResponseModel[] newArray(int size) {
            return new NedResponseModel[size];
        }
    };

    public ArrayList<Neddatamodel> getNedlist() {
        return nedlist;
    }

    public void setNedlist(ArrayList<Neddatamodel> nedlist) {
        this.nedlist = nedlist;
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
