package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class RATEnteredResponseModel {


    /**
     * currentTIME : 06-07-2020 16:39
     * patientDETAILS : [{"barcode":"langflee","mobile":"9594275744","name":"Omkar Javeri (90Y/M)","patientID":"COV0167470880991038","resultTIME":null,"resultURL":"","siiURL":"","status":0,"uploadTIME":null},{"barcode":"langfler","mobile":"9594275744","name":"Omkar Javeri (57Y/F)","patientID":"COV0167101980905289","resultTIME":null,"resultURL":"","siiURL":"","status":0,"uploadTIME":null}]
     * resID : RES0000
     * response : SUCCESS
     */

    private String currentTIME;
    private String resID;
    private String response;
    private int timespan;
    private ArrayList<PatientDETAILSBean> patientDETAILS;

    public String getCurrentTIME() {
        return currentTIME;
    }

    public void setCurrentTIME(String currentTIME) {
        this.currentTIME = currentTIME;
    }

    public String getResID() {
        return resID;
    }

    public int getTimespan() {
        return timespan;
    }

    public void setTimespan(int timespan) {
        this.timespan = timespan;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<PatientDETAILSBean> getPatientDETAILS() {
        return patientDETAILS;
    }

    public void setPatientDETAILS(ArrayList<PatientDETAILSBean> patientDETAILS) {
        this.patientDETAILS = patientDETAILS;
    }

    public static class PatientDETAILSBean {
        /**
         * barcode : langflee
         * mobile : 9594275744
         * name : Omkar Javeri (90Y/M)
         * patientID : COV0167470880991038
         * resultTIME : null
         * resultURL :
         * siiURL :
         * status : 0
         * uploadTIME : null
         */

        private String barcode;
        private String mobile;
        private String name;
        private String patientID;
        private String resultTIME;
        private String resultURL;
        private String siiURL;
        private int status;
        private String uploadTIME;
        private String result;
        private boolean isResubmitted;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPatientID() {
            return patientID;
        }

        public void setPatientID(String patientID) {
            this.patientID = patientID;
        }

        public String getResultTIME() {
            return resultTIME;
        }

        public void setResultTIME(String resultTIME) {
            this.resultTIME = resultTIME;
        }

        public String getResultURL() {
            return resultURL;
        }

        public void setResultURL(String resultURL) {
            this.resultURL = resultURL;
        }

        public String getSiiURL() {
            return siiURL;
        }

        public void setSiiURL(String siiURL) {
            this.siiURL = siiURL;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUploadTIME() {
            return uploadTIME;
        }

        public void setUploadTIME(String uploadTIME) {
            this.uploadTIME = uploadTIME;
        }

        public boolean isResubmitted() {
            return isResubmitted;
        }

        public void setResubmitted(boolean resubmitted) {
            isResubmitted = resubmitted;
        }
    }
}
