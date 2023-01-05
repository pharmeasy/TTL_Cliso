package com.example.e5322.thyrosoft.Models;

public class VerifyPayUreqModel {


    /**
     * URLId : 20
     * ModeId : 12
     * OrderNo : {unique Id}
     * TransactionId :
     */

    private Integer URLId;
    private String ModeId;
    private String OrderNo;
    private String TransactionId;

    public Integer getURLId() {
        return URLId;
    }

    public void setURLId(Integer URLId) {
        this.URLId = URLId;
    }

    public String getModeId() {
        return ModeId;
    }

    public void setModeId(String ModeId) {
        this.ModeId = ModeId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String TransactionId) {
        this.TransactionId = TransactionId;
    }
}
