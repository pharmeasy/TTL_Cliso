package com.example.e5322.thyrosoft.Models;

public class LeadBookingResponseModel {
    String RespId;
    String ResponseMessage;
    String LeadId;
    String IsCompleteLeadDtl;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    String Message;

    public LeadBookingResponseModel() {
    }

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String respId) {
        RespId = respId;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public String getLeadId() {
        return LeadId;
    }

    public void setLeadId(String leadId) {
        LeadId = leadId;
    }

    public String getIsCompleteLeadDtl() {
        return IsCompleteLeadDtl;
    }

    public void setIsCompleteLeadDtl(String isCompleteLeadDtl) {
        IsCompleteLeadDtl = isCompleteLeadDtl;
    }
}
