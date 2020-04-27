package com.example.e5322.thyrosoft.TestListModel;

/**
 * Created by E5322 on 24-05-2018.
 */

public class TestModel
{
    private String MASTERS;

    private B2B_MASTERS B2B_MASTERS;

    private String RESPONSE;

    private String RES_ID;

    private String USER_TYPE;

    public String getMASTERS ()
{
    return MASTERS;
}

    public void setMASTERS (String MASTERS)
    {
        this.MASTERS = MASTERS;
    }

    public B2B_MASTERS getB2B_MASTERS ()
    {
        return B2B_MASTERS;
    }

    public void setB2B_MASTERS (B2B_MASTERS B2B_MASTERS)
    {
        this.B2B_MASTERS = B2B_MASTERS;
    }

    public String getRESPONSE ()
{
    return RESPONSE;
}

    public void setRESPONSE (String RESPONSE)
    {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID ()
{
    return RES_ID;
}

    public void setRES_ID (String RES_ID)
    {
        this.RES_ID = RES_ID;
    }

    public String getUSER_TYPE ()
    {
        return USER_TYPE;
    }

    public void setUSER_TYPE (String USER_TYPE)
    {
        this.USER_TYPE = USER_TYPE;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [MASTERS = "+MASTERS+", B2B_MASTERSProfile.java = "+B2B_MASTERS+", RESPONSE = "+RESPONSE+", RES_ID = "+RES_ID+", USER_TYPE = "+USER_TYPE+"]";
    }
}

