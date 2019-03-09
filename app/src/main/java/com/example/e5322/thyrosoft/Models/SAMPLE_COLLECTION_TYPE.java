package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 28-03-2018.
 */
public class SAMPLE_COLLECTION_TYPE
{
    private String sample_collection_type_name;

    private String sample_collection_type_rank;

    public String getSample_collection_type_name ()
    {
        return sample_collection_type_name;
    }

    public void setSample_collection_type_name (String sample_collection_type_name)
    {
        this.sample_collection_type_name = sample_collection_type_name;
    }

    public String getSample_collection_type_rank ()
    {
        return sample_collection_type_rank;
    }

    public void setSample_collection_type_rank (String sample_collection_type_rank)
    {
        this.sample_collection_type_rank = sample_collection_type_rank;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [sample_collection_type_name = "+sample_collection_type_name+", sample_collection_type_rank = "+sample_collection_type_rank+"]";
    }
}