package com.example.e5322.thyrosoft.WorkOrder_entry_Model;

import java.util.ArrayList;

public class WOE_Model_Patient_Details  {
    private String response;

    public ArrayList<Patients> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patients> patients) {
        this.patients = patients;
    }

    ArrayList<Patients> patients;
//    Patients[] patients;

    private String pageNo;

    private String reportStatus;

    private String RES_ID;

    private String totalPages;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getPageNo ()
    {
        return pageNo;
    }

    public void setPageNo (String pageNo)
    {
        this.pageNo = pageNo;
    }

    public String getReportStatus ()
    {
        return reportStatus;
    }

    public void setReportStatus (String reportStatus)
    {
        this.reportStatus = reportStatus;
    }

    public String getRES_ID ()
    {
        return RES_ID;
    }

    public void setRES_ID (String RES_ID)
    {
        this.RES_ID = RES_ID;
    }

    public String getTotalPages ()
    {
        return totalPages;
    }

    public void setTotalPages (String totalPages)
    {
        this.totalPages = totalPages;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", patients = "+patients+", pageNo = "+pageNo+", reportStatus = "+reportStatus+", RES_ID = "+RES_ID+", totalPages = "+totalPages+"]";
    }
}
