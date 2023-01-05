package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class StaffResponseModel implements Parcelable {
    private ArrayList<staffdatamodel> stafflist;
    private String ResId;
    private String Response;


    protected StaffResponseModel(Parcel in) {
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

    public static final Creator<StaffResponseModel> CREATOR = new Creator<StaffResponseModel>() {
        @Override
        public StaffResponseModel createFromParcel(Parcel in) {
            return new StaffResponseModel(in);
        }

        @Override
        public StaffResponseModel[] newArray(int size) {
            return new StaffResponseModel[size];
        }
    };

    public ArrayList<staffdatamodel> getStafflist() {
        return stafflist;
    }

    public void setStafflist(ArrayList<staffdatamodel> stafflist) {
        this.stafflist = stafflist;
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
