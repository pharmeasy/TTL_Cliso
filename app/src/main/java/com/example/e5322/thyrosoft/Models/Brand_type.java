package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */

public class Brand_type
{
    private String id;

    private String type;

    private String mode;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getMode ()
    {
        return mode;
    }

    public void setMode (String mode)
    {
        this.mode = mode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", type = "+type+", mode = "+mode+"]";
    }
}



