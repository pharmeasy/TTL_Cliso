package com.example.e5322.thyrosoft.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Childs implements Parcelable
{
    String code;

    String type;

    Childs(Parcel in) {
        code = in.readString();
        type = in.readString();
    }

    public  final Creator<Childs> CREATOR = new Creator<Childs>() {
        @Override
        public Childs createFromParcel(Parcel in) {
            return new Childs(in);
        }

        @Override
        public Childs[] newArray(int size) {
            return new Childs[size];
        }
    };

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [code = "+code+", type = "+type+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(type);
    }

}
