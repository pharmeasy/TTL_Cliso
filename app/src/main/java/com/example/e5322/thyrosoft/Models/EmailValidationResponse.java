package com.example.e5322.thyrosoft.Models;

public class EmailValidationResponse {

    /**
     * Message : VALID EMAIL ID
     * Sucess : TRUE
     */

    private String Message;
    private String Sucess;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getSucess() {
        return Sucess;
    }

    public void setSucess(String Sucess) {
        this.Sucess = Sucess;
    }
}
