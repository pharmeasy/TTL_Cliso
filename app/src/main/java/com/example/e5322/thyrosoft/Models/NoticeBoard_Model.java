package com.example.e5322.thyrosoft.Models;

/**
 * Created by e5322 on 28-05-2018.
 */

public class NoticeBoard_Model {
    private String response;

    private String resId;


    private Messages_Noticeboard[] messages;

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getResId ()
    {
        return resId;
    }

    public void setResId (String resId)
    {
        this.resId = resId;
    }

    public Messages_Noticeboard[] getMessages ()
    {
        return messages;
    }

    public void setMessages (Messages_Noticeboard[] messages)
    {
        this.messages = messages;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", resId = "+resId+", messages = "+messages+"]";
    }
}


