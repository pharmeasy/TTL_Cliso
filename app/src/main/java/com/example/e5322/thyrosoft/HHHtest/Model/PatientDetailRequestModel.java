package com.example.e5322.thyrosoft.HHHtest.Model;

public class PatientDetailRequestModel {


    /**
     * apikey : S)DgxLM7wddh@fJ@Kvz7wT1Zdv1d11AUDZbXECkng2c=
     * sourceCode : COV01
     * status : 0
     * test : CRAT
     * date : 2020-09-09
     */

    private String apikey;
    private String sourceCode;
    private int status;
    private String test;
    private String date;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
