package com.example.e5322.thyrosoft.Cliso_BMC.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class MaterialDetailsModel implements Parcelable {

    String MaterialId,MaterialName,OpeningStock,UsedStock;

    public MaterialDetailsModel() {
    }

    protected MaterialDetailsModel(Parcel in) {
        MaterialId = in.readString();
        MaterialName = in.readString();
        OpeningStock = in.readString();
        UsedStock = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MaterialId);
        dest.writeString(MaterialName);
        dest.writeString(OpeningStock);
        dest.writeString(UsedStock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MaterialDetailsModel> CREATOR = new Creator<MaterialDetailsModel>() {
        @Override
        public MaterialDetailsModel createFromParcel(Parcel in) {
            return new MaterialDetailsModel(in);
        }

        @Override
        public MaterialDetailsModel[] newArray(int size) {
            return new MaterialDetailsModel[size];
        }
    };

    public String getMaterialId() {
        return MaterialId;
    }

    public void setMaterialId(String materialId) {
        MaterialId = materialId;
    }

    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String materialName) {
        MaterialName = materialName;
    }

    public String getOpeningStock() {
        return OpeningStock;
    }

    public void setOpeningStock(String openingStock) {
        OpeningStock = openingStock;
    }

    public String getUsedStock() {
        return UsedStock;
    }

    public void setUsedStock(String usedStock) {
        UsedStock = usedStock;
    }
}
