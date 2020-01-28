package com.example.e5322.thyrosoft.Models.PincodeMOdel;

public class CommunicationRepsponseModel {

    private String response;

    private CommunicationMaster[] communicationMaster;

    private Inboxes[] inboxes;

    private Sents[] sents;

    private String resId;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public CommunicationMaster[] getCommunicationMaster ()
    {
        return communicationMaster;
    }

    public void setCommunicationMaster (CommunicationMaster[] communicationMaster)
    {
        this.communicationMaster = communicationMaster;
    }

    public Inboxes[] getInboxes ()
    {
        return inboxes;
    }

    public void setInboxes (Inboxes[] inboxes)
    {
        this.inboxes = inboxes;
    }

    public Sents[] getSents ()
    {
        return sents;
    }

    public void setSents (Sents[] sents)
    {
        this.sents = sents;
    }

    public String getResId ()
    {
        return resId;
    }

    public void setResId (String resId)
    {
        this.resId = resId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", communicationMaster = "+communicationMaster+", inboxes = "+inboxes+", sents = "+sents+", resId = "+resId+"]";
    }
}
