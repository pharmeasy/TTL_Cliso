package com.example.e5322.thyrosoft.OutLabTEstModel;

/**
 * Created by E5322 on 29-05-2018.
 */

public class OUTLAB_TESTLIST
{
    private String LOCATION;

    private Outlabdetails[] outlabdetails;

    public String getLOCATION ()
    {
        return LOCATION;
    }

    public void setLOCATION (String LOCATION)
    {
        this.LOCATION = LOCATION;
    }

    public Outlabdetails[] getOutlabdetails ()
    {
        return outlabdetails;
    }

    public void setOutlabdetails (Outlabdetails[] outlabdetails)
    {
        this.outlabdetails = outlabdetails;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [LOCATION = "+LOCATION+", outlabdetails = "+outlabdetails+"]";
    }
}