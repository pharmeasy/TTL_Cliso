package com.example.e5322.thyrosoft.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BSMISModel implements Parcelable {
    ArrayList<BloodSugarEntries> bloodSugarEntries;
    String Response, ResId;

    public BSMISModel() {
    }

    protected BSMISModel(Parcel in) {
        Response = in.readString();
        ResId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Response);
        dest.writeString(ResId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BSMISModel> CREATOR = new Creator<BSMISModel>() {
        @Override
        public BSMISModel createFromParcel(Parcel in) {
            return new BSMISModel(in);
        }

        @Override
        public BSMISModel[] newArray(int size) {
            return new BSMISModel[size];
        }
    };

    public ArrayList<BloodSugarEntries> getBloodSugarEntries() {
        return bloodSugarEntries;
    }

    public void setBloodSugarEntries(ArrayList<BloodSugarEntries> bloodSugarEntries) {
        this.bloodSugarEntries = bloodSugarEntries;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getResId() {
        return ResId;
    }

    public void setResId(String resId) {
        ResId = resId;
    }
}
