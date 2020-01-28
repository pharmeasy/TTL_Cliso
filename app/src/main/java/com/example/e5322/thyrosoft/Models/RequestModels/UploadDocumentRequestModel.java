package com.example.e5322.thyrosoft.Models.RequestModels;

public class UploadDocumentRequestModel {
    private String apikey, code, letter_type, purpose, attached_file, remarks, ENTERED_BY, IsB2B, expiryDate, attached_data;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLetter_type() {
        return letter_type;
    }

    public void setLetter_type(String letter_type) {
        this.letter_type = letter_type;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAttached_file() {
        return attached_file;
    }

    public void setAttached_file(String attached_file) {
        this.attached_file = attached_file;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getENTERED_BY() {
        return ENTERED_BY;
    }

    public void setENTERED_BY(String ENTERED_BY) {
        this.ENTERED_BY = ENTERED_BY;
    }

    public String getIsB2B() {
        return IsB2B;
    }

    public void setIsB2B(String isB2B) {
        IsB2B = isB2B;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAttached_data() {
        return attached_data;
    }

    public void setAttached_data(String attached_data) {
        this.attached_data = attached_data;
    }
}
