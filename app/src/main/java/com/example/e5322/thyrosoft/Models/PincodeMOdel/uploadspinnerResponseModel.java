package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class uploadspinnerResponseModel implements Parcelable {
    private ArrayList<UploadDocumentdatamodel> doclist;
    private String ResId;
    private String Response;


    protected uploadspinnerResponseModel(Parcel in) {
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

    public static final Creator<uploadspinnerResponseModel> CREATOR = new Creator<uploadspinnerResponseModel>() {
        @Override
        public uploadspinnerResponseModel createFromParcel(Parcel in) {
            return new uploadspinnerResponseModel(in);
        }

        @Override
        public uploadspinnerResponseModel[] newArray(int size) {
            return new uploadspinnerResponseModel[size];
        }
    };

    public ArrayList<UploadDocumentdatamodel> getDoclist() {
        return doclist;
    }

    public void setDoclist(ArrayList<UploadDocumentdatamodel> doclist) {
        this.doclist = doclist;
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
