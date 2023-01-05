package com.example.e5322.thyrosoft.Models;

import java.util.List;

/**
 * Created by e5426@thyrocare.com on 22/5/18.
 */

public class Track_BarcodeModel {


    /**
     * billStatus : PENDING
     * billed : 0
     * bvt : 16-07-2020 11:30
     * collected : 24
     * email :
     * etr : null
     * isOrder : 0
     * kycType : 9865580359
     * leadId : null
     * orderId : null
     * orderNo : null
     * patient : MRS SIVASATHYA (24Y/F)
     * pendingCancelledTests : []
     * portalType : null
     * refBy : DR A PARIMALAM MS DGO
     * reportAddress : (93803),SKV LABORATORY,VINAYAGA CLINICAL LAB, POST OFFICE STREET, USILAMPATTI,,625532
     * resId : RES0000
     * response : SUCCESS
     * rrt : 16-07-2020 12:59
     * sampleDetails : [{"barcode":"J4579049","labcode":"160720994","sampleType":"SERUM","tests":"TSH"}]
     * scp : SKV LABORATORY
     * sct : 15-07-2020 17:00
     * status : null
     * statusFlag : null
     * woeEdit : null
     * woeStage : 3
     * woeTime : 15-07-2020 18:06
     * woiLocation : RPL
     */

    private String billStatus;
    private String billed;
    private String bvt;
    private String collected;
    private String email;
    private Object etr;
    private String isOrder;
    private String kycType;
    private Object leadId;
    private Object orderId;
    private Object orderNo;
    private String patient;
    private Object portalType;
    private String refBy;
    private String reportAddress;
    private String resId;
    private String response;
    private String rrt;
    private String scp;
    private String sct;
    private Object status;
    private Object statusFlag;
    private Object woeEdit;
    private String woeStage;
    private String woeTime;
    private String woiLocation;
    private List<?> pendingCancelledTests;
    private List<SampleDetailsBean> sampleDetails;

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBilled() {
        return billed;
    }

    public void setBilled(String billed) {
        this.billed = billed;
    }

    public String getBvt() {
        return bvt;
    }

    public void setBvt(String bvt) {
        this.bvt = bvt;
    }

    public String getCollected() {
        return collected;
    }

    public void setCollected(String collected) {
        this.collected = collected;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getEtr() {
        return etr;
    }

    public void setEtr(Object etr) {
        this.etr = etr;
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }

    public String getKycType() {
        return kycType;
    }

    public void setKycType(String kycType) {
        this.kycType = kycType;
    }

    public Object getLeadId() {
        return leadId;
    }

    public void setLeadId(Object leadId) {
        this.leadId = leadId;
    }

    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    public Object getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Object orderNo) {
        this.orderNo = orderNo;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public Object getPortalType() {
        return portalType;
    }

    public void setPortalType(Object portalType) {
        this.portalType = portalType;
    }

    public String getRefBy() {
        return refBy;
    }

    public void setRefBy(String refBy) {
        this.refBy = refBy;
    }

    public String getReportAddress() {
        return reportAddress;
    }

    public void setReportAddress(String reportAddress) {
        this.reportAddress = reportAddress;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRrt() {
        return rrt;
    }

    public void setRrt(String rrt) {
        this.rrt = rrt;
    }

    public String getScp() {
        return scp;
    }

    public void setScp(String scp) {
        this.scp = scp;
    }

    public String getSct() {
        return sct;
    }

    public void setSct(String sct) {
        this.sct = sct;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(Object statusFlag) {
        this.statusFlag = statusFlag;
    }

    public Object getWoeEdit() {
        return woeEdit;
    }

    public void setWoeEdit(Object woeEdit) {
        this.woeEdit = woeEdit;
    }

    public String getWoeStage() {
        return woeStage;
    }

    public void setWoeStage(String woeStage) {
        this.woeStage = woeStage;
    }

    public String getWoeTime() {
        return woeTime;
    }

    public void setWoeTime(String woeTime) {
        this.woeTime = woeTime;
    }

    public String getWoiLocation() {
        return woiLocation;
    }

    public void setWoiLocation(String woiLocation) {
        this.woiLocation = woiLocation;
    }

    public List<?> getPendingCancelledTests() {
        return pendingCancelledTests;
    }

    public void setPendingCancelledTests(List<?> pendingCancelledTests) {
        this.pendingCancelledTests = pendingCancelledTests;
    }

    public List<SampleDetailsBean> getSampleDetails() {
        return sampleDetails;
    }

    public void setSampleDetails(List<SampleDetailsBean> sampleDetails) {
        this.sampleDetails = sampleDetails;
    }

    public static class SampleDetailsBean {
        /**
         * barcode : J4579049
         * labcode : 160720994
         * sampleType : SERUM
         * tests : TSH
         */

        private String barcode;
        private String labcode;
        private String sampleType;
        private String tests;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getLabcode() {
            return labcode;
        }

        public void setLabcode(String labcode) {
            this.labcode = labcode;
        }

        public String getSampleType() {
            return sampleType;
        }

        public void setSampleType(String sampleType) {
            this.sampleType = sampleType;
        }

        public String getTests() {
            return tests;
        }

        public void setTests(String tests) {
            this.tests = tests;
        }
    }
}
