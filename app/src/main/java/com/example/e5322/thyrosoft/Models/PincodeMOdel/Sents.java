package com.example.e5322.thyrosoft.Models.PincodeMOdel;

public class Sents
{
    private String responseBy;

    private String response;

    private String status;

    private String communication;

    private String department;

    private String commId;

    private String commDate;

    private String TAT;

    private String resDate;

    public String getResponseBy ()
    {
        return responseBy;
    }

    public void setResponseBy (String responseBy)
    {
        this.responseBy = responseBy;
    }

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getCommunication ()
    {
        return communication;
    }

    public void setCommunication (String communication)
    {
        this.communication = communication;
    }

    public String getDepartment ()
    {
        return department;
    }

    public void setDepartment (String department)
    {
        this.department = department;
    }

    public String getCommId ()
    {
        return commId;
    }

    public void setCommId (String commId)
    {
        this.commId = commId;
    }

    public String getCommDate ()
    {
        return commDate;
    }

    public void setCommDate (String commDate)
    {
        this.commDate = commDate;
    }

    public String getTAT ()
    {
        return TAT;
    }

    public void setTAT (String TAT)
    {
        this.TAT = TAT;
    }

    public String getResDate ()
    {
        return resDate;
    }

    public void setResDate (String resDate)
    {
        this.resDate = resDate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [responseBy = "+responseBy+", response = "+response+", status = "+status+", communication = "+communication+", department = "+department+", commId = "+commId+", commDate = "+commDate+", TAT = "+TAT+", resDate = "+resDate+"]";
    }
}

