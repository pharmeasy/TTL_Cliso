package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class FeedbackPostQuestionsRequestModel {

    private List<FeedbacksDTO> feedbacks;

    public List<FeedbacksDTO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbacksDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public static class FeedbacksDTO {
        /**
         * QuestionId : 1
         * Rating : 10
         * Remark : Excellent
         * EntryBy : 3126E
         */

        private int QuestionId;
        private String Rating;
        private String Remark;
        private String EntryBy;

        public Integer getQuestionId() {
            return QuestionId;
        }

        public void setQuestionId(Integer QuestionId) {
            this.QuestionId = QuestionId;
        }

        public String getRating() {
            return Rating;
        }

        public void setRating(String Rating) {
            this.Rating = Rating;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getEntryBy() {
            return EntryBy;
        }

        public void setEntryBy(String EntryBy) {
            this.EntryBy = EntryBy;
        }
    }
}
