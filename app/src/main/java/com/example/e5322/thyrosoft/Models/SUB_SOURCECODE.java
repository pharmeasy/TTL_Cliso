package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */


public class SUB_SOURCECODE
{
    private String sub_source_code;

    private String sub_source_code_pass;

    public String getSub_source_code ()
    {
        return sub_source_code;
    }

    public void setSub_source_code (String sub_source_code)
    {
        this.sub_source_code = sub_source_code;
    }

    public String getSub_source_code_pass ()
    {
        return sub_source_code_pass;
    }

    public void setSub_source_code_pass (String sub_source_code_pass)
    {
        this.sub_source_code_pass = sub_source_code_pass;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sub_source_code = "+sub_source_code+", sub_source_code_pass = "+sub_source_code_pass+"]";
    }
}
