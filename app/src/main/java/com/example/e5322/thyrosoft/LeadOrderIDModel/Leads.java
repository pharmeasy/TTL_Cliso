package com.example.e5322.thyrosoft.LeadOrderIDModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E5322 on 06-06-2018.
 */

public class Leads implements Parcelable
{
    private String PACKAGE;

    private String LEAD_ID;

    private String SERUM;

    public Leads() {
    }

    private String AGE_TYPE;

    private String SCT;

    private String WATER;

    private String LAB_NAME;

    private String EDTA;

    private String TESTS;

    private String ERROR;

    private String PINCODE;

    private String EMAIL;

    private String REF_BY;

    private String leadData;

    private String TYPE;

    private String BCT;

    private String LAB_ID;

    private String NAME;

    private String RATE;

    private String AGE;

    private String SAMPLE_TYPE;

    private String URINE;

    private String ADDRESS;

    private String ORDER_NO;

    private String HEPARIN;

    private String PRODUCT;

    private String MOBILE;

    private String GENDER;

    private String RESPONSE;

    private String FLUORIDE;

    protected Leads(Parcel in) {
        PACKAGE = in.readString();
        LEAD_ID = in.readString();
        SERUM = in.readString();
        AGE_TYPE = in.readString();
        SCT = in.readString();
        WATER = in.readString();
        LAB_NAME = in.readString();
        EDTA = in.readString();
        TESTS = in.readString();
        ERROR = in.readString();
        PINCODE = in.readString();
        EMAIL = in.readString();
        REF_BY = in.readString();
        leadData = in.readString();
        TYPE = in.readString();
        BCT = in.readString();
        LAB_ID = in.readString();
        NAME = in.readString();
        RATE = in.readString();
        AGE = in.readString();
        SAMPLE_TYPE = in.readString();
        URINE = in.readString();
        ADDRESS = in.readString();
        ORDER_NO = in.readString();
        HEPARIN = in.readString();
        PRODUCT = in.readString();
        MOBILE = in.readString();
        GENDER = in.readString();
        RESPONSE = in.readString();
        FLUORIDE = in.readString();
    }

    public static final Creator<Leads> CREATOR = new Creator<Leads>() {
        @Override
        public Leads createFromParcel(Parcel in) {
            return new Leads(in);
        }

        @Override
        public Leads[] newArray(int size) {
            return new Leads[size];
        }
    };

    public String getPACKAGE ()
{
    return PACKAGE;
}

    public void setPACKAGE (String PACKAGE)
    {
        this.PACKAGE = PACKAGE;
    }

    public String getLEAD_ID ()
    {
        return LEAD_ID;
    }

    public void setLEAD_ID (String LEAD_ID)
    {
        this.LEAD_ID = LEAD_ID;
    }

    public String getSERUM ()
    {
        return SERUM;
    }

    public void setSERUM (String SERUM)
    {
        this.SERUM = SERUM;
    }

    public String getAGE_TYPE ()
    {
        return AGE_TYPE;
    }

    public void setAGE_TYPE (String AGE_TYPE)
    {
        this.AGE_TYPE = AGE_TYPE;
    }

    public String getSCT ()
    {
        return SCT;
    }

    public void setSCT (String SCT)
    {
        this.SCT = SCT;
    }

    public String getWATER ()
    {
        return WATER;
    }

    public void setWATER (String WATER)
    {
        this.WATER = WATER;
    }

    public String getLAB_NAME ()
    {
        return LAB_NAME;
    }

    public void setLAB_NAME (String LAB_NAME)
    {
        this.LAB_NAME = LAB_NAME;
    }

    public String getEDTA ()
    {
        return EDTA;
    }

    public void setEDTA (String EDTA)
    {
        this.EDTA = EDTA;
    }

    public String getTESTS ()
    {
        return TESTS;
    }

    public void setTESTS (String TESTS)
    {
        this.TESTS = TESTS;
    }

    public String getERROR ()
    {
        return ERROR;
    }

    public void setERROR (String ERROR)
    {
        this.ERROR = ERROR;
    }

    public String getPINCODE ()
    {
        return PINCODE;
    }

    public void setPINCODE (String PINCODE)
    {
        this.PINCODE = PINCODE;
    }

    public String getEMAIL ()
    {
        return EMAIL;
    }

    public void setEMAIL (String EMAIL)
    {
        this.EMAIL = EMAIL;
    }

    public String getREF_BY ()
    {
        return REF_BY;
    }

    public void setREF_BY (String REF_BY)
    {
        this.REF_BY = REF_BY;
    }

    public String getLeadData ()
{
    return leadData;
}

    public void setLeadData (String leadData)
    {
        this.leadData = leadData;
    }

    public String getTYPE ()
    {
        return TYPE;
    }

    public void setTYPE (String TYPE)
    {
        this.TYPE = TYPE;
    }

