package com.example.e5322.thyrosoft.Models;

public class StartPaytmReqModel {


    /**
     * URLId : 9
     * NarrationId : 2
     * ModeId : 11
     * Amount : 3200
     * OrderNo : {Unique id}
     * SourceCode : TAM03
     * ACCode :
     * TransactionDtls :
     * UserId : 884543163
     */

    private Integer URLId;
    private String NarrationId;
    private String ModeId;
    private String Amount;
    private String OrderNo;
    private String SourceCode;
    private String ACCode;
    private String TransactionDtls;
    private String UserId;
    private String Remarks;

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public Integer getURLId() {
        return URLId;
    }

    public void setURLId(Integer URLId) {
        this.URLId = URLId;
    }

    public String getNarrationId() {
        return NarrationId;
    }

    public void setNarrationId(String NarrationId) {
        this.NarrationId = NarrationId;
    }

    public String getModeId() {
        return ModeId;
    }

    public void setModeId(String ModeId) {
        this.ModeId = ModeId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String SourceCode) {
        this.SourceCode = SourceCode;
    }

    public String getACCode() {
        return ACCode;
    }

    public void setACCode(String ACCode) {
        this.ACCode = ACCode;
    }

    public String getTransactionDtls() {
        return TransactionDtls;
    }

    public void setTransactionDtls(String TransactionDtls) {
        this.TransactionDtls = TransactionDtls;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
}
