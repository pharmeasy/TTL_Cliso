package com.example.e5322.thyrosoft.SourceILSModel;

/**
 * Created by E5322 on 29-05-2018.
 */
public class REF_DR {
    private String pincode;

    private String id;

    private String email;

    private String status;

    private String address;

    private String name;

    private String mobile;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [pincode = " + pincode + ", id = " + id + ", email = " + email + ", address = " + address + ", name = " + name + ", mobile = " + mobile + "]";
    }
}

