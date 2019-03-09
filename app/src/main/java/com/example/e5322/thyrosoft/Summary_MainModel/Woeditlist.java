package com.example.e5322.thyrosoft.Summary_MainModel;

import android.os.Parcel;
import android.os.Parcelable;

public class Woeditlist implements Parcelable
{
    public Woeditlist() {
    }

    private Woe woe;

    private String woe_type;

    private String api_key;

    private Barcodelist[] barcodelist;

    protected Woeditlist(Parcel in) {
        woe = in.readParcelable(Woe.class.getClassLoader());
        woe_type = in.readString();
        api_key = in.readString();
    }

    public static final Creator<Woeditlist> CREATOR = new Creator<Woeditlist>() {
        @Override
        public Woeditlist createFromParcel(Parcel in) {
            return new Woeditlist(in);
        }

        @Override
        public Woeditlist[] newArray(int size) {
            return new Woeditlist[size];
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

    public Barcodelist[] getBarcodelist ()
    {
        return barcodelist;
    }

    public void setBarcodelist (Barcodelist[] barcodelist)
    {
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
        dest.writeParcelable(woe, flags);
        dest.writeString(woe_type);
        dest.writeString(api_key);
    }
}

