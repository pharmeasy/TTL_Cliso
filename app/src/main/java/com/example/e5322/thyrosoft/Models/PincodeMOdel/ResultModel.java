package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by E5560 on 5/29/2018.
 */

public class ResultModel implements Parcelable{

    private ArrayList<AddressComponentsModel> address_components;
    private String formatted_address;

    protected ResultModel(Parcel in) {
        formatted_address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(formatted_address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResultModel> CREATOR = new Creator<ResultModel>() {
        @Override
        public ResultModel createFromParcel(Parcel in) {
            return new ResultModel(in);
        }

        @Override
        public ResultModel[] newArray(int size) {
            return new ResultModel[size];
        }
    };

    public ArrayList<AddressComponentsModel> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(ArrayList<AddressComponentsModel> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public ResultModel() {
    }
}
