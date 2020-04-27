package com.example.e5322.thyrosoft.Models;

import com.example.e5322.thyrosoft.Models.PincodeMOdel.OLCcontact_Array_list;

import java.util.ArrayList;


public class Company_Contact_Model {
    private String response;

    ArrayList<OLCcontact_Array_list> Contact_Array_list;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public ArrayList<OLCcontact_Array_list> getContact_Array_list ()
    {
        return Contact_Array_list;
    }

    public void setContact_Array_list (ArrayList<OLCcontact_Array_list> Contact_Array_list)
    {
        this.Contact_Array_list = Contact_Array_list;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", Contact_Array_list = "+Contact_Array_list+"]";
    }
}
