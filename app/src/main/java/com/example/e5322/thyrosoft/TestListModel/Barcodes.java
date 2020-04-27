package com.example.e5322.thyrosoft.TestListModel;

/**
 * Created by E5322 on 24-05-2018.
 */

public class Barcodes
{
    private String specimen_type;

    private String code;

    public String getSpecimen_type ()
    {
        return specimen_type;
    }

    public void setSpecimen_type (String specimen_type)
    {
        this.specimen_type = specimen_type;
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
        return "ClassPojo [specimen_type = "+specimen_type+", code = "+code+"]";
    }
}


