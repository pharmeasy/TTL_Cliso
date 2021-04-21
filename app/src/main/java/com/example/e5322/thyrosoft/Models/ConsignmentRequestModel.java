package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class ConsignmentRequestModel {


    /**
     * API_KEY : S)DgxLM7wddh@fJ@Kvz7wT1Zdv1d11AUDZbXECkng2c=
     * Consignmen_Barcode : TEST1234
     * ThroughTSPCode : COV01
     * ConsignmentNo : 1234
     * TransitTime : 8hr-12hr
     * Mode : Bus
     * SampleFromCPL : 92
     * SampleFromRPL : 0
     * PackagingDetails : Thermocol box with cooled gel pack
     * FlightName : TNSTC
     * FlightNo : test
     * BSVBarcode :
     * Remarks :
     * TotalSamples : 92
     * ConsignmentTemperature : Between 4C to 12C
     * CourierName : TNSTC
     * pouchReqs : [{"PouchCode":"IKIKIKIK"}]
     */

    private String API_KEY;
    private String Consignmen_Barcode;
    private String ThroughTSPCode;
    private String ConsignmentNo;
    private String TransitTime;
    private String Mode;
    private String SampleFromCPL;
    private String SampleFromRPL;
    private String PackagingDetails;
    private String FlightName;
    private String DispatchTime;
    private String FlightNo;
    private String BSVBarcode;
    private String Remarks;
    private String TotalSamples;
    private String DepTime;
    private String ConsignmentTemperature;
    private String CourierName;
    private String ArrTime;
    private ArrayList<PouchReqsDTO> pouchCodeDtlLists;

    public String getArrTime() {
        return ArrTime;
    }

    public void setArrTime(String arrTime) {
        ArrTime = arrTime;
    }

    public String getDepTime() {
        return DepTime;
    }

    public void setDepTime(String depTime) {
        DepTime = depTime;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public void setAPI_KEY(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public String getConsignmen_Barcode() {
        return Consignmen_Barcode;
    }

    public String getDispatchTime() {
        return DispatchTime;
    }

    public void setDispatchTime(String dispatchTime) {
        DispatchTime = dispatchTime;
    }

    public void setConsignmen_Barcode(String Consignmen_Barcode) {
        this.Consignmen_Barcode = Consignmen_Barcode;
    }

    public String getThroughTSPCode() {
        return ThroughTSPCode;
    }

    public void setThroughTSPCode(String ThroughTSPCode) {
        this.ThroughTSPCode = ThroughTSPCode;
    }

    public String getConsignmentNo() {
        return ConsignmentNo;
    }

    public void setConsignmentNo(String ConsignmentNo) {
        this.ConsignmentNo = ConsignmentNo;
    }

    public String getTransitTime() {
        return TransitTime;
    }

    public void setTransitTime(String TransitTime) {
        this.TransitTime = TransitTime;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String Mode) {
        this.Mode = Mode;
    }

    public String getSampleFromCPL() {
        return SampleFromCPL;
    }

    public void setSampleFromCPL(String SampleFromCPL) {
        this.SampleFromCPL = SampleFromCPL;
    }

    public String getSampleFromRPL() {
        return SampleFromRPL;
    }

    public void setSampleFromRPL(String SampleFromRPL) {
        this.SampleFromRPL = SampleFromRPL;
    }

    public String getPackagingDetails() {
        return PackagingDetails;
    }

    public void setPackagingDetails(String PackagingDetails) {
        this.PackagingDetails = PackagingDetails;
    }

    public String getFlightName() {
        return FlightName;
    }

    public void setFlightName(String FlightName) {
        this.FlightName = FlightName;
    }

    public String getFlightNo() {
        return FlightNo;
    }

    public void setFlightNo(String FlightNo) {
        this.FlightNo = FlightNo;
    }

    public String getBSVBarcode() {
        return BSVBarcode;
    }

    public void setBSVBarcode(String BSVBarcode) {
        this.BSVBarcode = BSVBarcode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public String getTotalSamples() {
        return TotalSamples;
    }

    public void setTotalSamples(String TotalSamples) {
        this.TotalSamples = TotalSamples;
    }

    public String getConsignmentTemperature() {
        return ConsignmentTemperature;
    }

    public void setConsignmentTemperature(String ConsignmentTemperature) {
        this.ConsignmentTemperature = ConsignmentTemperature;
    }

    public String getCourierName() {
        return CourierName;
    }

    public void setCourierName(String CourierName) {
        this.CourierName = CourierName;
    }

    public ArrayList<PouchReqsDTO> getPouchCodeDtlLists() {
        return pouchCodeDtlLists;
    }

    public void setPouchCodeDtlLists(ArrayList<PouchReqsDTO> pouchCodeDtlLists) {
        this.pouchCodeDtlLists = pouchCodeDtlLists;
    }

    public static class PouchReqsDTO {
        /**
         * PouchCode : IKIKIKIK
         */

        private String Barcode;

        public String getBarcode() {
            return Barcode;
        }

        public void setBarcode(String PouchCode) {
            this.Barcode = PouchCode;
        }
    }
}
