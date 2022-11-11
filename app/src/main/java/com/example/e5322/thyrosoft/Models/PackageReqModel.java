package com.example.e5322.thyrosoft.Models;

public class PackageReqModel {


    private FactsDTO facts;
    private String ruleName;

    public FactsDTO getFacts() {
        return facts;
    }

    public void setFacts(FactsDTO facts) {
        this.facts = facts;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public static class FactsDTO {
        private int tenant_id;
        private String category;
        private String citi_name;
        private String device;

        public int getTenant_id() {
            return tenant_id;
        }

        public void setTenant_id(int tenant_id) {
            this.tenant_id = tenant_id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCiti_name() {
            return citi_name;
        }

        public void setCiti_name(String citi_name) {
            this.citi_name = citi_name;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }
    }
}
