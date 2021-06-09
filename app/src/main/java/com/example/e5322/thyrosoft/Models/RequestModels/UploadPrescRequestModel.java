package com.example.e5322.thyrosoft.Models.RequestModels;

import java.io.File;

public class UploadPrescRequestModel {
    String SourceCode, Patientid, DocType, ENTRYBY;
    File ImgUrl;

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String sourceCode) {
        SourceCode = sourceCode;
    }

    public String getPatientid() {
        return Patientid;
    }

    public void setPatientid(String patientid) {
        Patientid = patientid;
    }

    public String getDocType() {
        return DocType;
    }

    public void setDocType(String docType) {
        DocType = docType;
    }

    public String getENTRYBY() {
        return ENTRYBY;
    }

    public void setENTRYBY(String ENTRYBY) {
        this.ENTRYBY = ENTRYBY;
    }

    public File getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(File imgUrl) {
        ImgUrl = imgUrl;
    }
}
