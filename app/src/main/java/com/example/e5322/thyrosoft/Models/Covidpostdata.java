package com.example.e5322.thyrosoft.Models;

import java.io.File;

public class Covidpostdata {
    String UNIQUEID ;
    String SOURCECODE;
    String AMOUNTCOLLECTED;
    String MOBILE;
    String NAME;
    String AGE;
    String GENDER;
    String BARCODE;
    String TESTCODE;
    File PRESCRIPTION ;
    File ADHAR ;
    File ADHAR1;
    File TRF;
    File TRF1;
    File VIAIMAGE;
    File OTHER;
    File OTHER1;
    File SELFIE;

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getBARCODE() {
        return BARCODE;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public String getAGE() {
        return AGE;
    }

    public void setAGE(String AGE) {
        this.AGE = AGE;
    }

    public String getTESTCODE() {
        return TESTCODE;
    }

    public void setTESTCODE(String TESTCODE) {
        this.TESTCODE = TESTCODE;
    }

    public File getSELFIE() {
        return SELFIE;
    }

    public void setSELFIE(File SELFIE) {
        this.SELFIE = SELFIE;
    }

    public String getAMOUNTCOLLECTED() {
        return AMOUNTCOLLECTED;
    }

    public void setAMOUNTCOLLECTED(String AMOUNTCOLLECTED) {
        this.AMOUNTCOLLECTED = AMOUNTCOLLECTED;
    }

    public File getOTHER1() {
        return OTHER1;
    }

    public void setOTHER1(File OTHER1) {
        this.OTHER1 = OTHER1;
    }

    public File getOTHER() {
        return OTHER;
    }

    public File getVIAIMAGE() {
        return VIAIMAGE;
    }

    public void setOTHER(File OTHER) {
        this.OTHER = OTHER;
    }

    public void setVIAIMAGE(File VIAIMAGE) {
        this.VIAIMAGE = VIAIMAGE;
    }

    public String getUNIQUEID() {
        return UNIQUEID;
    }

    public void setUNIQUEID(String UNIQUEID) {
        this.UNIQUEID = UNIQUEID;
    }

    public File getADHAR1() {
        return ADHAR1;
    }

    public void setADHAR1(File ADHAR1) {
        this.ADHAR1 = ADHAR1;
    }

    public void setTRF1(File TRF1) {
        this.TRF1 = TRF1;
    }

    public File getTRF1() {
        return TRF1;
    }

    public String getSOURCECODE() {
        return SOURCECODE;
    }

    public void setSOURCECODE(String SOURCECODE) {
        this.SOURCECODE = SOURCECODE;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public File getPRESCRIPTION() {
        return PRESCRIPTION;
    }

    public void setPRESCRIPTION(File PRESCRIPTION) {
        this.PRESCRIPTION = PRESCRIPTION;
    }

    public File getADHAR() {
        return ADHAR;
    }

    public void setADHAR(File ADHAR) {
        this.ADHAR = ADHAR;
    }

    public File getTRF() {
        return TRF;
    }

    public void setTRF(File TRF) {
        this.TRF = TRF;
    }
}
