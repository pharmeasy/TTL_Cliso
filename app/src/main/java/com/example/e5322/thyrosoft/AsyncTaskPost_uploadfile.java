package com.example.e5322.thyrosoft;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Fragment.Offline_woe;
import com.example.e5322.thyrosoft.Models.TRFModel;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Scan_Barcode_ILS_New;
import com.example.e5322.thyrosoft.WOE.Scan_Barcode_Outlabs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AsyncTaskPost_uploadfile extends AsyncTask<Void, Void, String> {

    ProgressDialog barProgressDialog;
    private int flag;
    @SuppressLint("StaticFieldLeak")
    private Scan_Barcode_Outlabs scan_barcode_outlabs;
    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    private String apiKey, sourceCode, patientID;
    private ArrayList<TRFModel> trfModelArrayList;

    private String TAG = AsyncTaskPost_uploadfile.class.getSimpleName();
    private int status_code;
    @SuppressLint("StaticFieldLeak")
    private Scan_Barcode_ILS_New scan_barcode_ils_new;
    private Offline_woe offline_woe;
    Context context;

    public AsyncTaskPost_uploadfile(Scan_Barcode_Outlabs scan_barcode_outlabs, Activity mActivity, String apiKey, String sourceCode, String patientID, ArrayList<TRFModel> trfModelArrayList) {
        this.scan_barcode_outlabs = scan_barcode_outlabs;
        this.mActivity = mActivity;
        this.apiKey = apiKey;
        this.sourceCode = sourceCode;
        this.patientID = patientID;
        this.trfModelArrayList = trfModelArrayList;
        status_code = 0;
        flag = 0;
    }

    public AsyncTaskPost_uploadfile(Scan_Barcode_ILS_New scan_barcode_ils_new, Activity mActivity, String apiKey, String sourceCode, String patientID, ArrayList<TRFModel> trfModelArrayList) {
        this.scan_barcode_ils_new = scan_barcode_ils_new;
        this.mActivity = mActivity;
        this.apiKey = apiKey;
        this.sourceCode = sourceCode;
        this.patientID = patientID;
        this.trfModelArrayList = trfModelArrayList;
        status_code = 0;
        flag = 1;
    }


    public AsyncTaskPost_uploadfile(Offline_woe offline_woe, Activity mActivity, String apiKey, String sourceCode, String patientID, ArrayList<TRFModel> trfModelArrayList) {
        this.offline_woe = offline_woe;
        this.mActivity = mActivity;
        this.apiKey = apiKey;
        this.sourceCode = sourceCode;
        this.patientID = patientID;
        this.trfModelArrayList = trfModelArrayList;
        status_code = 0;
        flag = 2;
    }

   /* public AsyncTaskPost_Multipartfile(Context context, String apiKey, String sourceCode, String patientID, ArrayList<TRFModel> trfModelArrayList) {
        this.context=context;
        this.apiKey = apiKey;
        this.sourceCode = sourceCode;
        this.patientID = patientID;
        this.trfModelArrayList = trfModelArrayList;
        status_code = 0;
        flag = 3;
    }*/

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        barProgressDialog = new ProgressDialog(mActivity);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        barProgressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String strUrl = Api.UPLOAD_TRF_RECEIPT;
        System.out.println(strUrl);

        InputStream inputStream = null;
        String result = "";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strUrl);

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addPart("KEY", new StringBody("" + apiKey));
            builder.addPart("SOURCECODE", new StringBody("" + sourceCode));
            builder.addPart("PATIENTID", new StringBody("" + patientID));
            builder.addPart("TYPE", new StringBody("DATA Reciept"));
            builder.addPart("MODE", new StringBody("THYROSOFTLITE APP"));


            for (int i = 0; i < trfModelArrayList.size(); i++) {
                String product;
                File trfImageFile;
                if (trfModelArrayList.get(i).getTrf_image() != null && trfModelArrayList.get(i).getTrf_image().exists()) {
                    product = trfModelArrayList.get(i).getProduct();
                    trfImageFile = trfModelArrayList.get(i).getTrf_image();
                    builder.addBinaryBody(product, trfImageFile);
                    Log.e(TAG, "TRF :-" + product + " " + trfImageFile);
                }

            }

            Log.e(TAG, "Post params:- " + "KEY" + ":" + apiKey + "\n" + "SOURCECODE" + ":" + sourceCode + "\n" + "PATIENTID" + ":" + patientID + "\n" + "TYPE:DATA Reciept" + "\n" + "MODE:THYROSOFTLITE APP" + "\n"
                    + "\"TRF_UPLOAD_SIZE\"" + ":\"" + trfModelArrayList.size() + "\"");

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
            Log.d("InputStream", e.getLocalizedMessage());
            result = "Something went wrong";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (barProgressDialog != null && barProgressDialog.isShowing())
            barProgressDialog.dismiss();
        if (status_code == 200) {
            if (response != null && !response.isEmpty()) {
                Log.e(TAG, "ON Response: " + response);
                response = response.replaceAll("^\"|\"$", "");
                if (response.equalsIgnoreCase("File Uploaded successfully")) {

                    if (flag == 0) {
                        scan_barcode_outlabs.getUploadFileResponse();
                    }

                    if (flag == 1)
                        scan_barcode_ils_new.getUploadFileResponse();

                    if (flag ==2){
                        offline_woe=new Offline_woe();
                    }

                } else {
                    Toast.makeText(mActivity, response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(mActivity, ToastFile.IMAGE_UPLOAD_FAILED, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mActivity, "Result is null", Toast.LENGTH_SHORT).show();
                Toast.makeText(mActivity, ToastFile.IMAGE_UPLOAD_FAILED, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mActivity, ToastFile.IMAGE_UPLOAD_FAILED, Toast.LENGTH_LONG).show();
        }
    }

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
