package com.example.e5322.thyrosoft.Models.PincodeMOdel;

public class OLCcontact_Array_list {
    private String CATEGORY;

    private String NAME;

    private String ROLE;

    private String MOBILE;

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    private String DESIGNATION;

    private String ECODE;

    private String IMG;

    private String EMAIL_ID;

    public String getCATEGORY ()
    {
        return CATEGORY;
    }

    public void setCATEGORY (String CATEGORY)
    {
        this.CATEGORY = CATEGORY;
    }

    public String getNAME ()
    {
        return NAME;
    }

    public void setNAME (String NAME)
    {
        this.NAME = NAME;
    }

    public String getROLE ()
    {
        return ROLE;
    }

    public void setROLE (String ROLE)
    {
        this.ROLE = ROLE;
    }

    public String getDESIGNATION ()
    {
        return DESIGNATION;
    }

    public void setDESIGNATION (String DESIGNATION)
    {
        this.DESIGNATION = DESIGNATION;
    }

    public String getECODE ()
    {
        return ECODE;
    }

    public void setECODE (String ECODE)
    {
        this.ECODE = ECODE;
    }

    public String getIMG ()
    {
        return IMG;
    }

    public void setIMG (String IMG)
    {
        this.IMG = IMG;
    }

    public String getEMAIL_ID ()
    {
        return EMAIL_ID;
    }

    public void setEMAIL_ID (String EMAIL_ID)
    {
        this.EMAIL_ID = EMAIL_ID;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [CATEGORY = "+CATEGORY+", NAME = "+NAME+", ROLE = "+ROLE+", DESIGNATION = "+DESIGNATION+", ECODE = "+ECODE+", IMG = "+IMG+", EMAIL_ID = "+EMAIL_ID+"]";
    }
}
