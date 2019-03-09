package com.example.e5322.thyrosoft.OutLabTEstModel;

/**
 * Created by E5322 on 29-05-2018.
 */

public class Rate
{
    private String b2c;

    private String b2b;

    public String getB2c ()
    {
        return b2c;
    }

    public void setB2c (String b2c)
    {
        this.b2c = b2c;
    }

    public String getB2b ()
    {
        return b2b;
    }

    public void setB2b (String b2b)
    {
        this.b2b = b2b;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [b2c = "+b2c+", b2b = "+b2b+"]";
    }
}

