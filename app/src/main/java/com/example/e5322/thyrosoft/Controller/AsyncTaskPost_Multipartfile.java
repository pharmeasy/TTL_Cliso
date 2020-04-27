package com.example.e5322.thyrosoft.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.example.e5322.thyrosoft.Controller.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.frags.BS_EntryFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BS_POSTDataModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsyncTaskPost_Multipartfile extends AsyncTask<Void, Void, String> {


    BS_EntryFragment bs_entryFragment;
    BS_POSTDataModel BSPostDataModel;
    private int flag;
    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    private String TAG = AsyncTaskPost_Multipartfile.class.getSimpleName();
    private int status_code;
    ProgressDialog progressDialog = null;

    public AsyncTaskPost_Multipartfile(BS_EntryFragment bsEntryFragment, Activity activity, BS_POSTDataModel BSPostDataModel) {
        this.bs_entryFragment = bsEntryFragment;
        this.mActivity = activity;
        status_code = 0;
        flag = 0;
        this.BSPostDataModel = BSPostDataModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = GlobalClass.ShowprogressDialog(mActivity);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String strUrl = Api.SUGARSO + Api.MULTIPART_UPLOAD;
        Log.e(TAG, "BS_EntryAPI " + strUrl);

        InputStream inputStream = null;
        String result = "";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strUrl);

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addPart("Name", new StringBody("" + BSPostDataModel.getName()));
            builder.addPart("Age", new StringBody("" + BSPostDataModel.getAge()));
            builder.addPart("MOBILE", new StringBody("" + BSPostDataModel.getMobile()));
            builder.addPart("landline", new StringBody("" + BSPostDataModel.getLandLine()));
            builder.addPart("Gender", new StringBody("" + BSPostDataModel.getGender()));
            builder.addPart("Test", new StringBody("" + BSPostDataModel.getTest()));
            builder.addPart("Value", new StringBody("" + BSPostDataModel.getValue()));
            builder.addPart("Range", new StringBody("" + BSPostDataModel.getRange()));
            builder.addPart("EmailId", new StringBody("" + BSPostDataModel.getEmail_id()));
            builder.addPart("Usercode", new StringBody("" + BSPostDataModel.getUser()));
            builder.addPart("Latitude", new StringBody("" + BSPostDataModel.getLatitude()));
            builder.addPart("Longitude", new StringBody("" + BSPostDataModel.getLongitude()));
            builder.addPart("AmountCollected", new StringBody("" + BSPostDataModel.getCollAmount()));
            builder.addPart("Pincode", new StringBody("" + BSPostDataModel.getPincode()));
            builder.addPart("SBP", new StringBody("" + BSPostDataModel.getSBP()));
            builder.addPart("DBP", new StringBody("" + BSPostDataModel.getDBP()));

            builder.addBinaryBody("file", BSPostDataModel.getFile());

            Log.e(TAG, "\"Post params:- " + "" + "\nName:" + BSPostDataModel.getName() + "\nAge:" + BSPostDataModel.getAge() + "\nMOBILE:" + BSPostDataModel.getMobile()
                    + "\nlandline:" + BSPostDataModel.getLandLine() + "\nGender:" + BSPostDataModel.getGender() + "\nTest:" + BSPostDataModel.getTest() + "\nValue:" + BSPostDataModel.getValue()
                    + "\nRange:" + BSPostDataModel.getRange() + "\nEmailId:" + BSPostDataModel.getEmail_id() + "\nUsercode:" + BSPostDataModel.getUser()
                    + "\nLatitude:" + BSPostDataModel.getLatitude() + "\nLongitude:" + BSPostDataModel.getLongitude() + "\nAmountCollected:" + BSPostDataModel.getCollAmount()
                    + "\nSBP:" + BSPostDataModel.getSBP() + "\nDBP:" + BSPostDataModel.getDBP()
                    + "\nPincode:" + BSPostDataModel.getPincode() + "\nfile:" + BSPostDataModel.getFile());

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
        try {
            GlobalClass.hideProgress(mActivity, progressDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (status_code == 200) {
            if (response != null && !response.isEmpty()) {
                bs_entryFragment.getUploadResponse(response);
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
