package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */

public class BRAND_LIST
{
    private Brand_type[] brand_type;

    private String brand_rank;

    private String brand_name;

    public Brand_type[] getBrand_type ()
    {
        return brand_type;
    }

    public void setBrand_type (Brand_type[] brand_type)
    {
        this.brand_type = brand_type;
    }

    public String getBrand_rank ()
    {
        return brand_rank;
    }

    public void setBrand_rank (String brand_rank)
    {
        this.brand_rank = brand_rank;
    }

    public String getBrand_name ()
    {
        return brand_name;
    }

    public void setBrand_name (String brand_name)
    {
        this.brand_name = brand_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [brand_type = "+brand_type+", brand_rank = "+brand_rank+", brand_name = "+brand_name+"]";
    }
}

