package com.example.e5322.thyrosoft.LeadOrderIDModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E5322 on 06-06-2018.
 */

public class LeadOrderIdMainModel implements Parcelable{
    private Leads[] leads;

    private String RESPONSE;

    private String ERROR;

    private String RES_ID;

    public LeadOrderIdMainModel(Parcel in) {
        RESPONSE = in.readString();
        ERROR = in.readString();
        RES_ID = in.readString();
        leads = in.createTypedArray(Leads.CREATOR);
    }

    public static final Creator<LeadOrderIdMainModel> CREATOR = new Creator<LeadOrderIdMainModel>() {
        @Override
        public LeadOrderIdMainModel createFromParcel(Parcel in) {
            return new LeadOrderIdMainModel(in);
        }

        @Override
        public LeadOrderIdMainModel[] newArray(int size) {
            return new LeadOrderIdMainModel[size];
        }
    };

    public Leads[] getLeads ()
    {
        return leads;
    }

    public void setLeads (Leads[] leads)
    {
        this.leads = leads;
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

    public LeadOrderIdMainModel() {
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

    @Override
    public String toString()
    {
        return "ClassPojo [leads = "+leads+", RESPONSE = "+RESPONSE+", ERROR = "+ERROR+", RES_ID = "+RES_ID+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(RESPONSE);
        dest.writeString(ERROR);
        dest.writeString(RES_ID);
        dest.writeTypedArray(leads, flags);
    }
}
