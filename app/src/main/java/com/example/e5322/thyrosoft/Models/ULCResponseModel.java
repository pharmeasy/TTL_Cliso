package com.example.e5322.thyrosoft.Models;

public class ULCResponseModel {
    private String TEST;

    private String B2B;

    private String B2C;

    private ULC_TEST[] ULC_TEST;

    private String SAMPLE_TYPE;

    private String RESPONSE;

    private String ERROR;

    private String RES_ID;

    private String ULC_CODE;

    private String PRODUCT;

    private String ULC_STATUS;

    public String getTEST ()
    {
        return TEST;
    }

    public void setTEST (String TEST)
    {
        this.TEST = TEST;
    }

    public String getB2B ()
    {
        return B2B;
    }

    public void setB2B (String B2B)
    {
        this.B2B = B2B;
    }

    public String getB2C ()
    {
        return B2C;
    }

    public void setB2C (String B2C)
    {
        this.B2C = B2C;
    }

    public ULC_TEST[] getULC_TEST ()
    {
        return ULC_TEST;
    }

    public void setULC_TEST (ULC_TEST[] ULC_TEST)
    {
        this.ULC_TEST = ULC_TEST;
    }

    public String getSAMPLE_TYPE ()
    {
        return SAMPLE_TYPE;
    }

    public void setSAMPLE_TYPE (String SAMPLE_TYPE)
    {
        this.SAMPLE_TYPE = SAMPLE_TYPE;
    }

    public String getRESPONSE ()
    {
        return RESPONSE;
    }

    public void setRESPONSE (String RESPONSE)
    {
        this.RESPONSE = RESPONSE;
    }

    public String getERROR ()
    {
        return ERROR;
    }

    public void setERROR (String ERROR)
    {
        this.ERROR = ERROR;
    }

    public String getRES_ID ()
    {
        return RES_ID;
    }

    public void setRES_ID (String RES_ID)
    {
        this.RES_ID = RES_ID;
    }

    public String getULC_CODE ()
    {
        return ULC_CODE;
    }

    public void setULC_CODE (String ULC_CODE)
    {
        this.ULC_CODE = ULC_CODE;
    }

    public String getPRODUCT ()
    {
        return PRODUCT;
    }

    public void setPRODUCT (String PRODUCT)
    {
        this.PRODUCT = PRODUCT;
    }

    public String getULC_STATUS ()
    {
        return ULC_STATUS;
    }

    public void setULC_STATUS (String ULC_STATUS)
    {
        this.ULC_STATUS = ULC_STATUS;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TEST = "+TEST+", B2B = "+B2B+", B2C = "+B2C+", ULC_TEST = "+ULC_TEST+", SAMPLE_TYPE = "+SAMPLE_TYPE+", RESPONSE = "+RESPONSE+", ERROR = "+ERROR+", RES_ID = "+RES_ID+", ULC_CODE = "+ULC_CODE+", PRODUCT = "+PRODUCT+", ULC_STATUS = "+ULC_STATUS+"]";
    }
}
