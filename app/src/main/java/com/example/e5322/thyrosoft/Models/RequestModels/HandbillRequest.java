package com.example.e5322.thyrosoft.Models.RequestModels;

import java.io.File;

public class HandbillRequest {


    /**
     * Name : sumitsahani
     * Address : chembur vashi naka mahada colony mumbai 40074
     * Pincode : 400074
     * Mobile : 9004844180
     * Email : sumit.sahani@thyrocare.com
     * Enteredby : 7738943013
     * Action : 0
     * ImgStatus : 1
     * Imgid : 2
     */

    private String Name;
    private String Address;
    private String Pincode;
    private String Mobile;
    private String Email;
    private String Enteredby;
    private String Action;
    private int ImgStatus;
    private String Imgid;
    private File Files;

    public File getFiles() {
        return Files;
    }

    public void setFiles(File files) {
        Files = files;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
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

    public String getEnteredby() {
        return Enteredby;
    }

    public void setEnteredby(String Enteredby) {
        this.Enteredby = Enteredby;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String Action) {
        this.Action = Action;
    }

    public int getImgStatus() {
        return ImgStatus;
    }

    public void setImgStatus(int ImgStatus) {
        this.ImgStatus = ImgStatus;
    }

    public String getImgid() {
        return Imgid;
    }

    public void setImgid(String Imgid) {
        this.Imgid = Imgid;
    }
}
