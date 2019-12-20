package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */

public class MyPojo {
    private MASTERS MASTERS;

    private String RESPONSE;

    private String ERROR;

    private String RES_ID;

    private String USER_TYPE;

    public MASTERS getMASTERS() {
        return MASTERS;
    }

    public void setMASTERS(MASTERS MASTERS) {
        this.MASTERS = MASTERS;
    }

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getERROR() {
        return ERROR;
    }

    public void setERROR(String ERROR) {
        this.ERROR = ERROR;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getUSER_TYPE() {
        return USER_TYPE;
    }

    public void setUSER_TYPE(String USER_TYPE) {
        this.USER_TYPE = USER_TYPE;
    }

    @Override
    public String toString() {
        return "ClassPojo [MASTERS = " + MASTERS + ", RESPONSE = " + RESPONSE + ", ERROR = " + ERROR + ", RES_ID = " + RES_ID + ", USER_TYPE = " + USER_TYPE + "]";
    }
}

