package com.example.e5322.thyrosoft;

import android.os.Parcel;
import android.os.Parcelable;

public class ScannedBarcodeDetails implements Parcelable {
    String SAMPLE_TYPE;
    String BARCODE;
    String TESTS;
    String Remark;

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public ScannedBarcodeDetails() {
    }

    protected ScannedBarcodeDetails(Parcel in) {
        SAMPLE_TYPE = in.readString();
        BARCODE = in.readString();
        TESTS = in.readString();
        Remark = in.readString();
    }

    public static final Creator<ScannedBarcodeDetails> CREATOR = new Creator<ScannedBarcodeDetails>() {
        @Override
        public ScannedBarcodeDetails createFromParcel(Parcel in) {
            return new ScannedBarcodeDetails(in);
        }

        @Override
        public ScannedBarcodeDetails[] newArray(int size) {
            return new ScannedBarcodeDetails[size];
        }
    };

    public String getBarcode() {
        return BARCODE;
    }

    public void setBarcode(String barcode) {
        this.BARCODE = barcode;
    }

    public String getProducts() {
        return TESTS;
    }

    public void setProducts(String products) {
        this.TESTS = products;
    }

    public String getSpecimen_type() {
        return SAMPLE_TYPE;
    }

    public void setSpecimen_type(String specimen_type) {
        this.SAMPLE_TYPE = specimen_type;
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
        dest.writeString(Remark);
    }
}
