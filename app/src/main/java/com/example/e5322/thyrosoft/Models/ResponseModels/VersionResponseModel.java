package com.example.e5322.thyrosoft.Models.ResponseModels;

public class VersionResponseModel {


    /**
     * Version : 1.0.1.55
     * isoffline : true
     * resId : RES0000
     * response : Success
     * syncproduct : 1
     * url : http://www.charbi.com/CDN/Applications/Android/ThyrosoftLite.apk
     */

    private String Version;
    private boolean isoffline;
    private String resId;
    private String response;
    private int syncproduct;
    private String url;

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public boolean isIsoffline() {
        return isoffline;
    }

    public void setIsoffline(boolean isoffline) {
        this.isoffline = isoffline;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getSyncproduct() {
        return syncproduct;
    }

    public void setSyncproduct(int syncproduct) {
        this.syncproduct = syncproduct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
