package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */

public class TSP_MASTER
{
    private String ac_code;

    private String user_code;

    private String number;

    private String closing_balance;

    private String tsp_image;

    private String aadhar_no;

    private String source_code;

    private String pincode;

    private String response;

    private String credit_limit;

    private String address;

    private String email;

    private String name;

    private String doj;

    private String mobile;

    public String getAc_code ()
    {
        return ac_code;
    }

    public void setAc_code (String ac_code)
    {
        this.ac_code = ac_code;
    }

    public String getUser_code ()
    {
        return user_code;
    }

    public void setUser_code (String user_code)
    {
        this.user_code = user_code;
    }

    public String getNumber ()
    {
        return number;
    }

    public void setNumber (String number)
    {
        this.number = number;
    }

    public String getClosing_balance ()
{
    return closing_balance;
}

    public void setClosing_balance (String closing_balance)
    {
        this.closing_balance = closing_balance;
    }

    public String getTsp_image ()
{
    return tsp_image;
}

    public void setTsp_image (String tsp_image)
    {
        this.tsp_image = tsp_image;
    }

    public String getAadhar_no ()
{
    return aadhar_no;
}

    public void setAadhar_no (String aadhar_no)
    {
        this.aadhar_no = aadhar_no;
    }

    public String getSource_code ()
    {
        return source_code;
    }

    public void setSource_code (String source_code)
    {
        this.source_code = source_code;
    }

    public String getPincode ()
    {
        return pincode;
    }

    public void setPincode (String pincode)
    {
        this.pincode = pincode;
    }

    public String getResponse ()
{
    return response;
}

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getCredit_limit ()
{
    return credit_limit;
}

    public void setCredit_limit (String credit_limit)
    {
        this.credit_limit = credit_limit;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
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

    public String getDoj ()
    {
        return doj;
    }

    public void setDoj (String doj)
    {
        this.doj = doj;
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
        return "ClassPojo [ac_code = "+ac_code+", user_code = "+user_code+", number = "+number+", closing_balance = "+closing_balance+", tsp_image = "+tsp_image+", aadhar_no = "+aadhar_no+", source_code = "+source_code+", pincode = "+pincode+", response = "+response+", credit_limit = "+credit_limit+", address = "+address+", email = "+email+", name = "+name+", doj = "+doj+", mobile = "+mobile+"]";
    }
}

