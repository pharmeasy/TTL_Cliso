package com.example.e5322.thyrosoft.FAQ_Main_Model;

public class FAQandANSArray
{
    private String FAQ;

    private String ACKSTATUS;



    private String ENTERED_NAME;

    private String ACKDATE;

    private String Ack_TITLE;

    private String ACKREMARK;

    public String getFAQ ()
    {
        return FAQ;
    }

    public void setFAQ (String FAQ)
    {
        this.FAQ = FAQ;
    }

    public String getACKSTATUS ()
    {
        return ACKSTATUS;
    }

    public void setACKSTATUS (String ACKSTATUS)
    {
        this.ACKSTATUS = ACKSTATUS;
    }

    public String getENTERED_NAME ()
    {
        return ENTERED_NAME;
    }

    public void setENTERED_NAME (String ENTERED_NAME)
    {
        this.ENTERED_NAME = ENTERED_NAME;
    }

    public String getACKDATE ()
    {
        return ACKDATE;
    }

    public void setACKDATE (String ACKDATE)
    {
        this.ACKDATE = ACKDATE;
    }

    public String getAck_TITLE() {
        return Ack_TITLE;
    }

    public void setAck_TITLE(String ack_TITLE) {
        Ack_TITLE = ack_TITLE;
    }

    public String getACKREMARK ()
    {
        return ACKREMARK;
    }

    public void setACKREMARK (String ACKREMARK)
    {
        this.ACKREMARK = ACKREMARK;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [FAQ = "+FAQ+", ACKSTATUS = "+ACKSTATUS+", ENTERED_NAME = "+ENTERED_NAME+", ACKDATE = "+ACKDATE+", TITLE = "+Ack_TITLE+", ACKREMARK = "+ACKREMARK+"]";
    }
}


