package com.example.e5322.thyrosoft.Models;

/**
 * Created by e5426@thyrocare.com on 16/5/18.
 */

public class BillingSummaryMOdel {
    String billedAmount;
    String billingDate;
    String workLoad;



    public String getBilledAmount() {
        return billedAmount;
    }

    public void setBilledAmount(String billedAmount) {
        this.billedAmount = billedAmount;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

    public String getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(String workLoad) {
        this.workLoad = workLoad;
    }


}
