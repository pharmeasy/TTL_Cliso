package com.example.e5322.thyrosoft.Models;

public class GetLeadgerBalnce {


    /**
     * TSP : NF086
     * CL : 100000
     * CB : 24000
     * TodayBill : 16000
     * RD_PAGE : tsp.asp
     * BLOCK_TYPE :
     * TotalPayAmount : 40000
     * Balance : 60000
     * RESPONSE : ALLOW
     */

    private String TSP;
    private Integer CL;
    private Integer CB;
    private Integer TodayBill;
    private String RD_PAGE;
    private String BLOCK_TYPE;
    private Integer TotalPayAmount;
    private Integer Balance;
    private String RESPONSE;

    public String getTSP() {
        return TSP;
    }

    public void setTSP(String TSP) {
        this.TSP = TSP;
    }

    public Integer getCL() {
        return CL;
    }

    public void setCL(Integer CL) {
        this.CL = CL;
    }

    public Integer getCB() {
        return CB;
    }

    public void setCB(Integer CB) {
        this.CB = CB;
    }

    public Integer getTodayBill() {
        return TodayBill;
    }

    public void setTodayBill(Integer TodayBill) {
        this.TodayBill = TodayBill;
    }

    public String getRD_PAGE() {
        return RD_PAGE;
    }

    public void setRD_PAGE(String RD_PAGE) {
        this.RD_PAGE = RD_PAGE;
    }

    public String getBLOCK_TYPE() {
        return BLOCK_TYPE;
    }

    public void setBLOCK_TYPE(String BLOCK_TYPE) {
        this.BLOCK_TYPE = BLOCK_TYPE;
    }

    public Integer getTotalPayAmount() {
        return TotalPayAmount;
    }

    public void setTotalPayAmount(Integer TotalPayAmount) {
        this.TotalPayAmount = TotalPayAmount;
    }

    public Integer getBalance() {
        return Balance;
    }

    public void setBalance(Integer Balance) {
        this.Balance = Balance;
    }

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }
}
