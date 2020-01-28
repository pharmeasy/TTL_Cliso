package com.example.e5322.thyrosoft.Summary_MainModel;

import android.os.Parcel;
import android.os.Parcelable;

public class Barcodelist implements Parcelable
{
    private String Vial_Image;

    public Barcodelist() {
    }

    private String SAMPLE_TYPE;

    private String BARCODE;

    private String TESTS;

    protected Barcodelist(Parcel in) {
        Vial_Image = in.readString();
        SAMPLE_TYPE = in.readString();
        BARCODE = in.readString();
        TESTS = in.readString();
    }

    public static final Creator<Barcodelist> CREATOR = new Creator<Barcodelist>() {
        @Override
        public Barcodelist createFromParcel(Parcel in) {
            return new Barcodelist(in);
        }

        @Override
        public Barcodelist[] newArray(int size) {
            return new Barcodelist[size];
        }
    };

    public String getVial_Image ()
{
    return Vial_Image;
}

    public void setVial_Image (String Vial_Image)
    {
        this.Vial_Image = Vial_Image;
    }

    public String getSAMPLE_TYPE ()
    {
        return SAMPLE_TYPE;
    }

    public void setSAMPLE_TYPE (String SAMPLE_TYPE)
    {
        this.SAMPLE_TYPE = SAMPLE_TYPE;
    }

    public String getBARCODE ()
    {
        return BARCODE;
    }

    public void setBARCODE (String BARCODE)
    {
        this.BARCODE = BARCODE;
    }

    public String getTESTS ()
    {
        return TESTS;
    }

    public void setTESTS (String TESTS)
    {
        this.TESTS = TESTS;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Vial_Image = "+Vial_Image+", SAMPLE_TYPE = "+SAMPLE_TYPE+", BARCODE = "+BARCODE+", TESTS = "+TESTS+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Vial_Image);
        dest.writeString(SAMPLE_TYPE);
        dest.writeString(BARCODE);
        dest.writeString(TESTS);
    }
}

