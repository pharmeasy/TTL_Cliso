package com.example.e5322.thyrosoft.Models;

public class RATEnteredRequestModel {


    /**
     * apikey : C8o0fn9QeFE)o)gdxUK@l63IH9wqbYgmq4fb7bwp3Tc=
     * sourceCODE : COV01
     * toDATE : 2020-07-06
     * status : N
     * test : COVAG
     */

    private String apikey;
    private String sourceCODE;
    private String toDATE;
    private String status;
    private String test;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSourceCODE() {
        return sourceCODE;
    }

    public void setSourceCODE(String sourceCODE) {
        this.sourceCODE = sourceCODE;
    }

    public String getToDATE() {
        return toDATE;
    }

    public void setToDATE(String toDATE) {
        this.toDATE = toDATE;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
