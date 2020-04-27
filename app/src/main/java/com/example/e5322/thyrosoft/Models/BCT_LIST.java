package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */

public class BCT_LIST
{
    private String NAME;

    private String NED_NUMBER;

    private String MOBILE_NUMBER;

    public String getNAME ()
    {
        return NAME;
    }

    public void setNAME (String NAME)
    {
        this.NAME = NAME;
    }

    public String getNED_NUMBER ()
    {
        return NED_NUMBER;
    }

    public void setNED_NUMBER (String NED_NUMBER)
    {
        this.NED_NUMBER = NED_NUMBER;
    }

    public String getMOBILE_NUMBER ()
    {
        return MOBILE_NUMBER;
    }

    public void setMOBILE_NUMBER (String MOBILE_NUMBER)
    {
        this.MOBILE_NUMBER = MOBILE_NUMBER;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [NAME = "+NAME+", NED_NUMBER = "+NED_NUMBER+", MOBILE_NUMBER = "+MOBILE_NUMBER+"]";
    }
}

