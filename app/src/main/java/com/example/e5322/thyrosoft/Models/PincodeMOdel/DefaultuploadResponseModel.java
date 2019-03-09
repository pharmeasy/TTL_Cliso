package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class DefaultuploadResponseModel implements Parcelable {
    private ArrayList<DefaultUploaddatamodel> uploadDocumentList;
    private String ResId;
    private String Response;


    protected DefaultuploadResponseModel(Parcel in) {
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

    public static final Creator<DefaultuploadResponseModel> CREATOR = new Creator<DefaultuploadResponseModel>() {
        @Override
        public DefaultuploadResponseModel createFromParcel(Parcel in) {
            return new DefaultuploadResponseModel(in);
        }

        @Override
        public DefaultuploadResponseModel[] newArray(int size) {
            return new DefaultuploadResponseModel[size];
        }
    };

    public ArrayList<DefaultUploaddatamodel> getUploadDocumentList() {
        return uploadDocumentList;
    }

    public void setUploadDocumentList(ArrayList<DefaultUploaddatamodel> uploadDocumentList) {
        this.uploadDocumentList = uploadDocumentList;
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
