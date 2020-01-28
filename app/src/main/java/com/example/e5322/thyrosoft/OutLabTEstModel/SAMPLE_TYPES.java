package com.example.e5322.thyrosoft.OutLabTEstModel;

/**
 * Created by E5322 on 29-05-2018.
 */

public class SAMPLE_TYPES
{
    private String sample_type_name;

    public String getSample_type_name ()
    {
        return sample_type_name;
    }

    public void setSample_type_name (String sample_type_name)
    {
        this.sample_type_name = sample_type_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sample_type_name = "+sample_type_name+"]";
    }
}

