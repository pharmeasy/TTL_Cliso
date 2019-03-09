package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */
public class USER_MASTER
{
    private String response;

    private String pincode;

    private String ac_code;

    private String credit_limit;

    private String email;

    private String address;

    private String name;

    private String user_code;

    private String number;

    private String closing_balance;

    private String mobile;

    private String doj;

    public String getResponse ()
{
    return response;
}

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getPincode ()
    {
        return pincode;
    }

    public void setPincode (String pincode)
    {
        this.pincode = pincode;
    }

    public String getAc_code ()
    {
        return ac_code;
    }

    public void setAc_code (String ac_code)
    {
        this.ac_code = ac_code;
    }

    public String getCredit_limit ()
{
    return credit_limit;
}

    public void setCredit_limit (String credit_limit)
    {
        this.credit_limit = credit_limit;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
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

    public String getMobile ()
    {
        return mobile;
    }

    public void setMobile (String mobile)
    {
        this.mobile = mobile;
    }

    public String getDoj ()
    {
        return doj;
    }

    public void setDoj (String doj)
    {
        this.doj = doj;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", pincode = "+pincode+", ac_code = "+ac_code+", credit_limit = "+credit_limit+", email = "+email+", address = "+address+", name = "+name+", user_code = "+user_code+", number = "+number+", closing_balance = "+closing_balance+", mobile = "+mobile+", doj = "+doj+"]";
    }
}

