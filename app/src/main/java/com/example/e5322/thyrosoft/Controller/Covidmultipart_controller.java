package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.CovidEditActivity;
import com.example.e5322.thyrosoft.Fragment.Covidenter_Frag;
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

public class Covidmultipart_controller extends AsyncTask<Void, Void, String> {

    private Activity mActivity;
    private String TAG = Covidmultipart_controller.class.getSimpleName();
    private int status_code;
    ProgressDialog progressDialog = null;
    Covidenter_Frag covidenter_frag;
    int flag = 0;
    Covidpostdata covidpostdata;
    CovidEditActivity covidEditActivity;

    public Covidmultipart_controller(Covidenter_Frag covidenter_frag, Activity activity, Covidpostdata covidpostdata) {
        this.covidenter_frag = covidenter_frag;
        this.mActivity = activity;
        status_code = 0;
        flag = 1;
        this.covidpostdata = covidpostdata;
    }

    public Covidmultipart_controller(CovidEditActivity covidEditActivity, Activity activity, Covidpostdata covidpostdata) {
        this.covidEditActivity = covidEditActivity;
        this.mActivity = activity;
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
        String strUrl = Api.COVID + "pickso/api/DataSheetUpload/PatientDetails";
        Log.e(TAG, "COVID POST API " + strUrl);

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
            builder.addPart("MOBILE", new StringBody("" + covidpostdata.getMOBILE()));
            builder.addPart("NAME", new StringBody("" + covidpostdata.getNAME()));
            builder.addPart("AMOUNTCOLLECTED", new StringBody("" + covidpostdata.getAMOUNTCOLLECTED()));


            FileInputStream fileInputStream = new FileInputStream(covidpostdata.getPRESCRIPTION());
            builder.addPart("PRESCRIPTION", new InputStreamBody(fileInputStream, "image/jpeg", "file_name.jpg"));


            FileInputStream adharfilestream = new FileInputStream(covidpostdata.getADHAR());
            builder.addPart("ADHAR", new InputStreamBody(adharfilestream, "image/jpeg", "file_name.jpg"));

            if (covidpostdata.getADHAR1() != null) {
                FileInputStream adharfilestream1 = new FileInputStream(covidpostdata.getADHAR1());
                builder.addPart("ADHAR1", new InputStreamBody(adharfilestream1, "image/jpeg", "file_name.jpg"));
            }

            FileInputStream trffilestream = new FileInputStream(covidpostdata.getTRF());
            builder.addPart("TRF", new InputStreamBody(trffilestream, "image/jpeg", "file_name.jpg"));

            if (covidpostdata.getTRF1() != null) {
                FileInputStream trffilestream1 = new FileInputStream(covidpostdata.getTRF1());
                builder.addPart("TRF1", new InputStreamBody(trffilestream1, "image/jpeg", "file_name.jpg"));

            }

            FileInputStream vial_InputStream = new FileInputStream(covidpostdata.getVIAIMAGE());
            builder.addPart("VIALIMAGE", new InputStreamBody(vial_InputStream, "image/jpeg", "file_name.jpg"));

            if (covidpostdata.getOTHER()!=null){
                FileInputStream otherfilestream1 = new FileInputStream(covidpostdata.getOTHER());
                builder.addPart("OTHER", new InputStreamBody(otherfilestream1, "image/jpeg", "file_name.jpg"));

            }

            if (covidpostdata.getOTHER1()!=null){
                FileInputStream otherfilestream2 = new FileInputStream(covidpostdata.getOTHER1());
                builder.addPart("OTHER1", new InputStreamBody(otherfilestream2, "image/jpeg", "file_name.jpg"));

            }

            Log.e(TAG, "\"Post params:- " + "" + "\nUNIQUEID:" + covidpostdata.getUNIQUEID()  + "\nSOURCECODE:" + covidpostdata.getSOURCECODE() + "\nMOBILE:" + covidpostdata.getMOBILE() +"\nAMOUNTCOLLECTED:" + covidpostdata.getAMOUNTCOLLECTED()  +  "\nNAME:" + covidpostdata.getNAME() +
                    "\nPRESCRIPTION:" + covidpostdata.getPRESCRIPTION() + "\nADHAR:" + covidpostdata.getADHAR() + "\nADHAR1:" + covidpostdata.getADHAR1() + "\nTRF:" + covidpostdata.getTRF() + "\nTRF1:" + covidpostdata.getTRF1()
                    + "\nVIALIMAGE:" + covidpostdata.getVIAIMAGE()+ "\nOTHER:" + covidpostdata.getOTHER()+ "\nOTHER1:" + covidpostdata.getOTHER1());

            httpPost.setEntity(builder.build());
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            Log.e(TAG, "Status Line: " + httpResponse.getStatusLine());
            status_code = httpResponse.getStatusLine().getStatusCode();
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
                System.out.println("Response : " + result);
            }
        } catch (Exception e) {
            Log.e("InputStream", e.getLocalizedMessage());
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
                    covidenter_frag.getUploadResponse(response);
                } else {
                    covidEditActivity.getUploadResponse(response);
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
