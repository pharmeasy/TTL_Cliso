package com.example.e5322.thyrosoft.Models;

public class LeadReqModel {


    /**
     * Name : Sachin M
     * Mobile : 7738943013
     * Email : sachin.mejari@thyrocare.com
     * Address : B 601 Sagar Srushti Mumbai
     * Pincode : 400078
     * Remarks : Want to become TSP
     * Purpose : TSP
     * EntryBy : 7738943013
     * AppName : Diggy
     */

    private String Name;
    private String Mobile;
    private String Email;
    private String Address;
    private String Pincode;
    private String Remarks;
    private String Purpose;
    private String EntryBy;
    private String AppName;
    private String Channel;
    private String From;


    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channel) {
        Channel = channel;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String Pincode) {
        this.Pincode = Pincode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String Purpose) {
        this.Purpose = Purpose;
    }

    public String getEntryBy() {
        return EntryBy;
    }

    public void setEntryBy(String EntryBy) {
        this.EntryBy = EntryBy;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }
}
