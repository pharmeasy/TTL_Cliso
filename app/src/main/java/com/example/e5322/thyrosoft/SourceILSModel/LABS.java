package com.example.e5322.thyrosoft.SourceILSModel;

/**
 * Created by E5322 on 29-05-2018.
 */

public class LABS
{
    private String pincode;
    private String status;

    private String clientid;

    private String passingname;

    private String email;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String name;

    private String labName;

    private String idaddress;

    private String labAddress;

    private String mobile;

    public String getPincode ()
    {
        return pincode;
    }

    public void setPincode (String pincode)
    {
        this.pincode = pincode;
    }

    public String getClientid ()
    {
        return clientid;
    }

    public void setClientid (String clientid)
    {
        this.clientid = clientid;
    }

    public String getPassingname ()
    {
        return passingname;
    }

    public void setPassingname (String passingname)
    {
        this.passingname = passingname;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getLabName ()
    {
        return labName;
    }

    public void setLabName (String labName)
    {
        this.labName = labName;
    }

    public String getIdaddress ()
    {
        return idaddress;
    }

    public void setIdaddress (String idaddress)
    {
        this.idaddress = idaddress;
    }

    public String getLabAddress ()
    {
        return labAddress;
    }

    public void setLabAddress (String labAddress)
    {
        this.labAddress = labAddress;
    }

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pincode = "+pincode+", clientid = "+clientid+", passingname = "+passingname+", email = "+email+", name = "+name+", labName = "+labName+", idaddress = "+idaddress+", labAddress = "+labAddress+", mobile = "+mobile+"]";
    }
}


