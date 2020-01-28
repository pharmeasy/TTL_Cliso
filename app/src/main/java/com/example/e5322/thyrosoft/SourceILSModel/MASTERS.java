package com.example.e5322.thyrosoft.SourceILSModel;

/**
 * Created by E5322 on 29-05-2018.
 */

public class MASTERS
{
    private LABS[] LABS;

    private REF_DR[] REF_DR;

    public LABS[] getLABS ()
    {
        return LABS;
    }

    public void setLABS (LABS[] LABS)
    {
        this.LABS = LABS;
    }

    public REF_DR[] getREF_DR ()
    {
        return REF_DR;
    }

    public void setREF_DR (REF_DR[] REF_DR)
    {
        this.REF_DR = REF_DR;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [LABS = "+LABS+", REF_DR = "+REF_DR+"]";
    }
}


