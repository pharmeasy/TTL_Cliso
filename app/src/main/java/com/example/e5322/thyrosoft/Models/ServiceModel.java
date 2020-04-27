package com.example.e5322.thyrosoft.Models;

public class ServiceModel {


    /**
     * ServiceId : F
     * ServiceName : 18F-PSMA PET CT
     * Amount : 18000
     * Min_Amount : null
     * Category : PET
     * HVC_Rate : 0
     * Old_Rate : 0
     */

    private String ServiceId;
    private String ServiceName;
    private int Amount;
    private Object Min_Amount;
    private String Category;
    private int HVC_Rate;
    private int Old_Rate;
    private int NHF_Rate;

    public int getNHF_Rate() {
        return NHF_Rate;
    }

    public void setNHF_Rate(int NHF_Rate) {
        this.NHF_Rate = NHF_Rate;
    }

    public String getServiceId() {
        return ServiceId;
    }

    public void setServiceId(String ServiceId) {
        this.ServiceId = ServiceId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String ServiceName) {
        this.ServiceName = ServiceName;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int Amount) {
        this.Amount = Amount;
    }

    public Object getMin_Amount() {
        return Min_Amount;
    }

    public void setMin_Amount(Object Min_Amount) {
        this.Min_Amount = Min_Amount;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public int getHVC_Rate() {
        return HVC_Rate;
    }

    public void setHVC_Rate(int HVC_Rate) {
        this.HVC_Rate = HVC_Rate;
    }

    public int getOld_Rate() {
        return Old_Rate;
    }

    public void setOld_Rate(int Old_Rate) {
        this.Old_Rate = Old_Rate;
    }
}
