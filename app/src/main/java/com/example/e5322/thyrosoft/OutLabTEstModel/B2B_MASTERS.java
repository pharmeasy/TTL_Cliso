package com.example.e5322.thyrosoft.OutLabTEstModel;

/**
 * Created by E5322 on 29-05-2018.
 */
public class B2B_MASTERS
{
    private String PROFILE;

    private String WHATER_TESTLIST;

    private String TESTS;

    private SAMPLE_TYPES[] SAMPLE_TYPES;

    private OUTLAB_TESTLIST[] OUTLAB_TESTLIST;

    private String POP;

    private String MPL;

    public String getPROFILE ()
{
    return PROFILE;
}

    public void setPROFILE (String PROFILE)
    {
        this.PROFILE = PROFILE;
    }

    public String getWHATER_TESTLIST ()
{
    return WHATER_TESTLIST;
}

    public void setWHATER_TESTLIST (String WHATER_TESTLIST)
    {
        this.WHATER_TESTLIST = WHATER_TESTLIST;
    }

    public String getTESTS ()
{
    return TESTS;
}

    public void setTESTS (String TESTS)
    {
        this.TESTS = TESTS;
    }

    public SAMPLE_TYPES[] getSAMPLE_TYPES ()
    {
        return SAMPLE_TYPES;
    }

    public void setSAMPLE_TYPES (SAMPLE_TYPES[] SAMPLE_TYPES)
    {
        this.SAMPLE_TYPES = SAMPLE_TYPES;
    }

    public OUTLAB_TESTLIST[] getOUTLAB_TESTLIST ()
    {
        return OUTLAB_TESTLIST;
    }

    public void setOUTLAB_TESTLIST (OUTLAB_TESTLIST[] OUTLAB_TESTLIST)
    {
        this.OUTLAB_TESTLIST = OUTLAB_TESTLIST;
    }

    public String getPOP ()
{
    return POP;
}

    public void setPOP (String POP)
    {
        this.POP = POP;
    }

    public String getMPL ()
{
    return MPL;
}

    public void setMPL (String MPL)
    {
        this.MPL = MPL;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [PROFILE = "+PROFILE+", WHATER_TESTLIST = "+WHATER_TESTLIST+", TESTS = "+TESTS+", SAMPLE_TYPES = "+SAMPLE_TYPES+", OUTLAB_TESTLIST = "+OUTLAB_TESTLIST+", POP = "+POP+", MPL = "+MPL+"]";
    }
}


