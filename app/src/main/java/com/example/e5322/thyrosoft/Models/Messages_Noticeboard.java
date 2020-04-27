package com.example.e5322.thyrosoft.Models;

/**
 * Created by e5322 on 28-05-2018.
 */

public class Messages_Noticeboard
{
    private String noticeDate;

    private String messageCode;

    private String isAcknowledged;

    private String noticeMessage;

    private String enterBy;

    public String getNoticeDate ()
    {
        return noticeDate;
    }

    public void setNoticeDate (String noticeDate)
    {
        this.noticeDate = noticeDate;
    }

    public String getMessageCode ()
    {
        return messageCode;
    }

    public void setMessageCode (String messageCode)
    {
        this.messageCode = messageCode;
    }

    public String getIsAcknowledged ()
    {
        return isAcknowledged;
    }

    public void setIsAcknowledged (String isAcknowledged)
    {
        this.isAcknowledged = isAcknowledged;
    }

    public String getNoticeMessage ()
    {
        return noticeMessage;
    }

    public void setNoticeMessage (String noticeMessage)
    {
        this.noticeMessage = noticeMessage;
    }

    public String getEnterBy ()
    {
        return enterBy;
    }

    public void setEnterBy (String enterBy)
    {
        this.enterBy = enterBy;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [noticeDate = "+noticeDate+", messageCode = "+messageCode+", isAcknowledged = "+isAcknowledged+", noticeMessage = "+noticeMessage+", enterBy = "+enterBy+"]";
    }
}

