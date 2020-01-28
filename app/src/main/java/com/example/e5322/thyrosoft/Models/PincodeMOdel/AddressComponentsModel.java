package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by E5560 on 5/29/2018.
 */

public class AddressComponentsModel implements Parcelable{

    private String long_name;
    private String short_name;
    private ArrayList<String> types;

    protected AddressComponentsModel(Parcel in) {
        long_name = in.readString();
        short_name = in.readString();
        types = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(long_name);
        dest.writeString(short_name);
        dest.writeStringList(types);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressComponentsModel> CREATOR = new Creator<AddressComponentsModel>() {
        @Override
        public AddressComponentsModel createFromParcel(Parcel in) {
            return new AddressComponentsModel(in);
        }

        @Override
        public AddressComponentsModel[] newArray(int size) {
            return new AddressComponentsModel[size];
        }
    };

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public AddressComponentsModel() {
    }
}
