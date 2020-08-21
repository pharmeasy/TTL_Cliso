package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.os.Parcel;
import android.os.Parcelable;

public class CommunicationMaster implements Parcelable
{
    private String id;

    private String isBarcode;

    private String forwardTo;

    private String displayName;

    protected CommunicationMaster(Parcel in) {
        id = in.readString();
        isBarcode = in.readString();
        forwardTo = in.readString();
        displayName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(isBarcode);
        dest.writeString(forwardTo);
        dest.writeString(displayName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommunicationMaster> CREATOR = new Creator<CommunicationMaster>() {
        @Override
        public CommunicationMaster createFromParcel(Parcel in) {
            return new CommunicationMaster(in);
        }

        @Override
        public CommunicationMaster[] newArray(int size) {
            return new CommunicationMaster[size];
        }
    };

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIsBarcode ()
    {
        return isBarcode;
    }

    public void setIsBarcode (String isBarcode)
    {
        this.isBarcode = isBarcode;
    }

    public String getForwardTo ()
    {
        return forwardTo;
    }

    public void setForwardTo (String forwardTo)
    {
        this.forwardTo = forwardTo;
    }

    public String getDisplayName ()
    {
        return displayName;
    }

    public void setDisplayName (String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", isBarcode = "+isBarcode+", forwardTo = "+forwardTo+", displayName = "+displayName+"]";
    }
}

