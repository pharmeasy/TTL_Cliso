package com.example.e5322.thyrosoft.MainModelForAllTests;

import java.util.ArrayList;

/**
 * Created by E5322 on 07-06-2018.
 */

public class POPGETALLTESTS {

    ArrayList<Barcodes_POP> barcodes;
    ArrayList<Childs_POP> childs;

    String code,fasting,name,product,type;
    Rate_Pop rate;

    public ArrayList<Barcodes_POP> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(ArrayList<Barcodes_POP> barcodes) {
        this.barcodes = barcodes;
    }

    public ArrayList<Childs_POP> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<Childs_POP> childs) {
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

    public Rate_Pop getRate() {
        return rate;
    }

    public void setRate(Rate_Pop rate) {
        this.rate = rate;
    }
}
