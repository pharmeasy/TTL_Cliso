package com.example.e5322.thyrosoft.Models;

public class OTPrequest {


    /**
     * AppId : 1
     * Version : 90
     * Purpose : OTP
     */

    private String AppId;
    private String Version;
    private String Purpose;

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String AppId) {
        this.AppId = AppId;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String Purpose) {
        this.Purpose = Purpose;
    }
}
