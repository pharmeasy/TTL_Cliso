package com.example.e5322.thyrosoft.FinalWoeModelPost;

import android.os.Parcel;
import android.os.Parcelable;

public class Woe implements Parcelable
{
    private String REF_DR_NAME;

    private String SPECIMEN_COLLECTION_TIME;

    private String WATER_SOURCE;

    protected Woe(Parcel in) {
        REF_DR_NAME = in.readString();
        SPECIMEN_COLLECTION_TIME = in.readString();
        WATER_SOURCE = in.readString();
        REMARKS = in.readString();
        ULCcode = in.readString();
        ADDITIONAL1 = in.readString();
        LEAD_ID = in.readString();
        REF_DR_ID = in.readString();
        SUB_SOURCE_CODE = in.readString();
        CUSTOMER_ID = in.readString();
        DELIVERY_MODE = in.readInt();
        purpose = in.readString();
        ALERT_MESSAGE = in.readString();
        STATUS = in.readString();
        APP_ID = in.readString();
        AGE_TYPE = in.readString();
        LAB_NAME = in.readString();
        AADHAR_NO = in.readString();
        SPECIMEN_SOURCE = in.readString();
        ENTERED_BY = in.readString();
        SR_NO = in.readInt();
        PINCODE = in.readString();
        TYPE = in.readString();
        EMAIL_ID = in.readString();
        CONT_PERSON = in.readString();
        LAB_ID = in.readString();
        AMOUNT_COLLECTED = in.readString();
        AGE = in.readString();
        WO_MODE = in.readString();
        ADDRESS = in.readString();
        ORDER_NO = in.readString();
        OS = in.readString();
        BCT_ID = in.readString();
        PRODUCT = in.readString();
        CONTACT_NO = in.readString();
        CAMP_ID = in.readString();
        LAB_ADDRESS = in.readString();
        PATIENT_NAME = in.readString();
        GENDER = in.readString();
        WO_STAGE = in.readInt();
        TOTAL_AMOUNT = in.readString();
        MAIN_SOURCE = in.readString();
        BRAND = in.readString();
        AMOUNT_DUE = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(REF_DR_NAME);
        dest.writeString(SPECIMEN_COLLECTION_TIME);
        dest.writeString(WATER_SOURCE);
        dest.writeString(REMARKS);
        dest.writeString(ULCcode);
        dest.writeString(ADDITIONAL1);
        dest.writeString(LEAD_ID);
        dest.writeString(REF_DR_ID);
        dest.writeString(SUB_SOURCE_CODE);
        dest.writeString(CUSTOMER_ID);
        dest.writeInt(DELIVERY_MODE);
        dest.writeString(purpose);
        dest.writeString(ALERT_MESSAGE);
        dest.writeString(STATUS);
        dest.writeString(APP_ID);
        dest.writeString(AGE_TYPE);
        dest.writeString(LAB_NAME);
        dest.writeString(AADHAR_NO);
        dest.writeString(SPECIMEN_SOURCE);
        dest.writeString(ENTERED_BY);
        dest.writeInt(SR_NO);
        dest.writeString(PINCODE);
        dest.writeString(TYPE);
        dest.writeString(EMAIL_ID);
        dest.writeString(CONT_PERSON);
        dest.writeString(LAB_ID);
        dest.writeString(AMOUNT_COLLECTED);
        dest.writeString(AGE);
        dest.writeString(WO_MODE);
        dest.writeString(ADDRESS);
        dest.writeString(ORDER_NO);
        dest.writeString(OS);
        dest.writeString(BCT_ID);
        dest.writeString(PRODUCT);
        dest.writeString(CONTACT_NO);
        dest.writeString(CAMP_ID);
        dest.writeString(LAB_ADDRESS);
        dest.writeString(PATIENT_NAME);
        dest.writeString(GENDER);
        dest.writeInt(WO_STAGE);
        dest.writeString(TOTAL_AMOUNT);
        dest.writeString(MAIN_SOURCE);
        dest.writeString(BRAND);
        dest.writeString(AMOUNT_DUE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Woe> CREATOR = new Creator<Woe>() {
        @Override
        public Woe createFromParcel(Parcel in) {
            return new Woe(in);
        }

        @Override
        public Woe[] newArray(int size) {
            return new Woe[size];
        }
    };

    public String getADDITIONAL1() {
        return ADDITIONAL1;
    }

    public void setADDITIONAL1(String ADDITIONAL1) {
        this.ADDITIONAL1 = ADDITIONAL1;
    }

    private String REMARKS;

    private String ULCcode;

    private String ADDITIONAL1;

    public String getULCcode() {
        return ULCcode;
    }

    public void setULCcode(String ULCcode) {
        this.ULCcode = ULCcode;
    }

    public Woe() {
    }

    private String LEAD_ID;

    private String REF_DR_ID;

    private String SUB_SOURCE_CODE;

    private String CUSTOMER_ID;

    private int DELIVERY_MODE;

    private String purpose;

    private String ALERT_MESSAGE;

    private String STATUS;

    private String APP_ID;

    private String AGE_TYPE;

    private String LAB_NAME;

    private String AADHAR_NO;

    private String SPECIMEN_SOURCE;

    private String ENTERED_BY;

    private int SR_NO;

    private String PINCODE;

    private String TYPE;

    private String EMAIL_ID;

    private String CONT_PERSON;

    private String LAB_ID;

    private String AMOUNT_COLLECTED;

    private String AGE;

    private String WO_MODE;

    private String ADDRESS;

    private String ORDER_NO;

    private String OS;

    private String BCT_ID;

    private String PRODUCT;

    private String CONTACT_NO;

    private String CAMP_ID;

    private String LAB_ADDRESS;

    private String PATIENT_NAME;

    private String GENDER;

    private int WO_STAGE;

    private String TOTAL_AMOUNT;

    private String MAIN_SOURCE;

    private String BRAND;

    private String AMOUNT_DUE;



    public String getREF_DR_NAME ()
    {
        return REF_DR_NAME;
    }

    public void setREF_DR_NAME (String REF_DR_NAME)
    {
        this.REF_DR_NAME = REF_DR_NAME;
    }

    public String getSPECIMEN_COLLECTION_TIME ()
    {
        return SPECIMEN_COLLECTION_TIME;
    }

    public void setSPECIMEN_COLLECTION_TIME (String SPECIMEN_COLLECTION_TIME)
    {
        this.SPECIMEN_COLLECTION_TIME = SPECIMEN_COLLECTION_TIME;
    }

    public String getWATER_SOURCE ()
    {
        return WATER_SOURCE;
    }

    public void setWATER_SOURCE (String WATER_SOURCE)
    {
        this.WATER_SOURCE = WATER_SOURCE;
    }

    public String getREMARKS ()
    {
        return REMARKS;
    }

    public void setREMARKS (String REMARKS)
    {
        this.REMARKS = REMARKS;
    }

    public String getLEAD_ID ()
    {
        return LEAD_ID;
    }

    public void setLEAD_ID (String LEAD_ID)
    {
        this.LEAD_ID = LEAD_ID;
    }

    public String getREF_DR_ID ()
    {
        return REF_DR_ID;
    }

    public void setREF_DR_ID (String REF_DR_ID)
    {
        this.REF_DR_ID = REF_DR_ID;
    }

    public String getSUB_SOURCE_CODE ()
    {
        return SUB_SOURCE_CODE;
    }

    public void setSUB_SOURCE_CODE (String SUB_SOURCE_CODE)
    {
        this.SUB_SOURCE_CODE = SUB_SOURCE_CODE;
    }

    public String getCUSTOMER_ID ()
    {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID (String CUSTOMER_ID)
    {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public int getDELIVERY_MODE ()
    {
        return DELIVERY_MODE;
    }

    public void setDELIVERY_MODE (int DELIVERY_MODE)
    {
        this.DELIVERY_MODE = DELIVERY_MODE;
    }

    public String getPurpose ()
    {
        return purpose;
    }

    public void setPurpose (String purpose)
    {
        this.purpose = purpose;
    }

    public String getALERT_MESSAGE ()
    {
        return ALERT_MESSAGE;
    }

    public void setALERT_MESSAGE (String ALERT_MESSAGE)
    {
        this.ALERT_MESSAGE = ALERT_MESSAGE;
    }

    public String getSTATUS ()
    {
        return STATUS;
    }

    public void setSTATUS (String STATUS)
    {
        this.STATUS = STATUS;
    }

    public String getAPP_ID ()
    {
        return APP_ID;
    }

    public void setAPP_ID (String APP_ID)
    {
        this.APP_ID = APP_ID;
    }

    public String getAGE_TYPE ()
    {
        return AGE_TYPE;
    }

    public void setAGE_TYPE (String AGE_TYPE)
    {
        this.AGE_TYPE = AGE_TYPE;
    }

    public String getLAB_NAME ()
    {
        return LAB_NAME;
    }

    public void setLAB_NAME (String LAB_NAME)
    {
        this.LAB_NAME = LAB_NAME;
    }

    public String getAADHAR_NO ()
    {
        return AADHAR_NO;
    }

    public void setAADHAR_NO (String AADHAR_NO)
    {
        this.AADHAR_NO = AADHAR_NO;
    }

    public String getSPECIMEN_SOURCE ()
    {
        return SPECIMEN_SOURCE;
    }

    public void setSPECIMEN_SOURCE (String SPECIMEN_SOURCE)
    {
        this.SPECIMEN_SOURCE = SPECIMEN_SOURCE;
    }

    public String getENTERED_BY ()
    {
        return ENTERED_BY;
    }

    public void setENTERED_BY (String ENTERED_BY)
    {
        this.ENTERED_BY = ENTERED_BY;
    }

    public int getSR_NO ()
    {
        return SR_NO;
    }

    public void setSR_NO (int SR_NO)
    {
        this.SR_NO = SR_NO;
    }

    public String getPINCODE ()
    {
        return PINCODE;
    }

    public void setPINCODE (String PINCODE)
    {
        this.PINCODE = PINCODE;
    }

    public String getTYPE ()
    {
        return TYPE;
    }

    public void setTYPE (String TYPE)
    {
        this.TYPE = TYPE;
    }

    public String getEMAIL_ID ()
    {
        return EMAIL_ID;
    }

    public void setEMAIL_ID (String EMAIL_ID)
    {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getCONT_PERSON ()
    {
        return CONT_PERSON;
    }

    public void setCONT_PERSON (String CONT_PERSON)
    {
        this.CONT_PERSON = CONT_PERSON;
    }

    public String getLAB_ID ()
    {
        return LAB_ID;
    }

    public void setLAB_ID (String LAB_ID)
    {
        this.LAB_ID = LAB_ID;
    }

    public String getAMOUNT_COLLECTED ()
    {
        return AMOUNT_COLLECTED;
    }

    public void setAMOUNT_COLLECTED (String AMOUNT_COLLECTED)
    {
        this.AMOUNT_COLLECTED = AMOUNT_COLLECTED;
    }

    public String getAGE ()
    {
        return AGE;
    }

    public void setAGE (String AGE)
    {
        this.AGE = AGE;
    }

    public String getWO_MODE ()
    {
        return WO_MODE;
    }

    public void setWO_MODE (String WO_MODE)
    {
        this.WO_MODE = WO_MODE;
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

    public String getOS ()
    {
        return OS;
    }

    public void setOS (String OS)
    {
        this.OS = OS;
    }

    public String getBCT_ID ()
    {
        return BCT_ID;
    }

    public void setBCT_ID (String BCT_ID)
    {
        this.BCT_ID = BCT_ID;
    }

    public String getPRODUCT ()
    {
        return PRODUCT;
    }

    public void setPRODUCT (String PRODUCT)
    {
        this.PRODUCT = PRODUCT;
    }

    public String getCONTACT_NO ()
    {
        return CONTACT_NO;
    }

    public void setCONTACT_NO (String CONTACT_NO)
    {
        this.CONTACT_NO = CONTACT_NO;
    }

    public String getCAMP_ID ()
    {
        return CAMP_ID;
    }

    public void setCAMP_ID (String CAMP_ID)
    {
        this.CAMP_ID = CAMP_ID;
    }

    public String getLAB_ADDRESS ()
    {
        return LAB_ADDRESS;
    }

    public void setLAB_ADDRESS (String LAB_ADDRESS)
    {
        this.LAB_ADDRESS = LAB_ADDRESS;
    }

    public String getPATIENT_NAME ()
    {
        return PATIENT_NAME;
    }

    public void setPATIENT_NAME (String PATIENT_NAME)
    {
        this.PATIENT_NAME = PATIENT_NAME;
    }

    public String getGENDER ()
    {
        return GENDER;
    }

    public void setGENDER (String GENDER)
    {
        this.GENDER = GENDER;
    }

    public int getWO_STAGE ()
    {
        return WO_STAGE;
    }

    public void setWO_STAGE (int WO_STAGE)
    {
        this.WO_STAGE = WO_STAGE;
    }

    public String getTOTAL_AMOUNT ()
    {
        return TOTAL_AMOUNT;
    }

    public void setTOTAL_AMOUNT (String TOTAL_AMOUNT)
    {
        this.TOTAL_AMOUNT = TOTAL_AMOUNT;
    }

    public String getMAIN_SOURCE ()
    {
        return MAIN_SOURCE;
    }

    public void setMAIN_SOURCE (String MAIN_SOURCE)
    {
        this.MAIN_SOURCE = MAIN_SOURCE;
    }

    public String getBRAND ()
    {
        return BRAND;
    }

    public void setBRAND (String BRAND)
    {
        this.BRAND = BRAND;
    }

    public String getAMOUNT_DUE ()
    {
        return AMOUNT_DUE;
    }

    public void setAMOUNT_DUE (String AMOUNT_DUE)
    {
        this.AMOUNT_DUE = AMOUNT_DUE;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [REF_DR_NAME = "+REF_DR_NAME+", SPECIMEN_COLLECTION_TIME = "+SPECIMEN_COLLECTION_TIME+", WATER_SOURCE = "+WATER_SOURCE+", REMARKS = "+REMARKS+", ULCcode = "+ULCcode+", LEAD_ID = "+LEAD_ID+", REF_DR_ID = "+REF_DR_ID+", SUB_SOURCE_CODE = "+SUB_SOURCE_CODE+", CUSTOMER_ID = "+CUSTOMER_ID+", DELIVERY_MODE = "+DELIVERY_MODE+", purpose = "+purpose+", ALERT_MESSAGE = "+ALERT_MESSAGE+", STATUS = "+STATUS+", APP_ID = "+APP_ID+", AGE_TYPE = "+AGE_TYPE+", LAB_NAME = "+LAB_NAME+", AADHAR_NO = "+AADHAR_NO+", SPECIMEN_SOURCE = "+SPECIMEN_SOURCE+", ENTERED_BY = "+ENTERED_BY+", SR_NO = "+SR_NO+", PINCODE = "+PINCODE+", TYPE = "+TYPE+", EMAIL_ID = "+EMAIL_ID+", CONT_PERSON = "+CONT_PERSON+", LAB_ID = "+LAB_ID+", AMOUNT_COLLECTED = "+AMOUNT_COLLECTED+", AGE = "+AGE+", WO_MODE = "+WO_MODE+", ADDRESS = "+ADDRESS+", ORDER_NO = "+ORDER_NO+", OS = "+OS+", BCT_ID = "+BCT_ID+", PRODUCT = "+PRODUCT+", CONTACT_NO = "+CONTACT_NO+", CAMP_ID = "+CAMP_ID+", LAB_ADDRESS = "+LAB_ADDRESS+", PATIENT_NAME = "+PATIENT_NAME+", GENDER = "+GENDER+", WO_STAGE = "+WO_STAGE+", TOTAL_AMOUNT = "+TOTAL_AMOUNT+", MAIN_SOURCE = "+MAIN_SOURCE+", BRAND = "+BRAND+", AMOUNT_DUE = "+AMOUNT_DUE+"]";
    }


}

