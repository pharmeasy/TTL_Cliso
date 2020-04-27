package com.example.e5322.thyrosoft.OutLabTEstModel;

/**
 * Created by E5322 on 29-05-2018.
 */

public class OutlabdetailsWOe
{
    private String product;

    private Sampletype[] sampletype;

    private Rate rate;

    private String name;

    private String code;

    public String getProduct ()
    {
        return product;
    }

    public void setProduct (String product)
    {
        this.product = product;
    }

    public Sampletype[] getSampletype ()
    {
        return sampletype;
    }

    public void setSampletype (Sampletype[] sampletype)
    {
        this.sampletype = sampletype;
    }

    public Rate getRate ()
    {
        return rate;
    }

    public void setRate (Rate rate)
    {
        this.rate = rate;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [product = "+product+", sampletype = "+sampletype+", rate = "+rate+", name = "+name+", code = "+code+"]";
    }
}

