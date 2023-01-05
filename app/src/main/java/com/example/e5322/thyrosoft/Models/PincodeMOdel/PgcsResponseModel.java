package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class PgcsResponseModel implements Parcelable {
    private ArrayList<pgcdatamodel> pgclist;
    private String ResId;
    private String Response;


    protected PgcsResponseModel(Parcel in) {
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

    public static final Creator<PgcsResponseModel> CREATOR = new Creator<PgcsResponseModel>() {
        @Override
        public PgcsResponseModel createFromParcel(Parcel in) {
            return new PgcsResponseModel(in);
        }

        @Override
        public PgcsResponseModel[] newArray(int size) {
            return new PgcsResponseModel[size];
        }
    };

    public ArrayList<pgcdatamodel> getPgclist() {
        return pgclist;
    }

    public void setPgclist(ArrayList<pgcdatamodel> pgclist) {
        this.pgclist = pgclist;
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
