package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.SRFCovidWOEEditActivity;
import com.example.e5322.thyrosoft.Fragment.SRFCovidWOEEnterFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Covidpostdata;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SRFCovidWOEmultipart_controller extends AsyncTask<Void, Void, String> {

    private Activity mActivity;
    private String TAG = SRFCovidWOEmultipart_controller.class.getSimpleName();
    private int status_code;
    ProgressDialog progressDialog;
    int flag;
    Covidpostdata covidpostdata;
    SRFCovidWOEEnterFragment srfCovidWOEEnterFragment;
    SRFCovidWOEEditActivity srfCovidWOEEditActivity;

    public SRFCovidWOEmultipart_controller(SRFCovidWOEEnterFragment srfCovidWOEEnterFragment, Covidpostdata covidpostdata) {
        this.srfCovidWOEEnterFragment = srfCovidWOEEnterFragment;
        this.mActivity = srfCovidWOEEnterFragment.getActivity();
        status_code = 0;
        flag = 1;
        this.covidpostdata = covidpostdata;
    }

    public SRFCovidWOEmultipart_controller(SRFCovidWOEEditActivity srfCovidWOEEditActivity, Covidpostdata covidpostdata) {
        this.srfCovidWOEEditActivity = srfCovidWOEEditActivity;
        this.mActivity = srfCovidWOEEditActivity;
        status_code = 0;
        flag = 2;
        this.covidpostdata = covidpostdata;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = GlobalClass.ShowprogressDialog(mActivity);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String strUrl = Api.Cloud_base + "SRFPatientDetails";
        Log.e(TAG, "SRF COVID POST API " + strUrl);

        InputStream inputStream = null;
        String result = "";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strUrl);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            if (flag == 2) {
                builder.addPart("UNIQUEID", new StringBody("" + covidpostdata.getUNIQUEID()));
            }
            builder.addPart("SOURCECODE", new StringBody("" + covidpostdata.getSOURCECODE()));
            builder.addPart("NAME", new StringBody("" + covidpostdata.getNAME()));
            builder.addPart("MOBILE", new StringBody("" + covidpostdata.getMOBILE()));
            builder.addPart("AMOUNTCOLLECTED", new StringBody("" + covidpostdata.getAMOUNTCOLLECTED()));
            builder.addPart("SRFID", new StringBody("" + covidpostdata.getSRFID()));
            builder.addPart("AGE", new StringBody("" + covidpostdata.getAGE()));
            builder.addPart("AGETYPE", new StringBody("" + covidpostdata.getAGETYPE()));
            builder.addPart("GENDER", new StringBody("" + covidpostdata.getGENDER()));
            builder.addPart("PATIENTADDRESS", new StringBody("" + covidpostdata.getPATIENTADDRESS()));
            builder.addPart("PATIENTPINCODE", new StringBody("" + covidpostdata.getPATIENTPINCODE()));
            builder.addPart("SCP", new StringBody("" + covidpostdata.getSCP()));
            builder.addPart("TESTCODE", new StringBody("" + covidpostdata.getTESTCODE()));
            builder.addPart("COLLECTIONADDRESS", new StringBody("" + covidpostdata.getCOLLECTIONADDRESS()));
            builder.addPart("COLLECTIONPINCODE", new StringBody("" + covidpostdata.getCOLLECTIONPINCODE()));
            builder.addPart("SPECIMENDATE", new StringBody("" + covidpostdata.getSPECIMENDATE()));
            builder.addPart("SPECIMENTIME", new StringBody("" + covidpostdata.getSPECIMENTIME()));
            builder.addPart("BARCODE", new StringBody("" + covidpostdata.getBARCODE()));
            builder.addPart("EMAIL", new StringBody("" + covidpostdata.getEMAIL()));
            builder.addPart("WOEMODE",new StringBody("CLISO APP SRF"));
            builder.addPart("APIKEY", new StringBody("" + covidpostdata.getAPIKEY()));

            if(flag == 1){
                builder.addPart("TECHNICIAN", new StringBody("" + covidpostdata.getTECHNICIAN()));
                Log.e(TAG, "\"Post params:- " + "" + "\nTECHNICIAN:" + covidpostdata.getTECHNICIAN());
            }

            if (covidpostdata.getPRESCRIPTION() != null) {
                FileInputStream fileInputStream = new FileInputStream(covidpostdata.getPRESCRIPTION());
                builder.addPart("PRESCRIPTION", new InputStreamBody(fileInputStream, "image/jpeg", "file_name.jpg"));
            }

            if (covidpostdata.getADHAR() != null) {
                FileInputStream adharfilestream = new FileInputStream(covidpostdata.getADHAR());
                builder.addPart("ADHAR", new InputStreamBody(adharfilestream, "image/jpeg", "file_name.jpg"));
            }

            if (covidpostdata.getADHAR1() != null) {
                FileInputStream adharfilestream1 = new FileInputStream(covidpostdata.getADHAR1());
                builder.addPart("ADHAR1", new InputStreamBody(adharfilestream1, "image/jpeg", "file_name.jpg"));
            }

            if (covidpostdata.getVIAIMAGE() != null) {
                FileInputStream vial_InputStream = new FileInputStream(covidpostdata.getVIAIMAGE());
                builder.addPart("VIALIMAGE", new InputStreamBody(vial_InputStream, "image/jpeg", "file_name.jpg"));
            }

            if (covidpostdata.getOTHER() != null) {
                FileInputStream otherfilestream1 = new FileInputStream(covidpostdata.getOTHER());
                builder.addPart("OTHER", new InputStreamBody(otherfilestream1, "image/jpeg", "file_name.jpg"));
            }

            if (covidpostdata.getOTHER1() != null) {
                FileInputStream otherfilestream2 = new FileInputStream(covidpostdata.getOTHER1());
                builder.addPart("OTHER1", new InputStreamBody(otherfilestream2, "image/jpeg", "file_name.jpg"));
            }

            if (covidpostdata.getSELFIE() != null) {
                FileInputStream otherfilestream1 = new FileInputStream(covidpostdata.getSELFIE());
                builder.addPart("SELFIE", new InputStreamBody(otherfilestream1, "image/jpeg", "file_name.jpg"));
            }

            Log.e(TAG, "\"Post params:- " + "" + "\nUNIQUEID:" + covidpostdata.getUNIQUEID() +
                    "\nSOURCECODE:" + covidpostdata.getSOURCECODE() +
                    "\nMOBILE:" + covidpostdata.getMOBILE() +
                    "\nNAME:" + covidpostdata.getNAME() +
                    "\nSRFID:" + covidpostdata.getSRFID() +
                    "\nAGE:" + covidpostdata.getAGE() +
                    "\nAGETYPE:" + covidpostdata.getAGETYPE() +
                    "\nGENDER:" + covidpostdata.getGENDER() +
                    "\nPATIENTADDRESS:" + covidpostdata.getPATIENTADDRESS() +
                    "\nPATIENTPINCODE:" + covidpostdata.getPATIENTPINCODE() +
                    "\nSCP:" + covidpostdata.getSCP() +
                    "\nAPIKEY:" + covidpostdata.getAPIKEY() +
                    "\nTESTCODE:" + covidpostdata.getTESTCODE() +
                    "\nCOLLECTIONADDRESS:" + covidpostdata.getCOLLECTIONADDRESS() +
                    "\nCOLLECTIONPINCODE:" + covidpostdata.getCOLLECTIONPINCODE() +
                    "\nSPECIMENDATE:" + covidpostdata.getSPECIMENDATE() +
                    "\nSPECIMENTIME:" + covidpostdata.getSPECIMENTIME() +
                    "\nBARCODE:" + covidpostdata.getBARCODE() +
                    "\nAMOUNTCOLLECTED:" + covidpostdata.getAMOUNTCOLLECTED() +
                    "\nPRESCRIPTION:" + covidpostdata.getPRESCRIPTION() +
                    "\nADHAR:" + covidpostdata.getADHAR() +
                    "\nADHAR1:" + covidpostdata.getADHAR1() +
                    "\nVIALIMAGE:" + covidpostdata.getVIAIMAGE() +
                    "\nOTHER:" + covidpostdata.getOTHER() +
                    "\nOTHER1:" + covidpostdata.getOTHER1() +
                    "\nSELFIE:" + covidpostdata.getSELFIE() +
                    "\nWOEMODE:CLISO APP SRF");
            httpPost.setEntity(builder.build());
            httpPost.setHeader(Constants.HEADER_USER_AGENT, GlobalClass.getHeaderValue(mActivity));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            status_code = httpResponse.getStatusLine().getStatusCode();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                System.out.println("Response : " + result);
            }

        } catch (Exception e) {
            result = "Something went wrong";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            GlobalClass.hideProgress(mActivity, progressDialog);
            if (status_code == 200) {
                if (response != null && !response.isEmpty()) {
                    if (flag == 1) {
                        srfCovidWOEEnterFragment.getUploadResponse(response);
                    }
                    if (flag == 2) {
                        srfCovidWOEEditActivity.getUploadResponse(response);
                    }
                } else {
                    Global.showCustomToast(mActivity, response);
                }
            } else {
                Global.showCustomToast(mActivity, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}