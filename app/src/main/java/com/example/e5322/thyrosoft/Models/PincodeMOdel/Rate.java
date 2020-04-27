package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

public class Rate implements Parcelable
{
    String b2c;

    String b2b;

    Rate(Parcel in) {
        b2c = in.readString();
        b2b = in.readString();
    }

    public  final Creator<Rate> CREATOR = new Creator<Rate>() {
        @Override
        public Rate createFromParcel(Parcel in) {
            return new Rate(in);
        }

        @Override
        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };

    public String getB2c ()
    {
        return b2c;
    }

    public void setB2c (String b2c)
    {
        this.b2c = b2c;
    }

    public String getB2b ()
    {
        return b2b;
    }

    public void setB2b (String b2b)
    {
        this.b2b = b2b;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [b2c = "+b2c+", b2b = "+b2b+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(b2c);
        dest.writeString(b2b);
    }
}

