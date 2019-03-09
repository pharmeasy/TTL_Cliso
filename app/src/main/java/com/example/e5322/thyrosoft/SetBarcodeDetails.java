package com.example.e5322.thyrosoft;

import android.os.Parcel;
import android.os.Parcelable;

public class SetBarcodeDetails implements Parcelable{
    String specimenType,barcode_number,productType;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public SetBarcodeDetails() {
    }

    protected SetBarcodeDetails(Parcel in) {
        specimenType = in.readString();
        barcode_number = in.readString();
        productType = in.readString();
    }

    public static final Creator<SetBarcodeDetails> CREATOR = new Creator<SetBarcodeDetails>() {
        @Override
        public SetBarcodeDetails createFromParcel(Parcel in) {
            return new SetBarcodeDetails(in);
        }

        @Override
        public SetBarcodeDetails[] newArray(int size) {
            return new SetBarcodeDetails[size];
        }
    };

    public String getSpecimenType() {
        return specimenType;
    }

    public void setSpecimenType(String specimenType) {
        this.specimenType = specimenType;
    }

    public String getBarcode_number() {
        return barcode_number;
    }

    public void setBarcode_number(String barcode_number) {
        this.barcode_number = barcode_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(specimenType);
        dest.writeString(barcode_number);
        dest.writeString(productType);
    }
}
