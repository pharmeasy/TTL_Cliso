package com.example.e5322.thyrosoft.Models;

import java.io.File;

public class TRFModel {
    String Product;
    File trf_image;

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public File getTrf_image() {
        return trf_image;
    }

    public void setTrf_image(File trf_image) {
        this.trf_image = trf_image;
    }
}
