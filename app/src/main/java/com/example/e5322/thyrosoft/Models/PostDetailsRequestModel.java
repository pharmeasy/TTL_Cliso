package com.example.e5322.thyrosoft.Models;

public class PostDetailsRequestModel {


    /**
     * sourceCode : COV01
     * patientid : COV03167560814735886
     * Occupation : ENGINEER,doctor
     * disease : FEVER
     */

    private String sourceCode;
    private String patientid;
    private String Occupation;
    private String disease;

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getOccupation() {
        return Occupation;
    }

    public void setOccupation(String Occupation) {
        this.Occupation = Occupation;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
}
