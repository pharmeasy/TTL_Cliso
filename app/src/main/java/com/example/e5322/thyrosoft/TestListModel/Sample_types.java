package com.example.e5322.thyrosoft.TestListModel;

/**
 * Created by E5322 on 24-05-2018.
 */

public class Sample_types
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

