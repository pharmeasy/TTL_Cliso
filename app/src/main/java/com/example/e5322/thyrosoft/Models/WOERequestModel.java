package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class WOERequestModel {


    /**
     * api_key : nVBoST2makVMY@gD22CNHj3M5WU4NaGo)bsELEn8lFY=
     * barcodelist : [{"BARCODE":"CVCVEDED","SAMPLE_TYPE":"SODIUM HEPARIN","TESTS":"CG005"}]
     * trfjson : [{"Product":"CG005","trf_image":{"path":"/storage/emulated/0/Pictures/IMG_20200704_174024.jpg"}}]
     * woe : {"AADHAR_NO":"","ADDITIONAL1":"CPL","ADDRESS":"SAKTHI CLINIC(NEAR BUS STAND, ALANGANALLUR, ..625501)","AGE":"11","AGE_TYPE":"Y","ALERT_MESSAGE":"","AMOUNT_COLLECTED":"3400","AMOUNT_DUE":"","APP_ID":"1.0.1.45","BCT_ID":"","BRAND":"TTL-OTHERS","CAMP_ID":"","CONTACT_NO":"","CONT_PERSON":"","CUSTOMER_ID":"","DELIVERY_MODE":2,"EMAIL_ID":"","ENTERED_BY":"TAM03","GENDER":"M","LAB_ADDRESS":"SAKTHI CLINIC(NEAR BUS STAND, ALANGANALLUR, ..625501)","LAB_ID":"181989","LAB_NAME":"SAKTHI CLINIC","LEAD_ID":"","MAIN_SOURCE":"TAM03","ORDER_NO":"","OS":"unknown","PATIENT_NAME":"KAP","PINCODE":"","PRODUCT":"CG005","REF_DR_ID":"","REF_DR_NAME":"SELF","REMARKS":"MOBILE","SPECIMEN_COLLECTION_TIME":"2020-07-04 05:15:00.000","SPECIMEN_SOURCE":"","SR_NO":1,"STATUS":"N","SUB_SOURCE_CODE":"TAM03","TOTAL_AMOUNT":"5000","TYPE":"ILS","ULCcode":"","WATER_SOURCE":"","WO_MODE":"THYROSOFTLITE APP","WO_STAGE":3,"purpose":"mobile application"}
     * woe_type : WOE
     */

    private String api_key;

    private WoeBean woe;
    private String woe_type;
    private List<BarcodelistBean> barcodelist;

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }


    public WoeBean getWoe() {
        return woe;
    }

    public void setWoe(WoeBean woe) {
        this.woe = woe;
    }

    public String getWoe_type() {
        return woe_type;
    }

    public void setWoe_type(String woe_type) {
        this.woe_type = woe_type;
    }

    public List<BarcodelistBean> getBarcodelist() {
        return barcodelist;
    }

    public void setBarcodelist(List<BarcodelistBean> barcodelist) {
        this.barcodelist = barcodelist;
    }

    public static class WoeBean {
        /**
         * AADHAR_NO :
         * ADDITIONAL1 : CPL
         * ADDRESS : SAKTHI CLINIC(NEAR BUS STAND, ALANGANALLUR, ..625501)
         * AGE : 11
         * AGE_TYPE : Y
         * ALERT_MESSAGE :
         * AMOUNT_COLLECTED : 3400
         * AMOUNT_DUE :
         * APP_ID : 1.0.1.45
         * BCT_ID :
         * BRAND : TTL-OTHERS
         * CAMP_ID :
         * CONTACT_NO :
         * CONT_PERSON :
         * CUSTOMER_ID :
         * DELIVERY_MODE : 2
         * EMAIL_ID :
         * ENTERED_BY : TAM03
         * GENDER : M
         * LAB_ADDRESS : SAKTHI CLINIC(NEAR BUS STAND, ALANGANALLUR, ..625501)
         * LAB_ID : 181989
         * LAB_NAME : SAKTHI CLINIC
         * LEAD_ID :
         * MAIN_SOURCE : TAM03
         * ORDER_NO :
         * OS : unknown
         * PATIENT_NAME : KAP
         * PINCODE :
         * PRODUCT : CG005
         * REF_DR_ID :
         * REF_DR_NAME : SELF
         * REMARKS : MOBILE
         * SPECIMEN_COLLECTION_TIME : 2020-07-04 05:15:00.000
         * SPECIMEN_SOURCE :
         * SR_NO : 1
         * STATUS : N
         * SUB_SOURCE_CODE : TAM03
         * TOTAL_AMOUNT : 5000
         * TYPE : ILS
         * ULCcode :
         * WATER_SOURCE :
         * WO_MODE : THYROSOFTLITE APP
         * WO_STAGE : 3
         * purpose : mobile application
         */

        private String AADHAR_NO;
        private String ADDITIONAL1;
        private String ADDRESS;
        private String AGE;
        private String AGE_TYPE;
        private String ALERT_MESSAGE;
        private String AMOUNT_COLLECTED;
        private String AMOUNT_DUE;
        private String APP_ID;
        private String BCT_ID;
        private String BRAND;
        private String CAMP_ID;
        private String CONTACT_NO;
        private String CONT_PERSON;
        private String CUSTOMER_ID;
        private int DELIVERY_MODE;
        private String EMAIL_ID;
        private String ENTERED_BY;
        private String GENDER;
        private String LAB_ADDRESS;
        private String LAB_ID;
        private String LAB_NAME;
        private String LEAD_ID;
        private String MAIN_SOURCE;
        private String ORDER_NO;
        private String OS;
        private String PATIENT_NAME;
        private String PINCODE;
        private String PRODUCT;
        private String REF_DR_ID;
        private String REF_DR_NAME;
        private String REMARKS;
        private String SPECIMEN_COLLECTION_TIME;
        private String SPECIMEN_SOURCE;
        private int SR_NO;
        private String STATUS;
        private String SUB_SOURCE_CODE;
        private String TOTAL_AMOUNT;
        private String TYPE;
        private String ULCcode;
        private String WATER_SOURCE;
        private String WO_MODE;
        private int WO_STAGE;
        private String purpose;

        public String getAADHAR_NO() {
            return AADHAR_NO;
        }

        public void setAADHAR_NO(String AADHAR_NO) {
            this.AADHAR_NO = AADHAR_NO;
        }

        public String getADDITIONAL1() {
            return ADDITIONAL1;
        }

        public void setADDITIONAL1(String ADDITIONAL1) {
            this.ADDITIONAL1 = ADDITIONAL1;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getAGE() {
            return AGE;
        }

        public void setAGE(String AGE) {
            this.AGE = AGE;
        }

        public String getAGE_TYPE() {
            return AGE_TYPE;
        }

        public void setAGE_TYPE(String AGE_TYPE) {
            this.AGE_TYPE = AGE_TYPE;
        }

        public String getALERT_MESSAGE() {
            return ALERT_MESSAGE;
        }

        public void setALERT_MESSAGE(String ALERT_MESSAGE) {
            this.ALERT_MESSAGE = ALERT_MESSAGE;
        }

        public String getAMOUNT_COLLECTED() {
            return AMOUNT_COLLECTED;
        }

        public void setAMOUNT_COLLECTED(String AMOUNT_COLLECTED) {
            this.AMOUNT_COLLECTED = AMOUNT_COLLECTED;
        }

        public String getAMOUNT_DUE() {
            return AMOUNT_DUE;
        }

        public void setAMOUNT_DUE(String AMOUNT_DUE) {
            this.AMOUNT_DUE = AMOUNT_DUE;
        }

        public String getAPP_ID() {
            return APP_ID;
        }

        public void setAPP_ID(String APP_ID) {
            this.APP_ID = APP_ID;
        }

        public String getBCT_ID() {
            return BCT_ID;
        }

        public void setBCT_ID(String BCT_ID) {
            this.BCT_ID = BCT_ID;
        }

        public String getBRAND() {
            return BRAND;
        }

        public void setBRAND(String BRAND) {
            this.BRAND = BRAND;
        }

        public String getCAMP_ID() {
            return CAMP_ID;
        }

        public void setCAMP_ID(String CAMP_ID) {
            this.CAMP_ID = CAMP_ID;
        }

        public String getCONTACT_NO() {
            return CONTACT_NO;
        }

        public void setCONTACT_NO(String CONTACT_NO) {
            this.CONTACT_NO = CONTACT_NO;
        }

        public String getCONT_PERSON() {
            return CONT_PERSON;
        }

        public void setCONT_PERSON(String CONT_PERSON) {
            this.CONT_PERSON = CONT_PERSON;
        }

        public String getCUSTOMER_ID() {
            return CUSTOMER_ID;
        }

        public void setCUSTOMER_ID(String CUSTOMER_ID) {
            this.CUSTOMER_ID = CUSTOMER_ID;
        }

        public int getDELIVERY_MODE() {
            return DELIVERY_MODE;
        }

        public void setDELIVERY_MODE(int DELIVERY_MODE) {
            this.DELIVERY_MODE = DELIVERY_MODE;
        }

        public String getEMAIL_ID() {
            return EMAIL_ID;
        }

        public void setEMAIL_ID(String EMAIL_ID) {
            this.EMAIL_ID = EMAIL_ID;
        }

        public String getENTERED_BY() {
            return ENTERED_BY;
        }

        public void setENTERED_BY(String ENTERED_BY) {
            this.ENTERED_BY = ENTERED_BY;
        }

        public String getGENDER() {
            return GENDER;
        }

        public void setGENDER(String GENDER) {
            this.GENDER = GENDER;
        }

        public String getLAB_ADDRESS() {
            return LAB_ADDRESS;
        }

        public void setLAB_ADDRESS(String LAB_ADDRESS) {
            this.LAB_ADDRESS = LAB_ADDRESS;
        }

        public String getLAB_ID() {
            return LAB_ID;
        }

        public void setLAB_ID(String LAB_ID) {
            this.LAB_ID = LAB_ID;
        }

        public String getLAB_NAME() {
            return LAB_NAME;
        }

        public void setLAB_NAME(String LAB_NAME) {
            this.LAB_NAME = LAB_NAME;
        }

        public String getLEAD_ID() {
            return LEAD_ID;
        }

        public void setLEAD_ID(String LEAD_ID) {
            this.LEAD_ID = LEAD_ID;
        }

        public String getMAIN_SOURCE() {
            return MAIN_SOURCE;
        }

        public void setMAIN_SOURCE(String MAIN_SOURCE) {
            this.MAIN_SOURCE = MAIN_SOURCE;
        }

        public String getORDER_NO() {
            return ORDER_NO;
        }

        public void setORDER_NO(String ORDER_NO) {
            this.ORDER_NO = ORDER_NO;
        }

        public String getOS() {
            return OS;
        }

        public void setOS(String OS) {
            this.OS = OS;
        }

        public String getPATIENT_NAME() {
            return PATIENT_NAME;
        }

        public void setPATIENT_NAME(String PATIENT_NAME) {
            this.PATIENT_NAME = PATIENT_NAME;
        }

        public String getPINCODE() {
            return PINCODE;
        }

        public void setPINCODE(String PINCODE) {
            this.PINCODE = PINCODE;
        }

        public String getPRODUCT() {
            return PRODUCT;
        }

        public void setPRODUCT(String PRODUCT) {
            this.PRODUCT = PRODUCT;
        }

        public String getREF_DR_ID() {
            return REF_DR_ID;
        }

        public void setREF_DR_ID(String REF_DR_ID) {
            this.REF_DR_ID = REF_DR_ID;
        }

        public String getREF_DR_NAME() {
            return REF_DR_NAME;
        }

        public void setREF_DR_NAME(String REF_DR_NAME) {
            this.REF_DR_NAME = REF_DR_NAME;
        }

        public String getREMARKS() {
            return REMARKS;
        }

        public void setREMARKS(String REMARKS) {
            this.REMARKS = REMARKS;
        }

        public String getSPECIMEN_COLLECTION_TIME() {
            return SPECIMEN_COLLECTION_TIME;
        }

        public void setSPECIMEN_COLLECTION_TIME(String SPECIMEN_COLLECTION_TIME) {
            this.SPECIMEN_COLLECTION_TIME = SPECIMEN_COLLECTION_TIME;
        }

        public String getSPECIMEN_SOURCE() {
            return SPECIMEN_SOURCE;
        }

        public void setSPECIMEN_SOURCE(String SPECIMEN_SOURCE) {
            this.SPECIMEN_SOURCE = SPECIMEN_SOURCE;
        }

        public int getSR_NO() {
            return SR_NO;
        }

        public void setSR_NO(int SR_NO) {
            this.SR_NO = SR_NO;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }

        public String getSUB_SOURCE_CODE() {
            return SUB_SOURCE_CODE;
        }

        public void setSUB_SOURCE_CODE(String SUB_SOURCE_CODE) {
            this.SUB_SOURCE_CODE = SUB_SOURCE_CODE;
        }

        public String getTOTAL_AMOUNT() {
            return TOTAL_AMOUNT;
        }

        public void setTOTAL_AMOUNT(String TOTAL_AMOUNT) {
            this.TOTAL_AMOUNT = TOTAL_AMOUNT;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public String getULCcode() {
            return ULCcode;
        }

        public void setULCcode(String ULCcode) {
            this.ULCcode = ULCcode;
        }

        public String getWATER_SOURCE() {
            return WATER_SOURCE;
        }

        public void setWATER_SOURCE(String WATER_SOURCE) {
            this.WATER_SOURCE = WATER_SOURCE;
        }

        public String getWO_MODE() {
            return WO_MODE;
        }

        public void setWO_MODE(String WO_MODE) {
            this.WO_MODE = WO_MODE;
        }

        public int getWO_STAGE() {
            return WO_STAGE;
        }

        public void setWO_STAGE(int WO_STAGE) {
            this.WO_STAGE = WO_STAGE;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }
    }

    public static class BarcodelistBean {
        /**
         * BARCODE : CVCVEDED
         * SAMPLE_TYPE : SODIUM HEPARIN
         * TESTS : CG005
         */

        private String BARCODE;
        private String SAMPLE_TYPE;
        private String TESTS;

        public String getBARCODE() {
            return BARCODE;
        }

        public void setBARCODE(String BARCODE) {
            this.BARCODE = BARCODE;
        }

        public String getSAMPLE_TYPE() {
            return SAMPLE_TYPE;
        }

        public void setSAMPLE_TYPE(String SAMPLE_TYPE) {
            this.SAMPLE_TYPE = SAMPLE_TYPE;
        }

        public String getTESTS() {
            return TESTS;
        }

        public void setTESTS(String TESTS) {
            this.TESTS = TESTS;
        }
    }
}
