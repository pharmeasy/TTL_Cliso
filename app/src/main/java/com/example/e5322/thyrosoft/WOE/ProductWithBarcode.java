package com.example.e5322.thyrosoft.WOE;

import android.os.Parcel;
import android.os.Parcelable;

public  class ProductWithBarcode implements Parcelable {
    String barcode,product,fasting;

    protected ProductWithBarcode(Parcel in) {
        barcode = in.readString();
        product = in.readString();
        fasting = in.readString();
    }

    public ProductWithBarcode() {
    }

    public static final Creator<ProductWithBarcode> CREATOR = new Creator<ProductWithBarcode>() {
        @Override
        public ProductWithBarcode createFromParcel(Parcel in) {
            return new ProductWithBarcode(in);
        }

        @Override
        public ProductWithBarcode[] newArray(int size) {
            return new ProductWithBarcode[size];
        }
    };

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getFasting() {
        return fasting;
    }

    public void setFasting(String fasting) {
        this.fasting = fasting;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barcode);
        dest.writeString(product);
        dest.writeString(fasting);
    }
}
