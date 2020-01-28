package com.example.e5322.thyrosoft.Models.PincodeMOdel;

public class CommunicationMaster
{
    private String id;

    private String isBarcode;

    private String forwardTo;

    private String displayName;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getIsBarcode ()
    {
        return isBarcode;
    }

    public void setIsBarcode (String isBarcode)
    {
        this.isBarcode = isBarcode;
    }

    public String getForwardTo ()
    {
        return forwardTo;
    }

    public void setForwardTo (String forwardTo)
    {
        this.forwardTo = forwardTo;
    }

    public String getDisplayName ()
    {
        return displayName;
    }

    public void setDisplayName (String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", isBarcode = "+isBarcode+", forwardTo = "+forwardTo+", displayName = "+displayName+"]";
    }
}

