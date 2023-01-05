package com.example.e5322.thyrosoft.Models;

public class PayTmChecksumResponseModel {


    /**
     * RESPONSE : SUCCESS
     * RES_ID : RES0001
     * TID : null
     * encCheckSum : gKN0zlZKJj2eGzHV5qe+w17tgnNCstszNso99slCKZZqtBMSJJyfVjoudqeJkjecomMeLtFDpA2+YKyROQ8omJJeEZ2w2jES97zjPU2wBTA=
     */

    private String RESPONSE;
    private String RES_ID;
    private Object TID;
    private String encCheckSum;
    private String TransToken;

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public Object getTID() {
        return TID;
    }

    public void setTID(Object TID) {
        this.TID = TID;
    }

    public String getEncCheckSum() {
        return encCheckSum;
    }

    public void setEncCheckSum(String encCheckSum) {
        this.encCheckSum = encCheckSum;
    }

    public String getTransToken() {
        return TransToken;
    }

    public void setTransToken(String transToken) {
        TransToken = transToken;
    }
}
