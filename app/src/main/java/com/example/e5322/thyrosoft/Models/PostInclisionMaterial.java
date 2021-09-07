package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class PostInclisionMaterial {

    private String LeadId;
    private ArrayList<getInclusionMasterModel> lstMaterial;
    private boolean hasOtherCharges;
    private String otherRemarks;
    private double otherAmount;

    private int Amount;
    private String Mode;
    private String RefNo;
    private String Remarks;
    private String Thyrocare_Api_key ;

    public String getLeadId() {
        return LeadId;
    }

    public void setLeadId(String leadId) {
        LeadId = leadId;
    }

    public ArrayList<getInclusionMasterModel> getLstMaterial() {
        return lstMaterial;
    }

    public void setLstMaterial(ArrayList<getInclusionMasterModel> lstMaterial) {
        this.lstMaterial = lstMaterial;
    }

    public boolean isHasOtherCharges() {
        return hasOtherCharges;
    }

    public void setHasOtherCharges(boolean hasOtherCharges) {
        this.hasOtherCharges = hasOtherCharges;
    }

    public String getOtherRemarks() {
        return otherRemarks;
    }

    public void setOtherRemarks(String otherRemarks) {
        this.otherRemarks = otherRemarks;
    }

    public double getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(double otherAmount) {
        this.otherAmount = otherAmount;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getThyrocare_Api_key() {
        return Thyrocare_Api_key;
    }

    public void setThyrocare_Api_key(String thyrocare_Api_key) {
        Thyrocare_Api_key = thyrocare_Api_key;
    }
}
