package com.example.e5322.thyrosoft.FinalWoeModelPost;


import android.os.Parcel;
import android.os.Parcelable;

public class BarcodelistModel implements Parcelable
{
    private String SAMPLE_TYPE;

    private String BARCODE;

    private String TESTS;

    public BarcodelistModel() {
    }

    protected BarcodelistModel(Parcel in) {
        SAMPLE_TYPE = in.readString();
        BARCODE = in.readString();
        TESTS = in.readString();
    }

    public static final Creator<BarcodelistModel> CREATOR = new Creator<BarcodelistModel>() {
        @Override
        public BarcodelistModel createFromParcel(Parcel in) {
            return new BarcodelistModel(in);
        }

        @Override
        public BarcodelistModel[] newArray(int size) {
            return new BarcodelistModel[size];
        }
    };

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
        return "ClassPojo [SAMPLE_TYPE = "+SAMPLE_TYPE+", BARCODE = "+BARCODE+", TESTS = "+TESTS+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SAMPLE_TYPE);
        dest.writeString(BARCODE);
        dest.writeString(TESTS);
    }
}

