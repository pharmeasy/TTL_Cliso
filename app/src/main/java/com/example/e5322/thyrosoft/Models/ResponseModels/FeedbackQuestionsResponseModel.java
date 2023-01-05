package com.example.e5322.thyrosoft.Models.ResponseModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class FeedbackQuestionsResponseModel implements Parcelable {

    /**
     * Resid : RES0000
     * Response : Success
     * Output : [{"Id":"1","Questions":"TAT of reporting"},{"Id":"2","Questions":"LME/Pick-up support facility"},{"Id":"3","Questions":"Support from BD officer on business ideas"},{"Id":"4","Questions":"Operational support from PIN officers"},{"Id":"5","Questions":"Effectiveness of broadcast/Notice board from company"},{"Id":"6","Questions":"Support from LAB on quality related concern"},{"Id":"7","Questions":"Trainings provided & effectiveness"},{"Id":"8","Questions":"Overall Experience"},{"Id":"9","Questions":"Remark"}]
     */

    private String Resid;
    private String Response;
    private ArrayList<OutputDTO> Output;
    private int FeedbackFlag;

    protected FeedbackQuestionsResponseModel(Parcel in) {
        Resid = in.readString();
        Response = in.readString();
        FeedbackFlag = in.readInt();
    }

    public static final Creator<FeedbackQuestionsResponseModel> CREATOR = new Creator<FeedbackQuestionsResponseModel>() {
        @Override
        public FeedbackQuestionsResponseModel createFromParcel(Parcel in) {
            return new FeedbackQuestionsResponseModel(in);
        }

        @Override
        public FeedbackQuestionsResponseModel[] newArray(int size) {
            return new FeedbackQuestionsResponseModel[size];
        }
    };

    public int getToShow() {
        return FeedbackFlag;
    }

    public void setToShow(int toShow) {
        this.FeedbackFlag = toShow;
    }

    public String getResid() {
        return Resid;
    }

    public void setResid(String Resid) {
        this.Resid = Resid;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public ArrayList<OutputDTO> getOutput() {
        return Output;
    }

    public void setOutput(ArrayList<OutputDTO> Output) {
        this.Output = Output;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Resid);
        dest.writeString(Response);
        dest.writeInt(FeedbackFlag);
    }

    public static class OutputDTO implements Parcelable {
        /**
         * Id : 1
         * Questions : TAT of reporting
         */

        private String Id;
        private String Questions;
        int rating = 0;

        protected OutputDTO(Parcel in) {
            Id = in.readString();
            Questions = in.readString();
            rating = in.readInt();
        }

        public static final Creator<OutputDTO> CREATOR = new Creator<OutputDTO>() {
            @Override
            public OutputDTO createFromParcel(Parcel in) {
                return new OutputDTO(in);
            }

            @Override
            public OutputDTO[] newArray(int size) {
                return new OutputDTO[size];
            }
        };

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getQuestions() {
            return Questions;
        }

        public void setQuestions(String Questions) {
            this.Questions = Questions;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(Id);
            dest.writeString(Questions);
            dest.writeInt(rating);
        }
    }
}
