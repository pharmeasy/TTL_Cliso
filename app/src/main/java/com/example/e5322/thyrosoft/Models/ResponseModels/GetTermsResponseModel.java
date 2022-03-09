package com.example.e5322.thyrosoft.Models.ResponseModels;

import com.google.gson.annotations.SerializedName;

public class GetTermsResponseModel {

    @SerializedName("Response")
    private String response;
    @SerializedName("ResponseId")
    private String responseId;
    @SerializedName("Url")
    private String url;
    @SerializedName("TermFlag")
    private Boolean termFlag;
    @SerializedName("Date")
    private String date;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getTermFlag() {
        return termFlag;
    }

    public void setTermFlag(Boolean termFlag) {
        this.termFlag = termFlag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