    public String getBCT ()
    {
        return BCT;
    }

    public void setBCT (String BCT)
    {
        this.BCT = BCT;
    }

    public String getLAB_ID ()
    {
        return LAB_ID;
    }

    public void setLAB_ID (String LAB_ID)
    {
        this.LAB_ID = LAB_ID;
    }

    public String getNAME ()
    {
        return NAME;
    }

    public void setNAME (String NAME)
    {
        this.NAME = NAME;
    }

    public String getRATE ()
    {
        return RATE;
    }

    public void setRATE (String RATE)
    {
        this.RATE = RATE;
    }

    public String getAGE ()
    {
        return AGE;
    }

    public void setAGE (String AGE)
    {
        this.AGE = AGE;
    }

    public String getSAMPLE_TYPE ()
    {
        return SAMPLE_TYPE;
    }

    public void setSAMPLE_TYPE (String SAMPLE_TYPE)
    {
        this.SAMPLE_TYPE = SAMPLE_TYPE;
    }

    public String getURINE ()
    {
        return URINE;
    }

    public void setURINE (String URINE)
    {
        this.URINE = URINE;
    }

    public String getADDRESS ()
    {
        return ADDRESS;
    }

    public void setADDRESS (String ADDRESS)
    {
        this.ADDRESS = ADDRESS;
    }

    public String getORDER_NO ()
    {
        return ORDER_NO;
    }

    public void setORDER_NO (String ORDER_NO)
    {
        this.ORDER_NO = ORDER_NO;
    }

    public String getHEPARIN ()
    {
        return HEPARIN;
    }

    public void setHEPARIN (String HEPARIN)
    {
        this.HEPARIN = HEPARIN;
    }

    public String getPRODUCT ()
{
    return PRODUCT;
}

    public void setPRODUCT (String PRODUCT)
    {
        this.PRODUCT = PRODUCT;
    }

    public String getMOBILE ()
    {
        return MOBILE;
    }

    public void setMOBILE (String MOBILE)
    {
        this.MOBILE = MOBILE;
    }

    public String getGENDER ()
    {
        return GENDER;
    }

    public void setGENDER (String GENDER)
    {
        this.GENDER = GENDER;
    }

    public String getRESPONSE ()
    {
        return RESPONSE;
    }

    public void setRESPONSE (String RESPONSE)
    {
        this.RESPONSE = RESPONSE;
    }

    public String getFLUORIDE ()
    {
        return FLUORIDE;
    }

    public void setFLUORIDE (String FLUORIDE)
    {
        this.FLUORIDE = FLUORIDE;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [PACKAGE = "+PACKAGE+", LEAD_ID = "+LEAD_ID+", SERUM = "+SERUM+", AGE_TYPE = "+AGE_TYPE+", SCT = "+SCT+", WATER = "+WATER+", LAB_NAME = "+LAB_NAME+", EDTA = "+EDTA+", TESTS = "+TESTS+", ERROR = "+ERROR+", PINCODE = "+PINCODE+", EMAIL = "+EMAIL+", REF_BY = "+REF_BY+", leadData = "+leadData+", TYPE = "+TYPE+", BCT = "+BCT+", LAB_ID = "+LAB_ID+", NAME = "+NAME+", RATE = "+RATE+", AGE = "+AGE+", SAMPLE_TYPE = "+SAMPLE_TYPE+", URINE = "+URINE+", ADDRESS = "+ADDRESS+", ORDER_NO = "+ORDER_NO+", HEPARIN = "+HEPARIN+", PRODUCT = "+PRODUCT+", MOBILE = "+MOBILE+", GENDER = "+GENDER+", RESPONSE = "+RESPONSE+", FLUORIDE = "+FLUORIDE+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PACKAGE);
        dest.writeString(LEAD_ID);
        dest.writeString(SERUM);
        dest.writeString(AGE_TYPE);
        dest.writeString(SCT);
        dest.writeString(WATER);
        dest.writeString(LAB_NAME);
        dest.writeString(EDTA);
        dest.writeString(TESTS);
        dest.writeString(ERROR);
        dest.writeString(PINCODE);
        dest.writeString(EMAIL);
        dest.writeString(REF_BY);
        dest.writeString(leadData);
        dest.writeString(TYPE);
        dest.writeString(BCT);
        dest.writeString(LAB_ID);
        dest.writeString(NAME);
        dest.writeString(RATE);
        dest.writeString(AGE);
        dest.writeString(SAMPLE_TYPE);
        dest.writeString(URINE);
        dest.writeString(ADDRESS);
        dest.writeString(ORDER_NO);
        dest.writeString(HEPARIN);
        dest.writeString(PRODUCT);
        dest.writeString(MOBILE);
        dest.writeString(GENDER);
        dest.writeString(RESPONSE);
        dest.writeString(FLUORIDE);
    }
}


