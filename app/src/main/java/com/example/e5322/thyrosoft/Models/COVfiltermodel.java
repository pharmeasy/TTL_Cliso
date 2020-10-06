package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class COVfiltermodel {

    /**
     * ResId : RES0000
     * Response : SUCCESS
     * Status : [{"Id":"100","Status":"ALL"},{"Id":"2","Status":"PENDING"},{"Id":"1","Status":"WOE DONE"},{"Id":"0","Status":"APPROVED"},{"Id":"-1","Status":"REJECTED"}]
     */

    private String ResId;
    private String Response;
    private List<StatusBean> Status;

    public String getResId() {
        return ResId;
    }

    public void setResId(String ResId) {
        this.ResId = ResId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public List<StatusBean> getStatus() {
        return Status;
    }

    public void setStatus(List<StatusBean> Status) {
        this.Status = Status;
    }

    public static class StatusBean {
        /**
         * Id : 100
         * Status : ALL
         */

        private String Id;
        private String Status;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }
    }
}

