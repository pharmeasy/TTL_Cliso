package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

/**
 * Created by e5426@thyrocare.com on 18/5/18.
 */

public class TrackDetModel {

    String Downloaded;
    String Ref_By;
    String Tests;
    String barcode;
    ArrayList<CancelledModel> cancel_tests_with_remark;
    String chn_pending;
    ArrayList<chn_test> chn_test_list;
    String chn_test;
    String confirm_status;
    String date;
    String email;
    String isOrder;
    String labcode;
    String leadId;
    String name;
    String patient_id;
    String pdflink;
    String sample_type;
    String scp;
    String sct;
    String su_code2;
    String wo_sl_no;

    public String getDownloaded() {
        return Downloaded;
    }

    public void setDownloaded(String downloaded) {
        Downloaded = downloaded;
    }

    public String getRef_By() {
        return Ref_By;
    }

    public void setRef_By(String ref_By) {
        Ref_By = ref_By;
    }

    public String getTests() {
        return Tests;
    }

    public void setTests(String tests) {
        Tests = tests;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }



    public void setCancel_tests_with_remark(ArrayList<CancelledModel> cancel_tests_with_remark) {
        this.cancel_tests_with_remark = cancel_tests_with_remark;
    }
    public ArrayList<CancelledModel> getCancel_tests_with_remark() {
        return cancel_tests_with_remark;
    }

    public String getChn_pending() {
        return chn_pending;
    }

    public void setChn_pending(String chn_pending) {
        this.chn_pending = chn_pending;
    }

    public String getChn_test() {
        return chn_test;
    }

    public void setChn_test(String chn_test) {
        this.chn_test = chn_test;
    }

    public String getConfirm_status() {
        return confirm_status;
    }

    public void setConfirm_status(String confirm_status) {
        this.confirm_status = confirm_status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(String isOrder) {
        this.isOrder = isOrder;
    }

    public String getLabcode() {
        return labcode;
    }

    public void setLabcode(String labcode) {
        this.labcode = labcode;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPdflink() {
        return pdflink;
    }

    public void setPdflink(String pdflink) {
        this.pdflink = pdflink;
    }

    public String getSample_type() {
        return sample_type;
    }

    public void setSample_type(String sample_type) {
        this.sample_type = sample_type;
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

    public String getSu_code2() {
        return su_code2;
    }

    public void setSu_code2(String su_code2) {
        this.su_code2 = su_code2;
    }

    public String getWo_sl_no() {
        return wo_sl_no;
    }

    public void setWo_sl_no(String wo_sl_no) {
        this.wo_sl_no = wo_sl_no;
    }


    public ArrayList<com.example.e5322.thyrosoft.Models.chn_test> getChn_test_list() {
        return chn_test_list;
    }

    public void setChn_test_list(ArrayList<com.example.e5322.thyrosoft.Models.chn_test> chn_test_list) {
        this.chn_test_list = chn_test_list;
    }

}
