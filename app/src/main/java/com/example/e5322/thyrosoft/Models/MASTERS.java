package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */
public class MASTERS
{
    private TSP_MASTER TSP_MASTER;

    private SUB_SOURCECODE[] SUB_SOURCECODE;

    private SAMPLE_COLLECTION_TYPE[] SAMPLE_COLLECTION_TYPE;

    private String[] WATER_SOURCE;

    private USER_MASTER USER_MASTER;

    private BCT_LIST[] BCT_LIST;

    private DELIVERY_MODE[] DELIVERY_MODE;

    private CAMP_LIST[] CAMP_LIST;

    private BRAND_LIST[] BRAND_LIST;

    private String[] LAB_ALERTS;

    public TSP_MASTER getTSP_MASTER ()
    {
        return TSP_MASTER;
    }

    public void setTSP_MASTER (TSP_MASTER TSP_MASTER)
    {
        this.TSP_MASTER = TSP_MASTER;
    }

    public SUB_SOURCECODE[] getSUB_SOURCECODE ()
    {
        return SUB_SOURCECODE;
    }

    public void setSUB_SOURCECODE (SUB_SOURCECODE[] SUB_SOURCECODE)
    {
        this.SUB_SOURCECODE = SUB_SOURCECODE;
    }

    public SAMPLE_COLLECTION_TYPE[] getSAMPLE_COLLECTION_TYPE ()
    {
        return SAMPLE_COLLECTION_TYPE;
    }

    public void setSAMPLE_COLLECTION_TYPE (SAMPLE_COLLECTION_TYPE[] SAMPLE_COLLECTION_TYPE)
    {
        this.SAMPLE_COLLECTION_TYPE = SAMPLE_COLLECTION_TYPE;
    }

    public String[] getWATER_SOURCE ()
    {
        return WATER_SOURCE;
    }

    public void setWATER_SOURCE (String[] WATER_SOURCE)
    {
        this.WATER_SOURCE = WATER_SOURCE;
    }

    public USER_MASTER getUSER_MASTER ()
    {
        return USER_MASTER;
    }

    public void setUSER_MASTER (USER_MASTER USER_MASTER)
    {
        this.USER_MASTER = USER_MASTER;
    }

    public BCT_LIST[] getBCT_LIST ()
    {
        return BCT_LIST;
    }

    public void setBCT_LIST (BCT_LIST[] BCT_LIST)
    {
        this.BCT_LIST = BCT_LIST;
    }

    public DELIVERY_MODE[] getDELIVERY_MODE ()
    {
        return DELIVERY_MODE;
    }

    public void setDELIVERY_MODE (DELIVERY_MODE[] DELIVERY_MODE)
    {
        this.DELIVERY_MODE = DELIVERY_MODE;
    }

    public CAMP_LIST[] getCAMP_LIST ()
    {
        return CAMP_LIST;
    }

    public void setCAMP_LIST (CAMP_LIST[] CAMP_LIST)
    {
        this.CAMP_LIST = CAMP_LIST;
    }

    public BRAND_LIST[] getBRAND_LIST ()
    {
        return BRAND_LIST;
    }

    public void setBRAND_LIST (BRAND_LIST[] BRAND_LIST)
    {
        this.BRAND_LIST = BRAND_LIST;
    }

    public String[] getLAB_ALERTS ()
    {
        return LAB_ALERTS;
    }

    public void setLAB_ALERTS (String[] LAB_ALERTS)
    {
        this.LAB_ALERTS = LAB_ALERTS;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [TSP_MASTER = "+TSP_MASTER+", SUB_SOURCECODE = "+SUB_SOURCECODE+", SAMPLE_COLLECTION_TYPE = "+SAMPLE_COLLECTION_TYPE+", WATER_SOURCE = "+WATER_SOURCE+", USER_MASTER = "+USER_MASTER+", BCT_LIST = "+BCT_LIST+", DELIVERY_MODE = "+DELIVERY_MODE+", CAMP_LIST = "+CAMP_LIST+", BRAND_LIST = "+BRAND_LIST+", LAB_ALERTS = "+LAB_ALERTS+"]";
    }
}

