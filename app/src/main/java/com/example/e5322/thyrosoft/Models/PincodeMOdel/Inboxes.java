package com.example.e5322.thyrosoft.Models.PincodeMOdel;

public class Inboxes
{
    private String response;

    private String commBy;

    private String status;

    private String department;

    private String commId;

    private String commDate;

    private String question;

    private String TAT;

    private String resDate;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getCommBy ()
    {
        return commBy;
    }

    public void setCommBy (String commBy)
    {
        this.commBy = commBy;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
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

    public String getQuestion ()
    {
        return question;
    }

    public void setQuestion (String question)
    {
        this.question = question;
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
        return "ClassPojo [response = "+response+", commBy = "+commBy+", status = "+status+", department = "+department+", commId = "+commId+", commDate = "+commDate+", question = "+question+", TAT = "+TAT+", resDate = "+resDate+"]";
    }
}

