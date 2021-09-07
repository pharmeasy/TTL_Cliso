package com.example.e5322.thyrosoft.Models;

/**
 * Created by e5233@thyrocare.com on 11/8/18.
 */

public class getInclusionMasterModel {

    private String MCode;
    private String Material_name;
    private String Qty;
    private boolean Selected;

    public String getMCode() {
        return MCode;
    }

    public void setMCode(String MCode) {
        this.MCode = MCode;
    }

    public String getMaterial_name() {
        return Material_name;
    }

    public void setMaterial_name(String material_name) {
        Material_name = material_name;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }
}
