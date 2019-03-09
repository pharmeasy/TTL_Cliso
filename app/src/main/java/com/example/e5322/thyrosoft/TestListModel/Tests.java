package com.example.e5322.thyrosoft.TestListModel;

/**
 * Created by E5322 on 24-05-2018.
 */


public class Tests
{
    private String product;

    private Barcodes[] barcodes;

    private Rate rate;

    private String name;

    private String fasting;

    private String[] childs;

    private String code;

    private String type;

    public String getProduct ()
    {
        return product;
    }

    public void setProduct (String product)
    {
        this.product = product;
    }

    public Barcodes[] getBarcodes ()
    {
        return barcodes;
    }

    public void setBarcodes (Barcodes[] barcodes)
    {
        this.barcodes = barcodes;
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

    public String getFasting ()
    {
        return fasting;
    }

    public void setFasting (String fasting)
    {
        this.fasting = fasting;
    }

    public String[] getChilds ()
    {
        return childs;
    }

    public void setChilds (String[] childs)
    {
        this.childs = childs;
    }

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [product = "+product+", barcodes = "+barcodes+", rate = "+rate+", name = "+name+", fasting = "+fasting+", childs = "+childs+", code = "+code+", type = "+type+"]";
    }
}

