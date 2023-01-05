package com.example.e5322.thyrosoft.HHHtest.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.HHHtest.HHHEnterFrag;
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

public class HHHController extends AsyncTask<Void, Void, String> {

    private Activity mActivity;
    private String TAG = HHHController.class.getSimpleName();
    private int status_code;
    ProgressDialog progressDialog;
    int flag = 0;
    Covidpostdata covidpostdata;
    HHHEnterFrag hhhEnterFrag;


    public HHHController(HHHEnterFrag hhhEnterFrag, Activity activity, Covidpostdata covidpostdata) {
        this.hhhEnterFrag = hhhEnterFrag;
        this.mActivity = activity;
        status_code = 0;
        flag = 1;
        this.covidpostdata = covidpostdata;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = GlobalClass.ShowprogressDialog(mActivity);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String strUrl = "";
        strUrl = Api.COVID + "PICKSO/API/PoctHhh/WOEPatientDetails";

        Log.e(TAG, "COVID POST API " + strUrl);

        InputStream inputStream = null;
        String result = "";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strUrl);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            if (flag == 1) {
                builder.addPart("UNIQUEID", new StringBody(""));
                builder.addPart("PRESCRIPTION", new StringBody(""));
                builder.addPart("SOURCECODE", new StringBody("" + covidpostdata.getSOURCECODE()));
                builder.addPart("MOBILE", new StringBody("" + covidpostdata.getMOBILE()));
                builder.addPart("NAME", new StringBody("" + covidpostdata.getNAME()));
                builder.addPart("AMOUNTCOLLECTED", new StringBody("" + covidpostdata.getAMOUNTCOLLECTED()));
                builder.addPart("TESTCODE", new StringBody("" + covidpostdata.getTESTCODE()));
                builder.addPart("SPECIMENTYPE", new StringBody("" + covidpostdata.getSPECIMENTYPE()));
                builder.addPart("AGE", new StringBody("" + covidpostdata.getAGE()));
                builder.addPart("BARCODE", new StringBody("" + covidpostdata.getBARCODE()));
                builder.addPart("GENDER", new StringBody("" + covidpostdata.getGENDER()));
                builder.addPart("ENTERBY", new StringBody("" + covidpostdata.getENTERBY()));
                builder.addPart("CAMPID", new StringBody("" + covidpostdata.getCAMPID()));
                builder.addPart("ADDRESS", new StringBody("" + covidpostdata.getADDRESS()));
                builder.addPart("EMAIL ", new StringBody("" + covidpostdata.getEMAIL()));
                builder.addPart("WOEFLOW ", new StringBody("" + 0));

                if (covidpostdata.getHOSPITAL() != null) {
                    builder.addPart("HOSPITAL", new StringBody("" + covidpostdata.getHOSPITAL()));
                }

                if (covidpostdata.getADHAR() != null) {
                    FileInputStream adharfilestream = new FileInputStream(covidpostdata.getADHAR());
                    builder.addPart("ADHAR", new InputStreamBody(adharfilestream, "image/jpeg", "file_name.jpg"));

                }
                if (covidpostdata.getADHAR1() != null) {
                    FileInputStream adharfilestream1 = new FileInputStream(covidpostdata.getADHAR1());
                    builder.addPart("ADHAR1", new InputStreamBody(adharfilestream1, "image/jpeg", "file_name.jpg"));
                }

                if (covidpostdata.getTRF() != null) {
                    FileInputStream trffilestream = new FileInputStream(covidpostdata.getTRF());
                    builder.addPart("TRF", new InputStreamBody(trffilestream, "image/jpeg", "file_name.jpg"));
                }

                if (covidpostdata.getTRF1() != null) {
                    FileInputStream trffilestream1 = new FileInputStream(covidpostdata.getTRF1());
                    builder.addPart("TRF1", new InputStreamBody(trffilestream1, "image/jpeg", "file_name.jpg"));

                }

                if (covidpostdata.getOTHER() != null) {
                    FileInputStream otherfilestream2 = new FileInputStream(covidpostdata.getOTHER());
                    builder.addPart("OTHER", new InputStreamBody(otherfilestream2, "image/jpeg", "file_name.jpg"));

                }
                if (covidpostdata.getOTHER1() != null) {
                    FileInputStream otherfilestream2 = new FileInputStream(covidpostdata.getOTHER1());
                    builder.addPart("OTHER1", new InputStreamBody(otherfilestream2, "image/jpeg", "file_name.jpg"));

                }

                System.out.println("\"Post params:- " + "" + "\nSOURCECODE:" + covidpostdata.getSOURCECODE() +
                        "\nMOBILE:" + covidpostdata.getMOBILE() + "\nNAME:" + covidpostdata.getNAME()
                        + "\nAMOUNTCOLLECTED:" + covidpostdata.getAMOUNTCOLLECTED()
                        + "\nTESTCODE:" + covidpostdata.getTESTCODE()
                        + "\nAGE:" + covidpostdata.getAGE()
                        + "\nBARCODE:" + covidpostdata.getBARCODE()
                        + "\nGENDER:" + covidpostdata.getGENDER()
                        + "\nADDRESS:" + covidpostdata.getADDRESS()
                        + "\nEMAIL:" + covidpostdata.getEMAIL()
                        + "\nSPECIMENTYPE:" + covidpostdata.getSPECIMENTYPE()
                        + "\nENTERBY:" + covidpostdata.getENTERBY()
                        + "\nTESTCODE:" + covidpostdata.getTESTCODE()
                        + "\nHOSPITAL:" + covidpostdata.getHOSPITAL()
                        + "\nCAMPID:" + covidpostdata.getCAMPID()
                        + "\nADHAR:" + covidpostdata.getADHAR()
                        + "\nADHAR1:" + covidpostdata.getADHAR1()
                        + "\nTRF:" + covidpostdata.getTRF()
                        + "\nTRF1:" + covidpostdata.getTRF1()
                        + "\nVIALIMAGE:" + covidpostdata.getVIAIMAGE()
                        + "\nWOEFLOW:" + "0"
                        + "\nOTHER:" + covidpostdata.getOTHER());
            }


            httpPost.setEntity(builder.build());
            httpPost.setHeader(Constants.HEADER_USER_AGENT,  GlobalClass.getHeaderValue(mActivity));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (status_code == 200) {
            if (response != null && !response.isEmpty()) {
                if (flag == 1) {
                    hhhEnterFrag.getUploadResponse(response);
                }
            } else {
                Global.showCustomToast(mActivity, response);
            }
        } else {
            Global.showCustomToast(mActivity, response);
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

