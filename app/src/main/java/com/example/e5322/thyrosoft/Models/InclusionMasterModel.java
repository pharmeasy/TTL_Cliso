package com.example.e5322.thyrosoft.Models;

/**
 * Created by e5233@thyrocare.com on 11/8/18.
 */

public class InclusionMasterModel {

    private String MCode;
    private String Name;
    private String UnitQty;
    private String UnitCost;
    private String UnitWeight;
    private boolean isSelected;
    private int selectedqty;

    public String getMCode() {
        return MCode;
    }

    public void setMCode(String MCode) {
        this.MCode = MCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUnitQty() {
        return UnitQty;
    }

    public void setUnitQty(String unitQty) {
        UnitQty = unitQty;
    }

    public String getUnitCost() {
        return UnitCost;
    }

    public void setUnitCost(String unitCost) {
        UnitCost = unitCost;
    }

    public String getUnitWeight() {
        return UnitWeight;
    }

    public void setUnitWeight(String unitWeight) {
        UnitWeight = unitWeight;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getSelectedqty() {
        return selectedqty;
    }

    public void setSelectedqty(int selectedqty) {
        this.selectedqty = selectedqty;
    }
}
