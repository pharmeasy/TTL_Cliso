package com.example.e5322.thyrosoft.Models.ResponseModels;

import com.example.e5322.thyrosoft.Models.BillingSummaryMOdel;

import java.util.ArrayList;

public class BillingSummaryResponseModel {
    private String resId,response;
    private ArrayList<BillingSummaryMOdel> billingList;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<BillingSummaryMOdel> getBillingList() {
        return billingList;
    }

    public void setBillingList(ArrayList<BillingSummaryMOdel> billingList) {
        this.billingList = billingList;
    }
}
