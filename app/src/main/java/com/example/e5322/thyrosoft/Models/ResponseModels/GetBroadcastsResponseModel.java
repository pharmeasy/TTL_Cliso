package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.io.Serializable;
import java.util.ArrayList;

public class GetBroadcastsResponseModel {

    private String resId;
    private String response;
    private ArrayList<MessagesBean> messages;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<MessagesBean> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessagesBean> messages) {
        this.messages = messages;
    }

    public static class MessagesBean implements Serializable {
        private String enterBy;
        private String imgURL;
        private String isAcknowledged;
        private String messageCode;
        private String noticeDate;
        private String noticeMessage;
        private String title;

        public String getEnterBy() {
            return enterBy;
        }

        public void setEnterBy(String enterBy) {
            this.enterBy = enterBy;
        }

        public String getImgURL() {
            return imgURL;
        }

        public void setImgURL(String imgURL) {
            this.imgURL = imgURL;
        }

        public String getIsAcknowledged() {
            return isAcknowledged;
        }

        public void setIsAcknowledged(String isAcknowledged) {
            this.isAcknowledged = isAcknowledged;
        }

        public String getMessageCode() {
            return messageCode;
        }

        public void setMessageCode(String messageCode) {
            this.messageCode = messageCode;
        }

        public String getNoticeDate() {
            return noticeDate;
        }

        public void setNoticeDate(String noticeDate) {
            this.noticeDate = noticeDate;
        }

        public String getNoticeMessage() {
            return noticeMessage;
        }

        public void setNoticeMessage(String noticeMessage) {
            this.noticeMessage = noticeMessage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
