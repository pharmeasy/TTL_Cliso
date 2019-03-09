package com.example.e5322.thyrosoft.MainModelForAllTests;

import java.util.ArrayList;

/**
 * Created by E5322 on 07-06-2018.
 */

public class TESTS_GETALLTESTS {

    ArrayList<Barcodes_Tets> barcodes;
    ArrayList<Childs_Test> childs;

    Rate_Test rate;
    String code,fasting,name,product,type;

    public ArrayList<Barcodes_Tets> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(ArrayList<Barcodes_Tets> barcodes) {
        this.barcodes = barcodes;
    }

    public ArrayList<Childs_Test> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<Childs_Test> childs) {
        this.childs = childs;
    }

    public Rate_Test getRate() {
        return rate;
    }

    public void setRate(Rate_Test rate) {
        this.rate = rate;
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
}
