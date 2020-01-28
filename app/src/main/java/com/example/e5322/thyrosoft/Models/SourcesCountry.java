package com.example.e5322.thyrosoft.Models;

/**
 * Created by E5322 on 22-05-2018.
 */

public class SourcesCountry {

        private String response;

        private Source[] source;

        private String cities;

        private States[] states;

        private Countries[] countries;

        private String resId;

        public String getResponse ()
    {
        return response;
    }

        public void setResponse (String response)
        {
            this.response = response;
        }

        public Source[] getSource ()
        {
            return source;
        }

        public void setSource (Source[] source)
        {
            this.source = source;
        }

        public String getCities ()
    {
        return cities;
    }

        public void setCities (String cities)
        {
            this.cities = cities;
        }

        public States[] getStates ()
        {
            return states;
        }

        public void setStates (States[] states)
        {
            this.states = states;
        }

        public Countries[] getCountries ()
        {
            return countries;
        }

        public void setCountries (Countries[] countries)
        {
            this.countries = countries;
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
            return "ClassPojo [response = "+response+", source = "+source+", cities = "+cities+", states = "+states+", countries = "+countries+", resId = "+resId+"]";
        }

    public class States
    {
        private String stateId;

        private String stateName;

        public String getStateId ()
        {
            return stateId;
        }

        public void setStateId (String stateId)
        {
            this.stateId = stateId;
        }

        public String getStateName ()
        {
            return stateName;
        }

        public void setStateName (String stateName)
        {
            this.stateName = stateName;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [stateId = "+stateId+", stateName = "+stateName+"]";
        }
    }
    public class Source
    {
        private String sourceName;

        private String sourceId;

        public String getSourceName ()
        {
            return sourceName;
        }

        public void setSourceName (String sourceName)
        {
            this.sourceName = sourceName;
        }

        public String getSourceId ()
        {
            return sourceId;
        }

        public void setSourceId (String sourceId)
        {
            this.sourceId = sourceId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [sourceName = "+sourceName+", sourceId = "+sourceId+"]";
        }
    }
    public class Countries
    {
        private String countryId;

        private String countryName;

        public String getCountryId ()
        {
            return countryId;
        }

        public void setCountryId (String countryId)
        {
            this.countryId = countryId;
        }

        public String getCountryName ()
        {
            return countryName;
        }

        public void setCountryName (String countryName)
        {
            this.countryName = countryName;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [countryId = "+countryId+", countryName = "+countryName+"]";
        }
    }



}
