package com.example.e5322.thyrosoft.FinalWoeModelPost;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MyPojoWoe implements Parcelable
{
    private Woe woe;

    public MyPojoWoe() {
    }

    private String woe_type;

    private String api_key;


    ArrayList<BarcodelistModel> barcodelist;
//    private Barcodelist[] barcodelist;

    protected MyPojoWoe(Parcel in) {
        woe_type = in.readString();
        api_key = in.readString();
    }

    public static final Creator<MyPojoWoe> CREATOR = new Creator<MyPojoWoe>() {
        @Override
        public MyPojoWoe createFromParcel(Parcel in) {
            return new MyPojoWoe(in);
        }

        @Override
        public MyPojoWoe[] newArray(int size) {
            return new MyPojoWoe[size];
        }
    };

    public Woe getWoe ()
    {
        return woe;
    }

    public void setWoe (Woe woe)
    {
        this.woe = woe;
    }

    public String getWoe_type ()
    {
        return woe_type;
    }

    public void setWoe_type (String woe_type)
    {
        this.woe_type = woe_type;
    }

    public String getApi_key ()
    {
        return api_key;
    }

    public void setApi_key (String api_key)
    {
        this.api_key = api_key;
    }


    public ArrayList<BarcodelistModel> getBarcodelist() {
        return barcodelist;
    }

    public void setBarcodelistModel(ArrayList<BarcodelistModel> barcodelist) {
        this.barcodelist = barcodelist;
    }

    @Override
    public String toString()

    {
        return "ClassPojo [woe = "+woe+", woe_type = "+woe_type+", api_key = "+api_key+", barcodelist = "+barcodelist+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(woe_type);
        dest.writeString(api_key);
    }


}


