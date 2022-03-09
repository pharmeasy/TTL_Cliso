package com.example.e5322.thyrosoft.Models.ResponseModels;

public class FeedbackQuestionRequestModel {

    /**
     * Type : Cliso
     */

    private String Type;
    private String sourcecode;

    public String getSourcecode() {
        return sourcecode;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }


    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
}
