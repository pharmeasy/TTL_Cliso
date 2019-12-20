package com.example.e5322.thyrosoft.Models.ResponseModels;

import com.example.e5322.thyrosoft.Models.TrackDetModel;

import java.util.ArrayList;

public class ReportsResponseModel {
    private String RES_ID, pageNo, totalPages, response, reportStatus;
    private ArrayList<TrackDetModel> patients;
    private int persentfiveB2b, totalb2b, logcharge;

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public ArrayList<TrackDetModel> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<TrackDetModel> patients) {
        this.patients = patients;
    }

    public int getPersentfiveB2b() {
        return persentfiveB2b;
    }

    public void setPersentfiveB2b(int persentfiveB2b) {
        this.persentfiveB2b = persentfiveB2b;
    }

    public int getTotalb2b() {
        return totalb2b;
    }

    public void setTotalb2b(int totalb2b) {
        this.totalb2b = totalb2b;
    }

    public int getLogcharge() {
        return logcharge;
    }

    public void setLogcharge(int logcharge) {
        this.logcharge = logcharge;
    }
}
