package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class LeadDataResponseModel {


    /**
     * ADDRESS : E 502 KOMAL APARTMENT MANPADA ROAD NR HANUMAN TEMPLE SAGAON
     * BOOKED_BY : POOJA
     * CUSTOMER_RATE : 400
     * EMAIL : pooja.uke@thyrocare.com
     * FASTING : NON FASTING
     * MOBILE : 9819170000
     * MODE : PAY WHILE SAMPLE COLLECTION
     * ORDERRESPONSE : {"PostOrderDataResponse":[{"AGE":"0","GENDER":"","LEAD_ID":"SP48314761","NAME":"ram"}]}
     * ORDER_NO : LDO629624
     * PAY_TYPE : POSTPAID
     * PRODUCT : T3-T4-TSH
     * REF_ORDERID : 946B37
     * REPORT_HARD_COPY : NO
     * RESPONSE : SUCCESS
     * RES_ID : RES0000
     * SERVICE_TYPE : HOME COLLECTION
     * STATUS : YET TO CONFIRM
     */

    private String ADDRESS;
    private String BOOKED_BY;
    private int CUSTOMER_RATE;
    private String EMAIL;
    private String FASTING;
    private String MOBILE;
    private String MODE;
    private ORDERRESPONSEBean ORDERRESPONSE;
    private String ORDER_NO;
    private String PAY_TYPE;
    private String PRODUCT;
    private String REF_ORDERID;
    private String REPORT_HARD_COPY;
    private String RESPONSE;
    private String RES_ID;
    private String SERVICE_TYPE;
    private String STATUS;

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getBOOKED_BY() {
        return BOOKED_BY;
    }

    public void setBOOKED_BY(String BOOKED_BY) {
        this.BOOKED_BY = BOOKED_BY;
    }

    public int getCUSTOMER_RATE() {
        return CUSTOMER_RATE;
    }

    public void setCUSTOMER_RATE(int CUSTOMER_RATE) {
        this.CUSTOMER_RATE = CUSTOMER_RATE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getFASTING() {
        return FASTING;
    }

    public void setFASTING(String FASTING) {
        this.FASTING = FASTING;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getMODE() {
        return MODE;
    }

    public void setMODE(String MODE) {
        this.MODE = MODE;
    }

    public ORDERRESPONSEBean getORDERRESPONSE() {
        return ORDERRESPONSE;
    }

    public void setORDERRESPONSE(ORDERRESPONSEBean ORDERRESPONSE) {
        this.ORDERRESPONSE = ORDERRESPONSE;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getPAY_TYPE() {
        return PAY_TYPE;
    }

    public void setPAY_TYPE(String PAY_TYPE) {
        this.PAY_TYPE = PAY_TYPE;
    }

    public String getPRODUCT() {
        return PRODUCT;
    }

    public void setPRODUCT(String PRODUCT) {
        this.PRODUCT = PRODUCT;
    }

    public String getREF_ORDERID() {
        return REF_ORDERID;
    }

    public void setREF_ORDERID(String REF_ORDERID) {
        this.REF_ORDERID = REF_ORDERID;
    }

    public String getREPORT_HARD_COPY() {
        return REPORT_HARD_COPY;
    }

    public void setREPORT_HARD_COPY(String REPORT_HARD_COPY) {
        this.REPORT_HARD_COPY = REPORT_HARD_COPY;
    }

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

    public String getSERVICE_TYPE() {
        return SERVICE_TYPE;
    }

    public void setSERVICE_TYPE(String SERVICE_TYPE) {
        this.SERVICE_TYPE = SERVICE_TYPE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public static class ORDERRESPONSEBean {
        private List<PostOrderDataResponseBean> PostOrderDataResponse;

        public List<PostOrderDataResponseBean> getPostOrderDataResponse() {
            return PostOrderDataResponse;
        }

        public void setPostOrderDataResponse(List<PostOrderDataResponseBean> PostOrderDataResponse) {
            this.PostOrderDataResponse = PostOrderDataResponse;
        }

        public static class PostOrderDataResponseBean {
            /**
             * AGE : 0
             * GENDER :
             * LEAD_ID : SP48314761
             * NAME : ram
             */

            private String AGE;
            private String GENDER;
            private String LEAD_ID;
            private String NAME;

            public String getAGE() {
                return AGE;
            }

            public void setAGE(String AGE) {
                this.AGE = AGE;
            }

            public String getGENDER() {
                return GENDER;
            }

            public void setGENDER(String GENDER) {
                this.GENDER = GENDER;
            }

            public String getLEAD_ID() {
                return LEAD_ID;
            }

            public void setLEAD_ID(String LEAD_ID) {
                this.LEAD_ID = LEAD_ID;
            }

            public String getNAME() {
                return NAME;
            }

            public void setNAME(String NAME) {
                this.NAME = NAME;
            }
        }
    }
}
