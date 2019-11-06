package com.example.e5322.thyrosoft.Models;

import java.io.File;

public class ComposeCommPOSTModel {
    String apiKey,userCode,type,communication,forwardTo,Source;
    File file;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
