package com.example.e5322.thyrosoft.FAQ_Main_Model;

public class FAQ_Model {
    String response;

    public FAQandANSArray[] FAQandANSArray;


    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public FAQandANSArray[] getFAQandANSArray ()
    {
        return FAQandANSArray;
    }

    public void setFAQandANSArray (FAQandANSArray[] FAQandANSArray)
    {
        this.FAQandANSArray = FAQandANSArray;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", FAQandANSArray = "+FAQandANSArray+"]";
    }
}
