package com.example.e5322.thyrosoft.Models;

public class ULC_TEST
{
    private String test;

    private String sample_type;

    public String getTest ()
    {
        return test;
    }

    public void setTest (String test)
    {
        this.test = test;
    }

    public String getSample_type ()
    {
        return sample_type;
    }

    public void setSample_type (String sample_type)
    {
        this.sample_type = sample_type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [test = "+test+", sample_type = "+sample_type+"]";
    }
}


