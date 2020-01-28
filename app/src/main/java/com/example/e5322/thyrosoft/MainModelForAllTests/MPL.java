package com.example.e5322.thyrosoft.MainModelForAllTests;

import java.util.ArrayList;

/**
 * Created by E5322 on 07-06-2018.
 */

public class MPL {
    ArrayList<Barcodes_MPL> barcodes;
    ArrayList<Childs_MPL> childs;
    String code,fasting,name,product;
    Rate_MPL rate;
    String type,subtypes,trf;

    public ArrayList<Barcodes_MPL> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(ArrayList<Barcodes_MPL> barcodes) {
        this.barcodes = barcodes;
    }

    public ArrayList<Childs_MPL> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<Childs_MPL> childs) {
        this.childs = childs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFasting() {
        return fasting;
    }

    public void setFasting(String fasting) {
        this.fasting = fasting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrf() {
        return trf;
    }

    public void setTrf(String trf) {
        this.trf = trf;
    }

    public Rate_MPL getRate() {
        return rate;
    }

    public void setRate(Rate_MPL rate) {
        this.rate = rate;
    }

    public String getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(String subtypes) {
        this.subtypes = subtypes;
    }
}
