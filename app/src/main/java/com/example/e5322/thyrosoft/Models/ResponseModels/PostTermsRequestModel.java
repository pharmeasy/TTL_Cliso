package com.example.e5322.thyrosoft.Models.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class PostTermsRequestModel {

    @SerializedName("ClientCode")
    private String clientCode;
    @SerializedName("ClientType")
    private String clientType;
    @SerializedName("AppId")
    private Integer appId;
    @SerializedName("AgreementLink")
    private String agreementLink;
    @SerializedName("Latitude")
    private String latitude;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("IPAddress")
    private String iPAddress;
    @SerializedName("DeviceID")
    private String deviceID;
    @SerializedName("Remarks")
    private String remarks;

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAgreementLink() {
        return agreementLink;
    }

    public void setAgreementLink(String agreementLink) {
        this.agreementLink = agreementLink;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
