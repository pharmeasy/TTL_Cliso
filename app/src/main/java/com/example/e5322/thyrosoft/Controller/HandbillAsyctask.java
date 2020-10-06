package com.example.e5322.thyrosoft.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.Previewhandbill_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.HandbillRequest;

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

public class HandbillAsyctask extends AsyncTask<Void, Void, String> {

    Previewhandbill_Activity previewhandbill_activity;
    private int flag;
    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    private String TAG = AsyncTaskPost_Multipartfile.class.getSimpleName();
    private int status_code;
    ProgressDialog progressDialog = null;
    HandbillRequest handbillRequest;
    private Global globalClass;

    public HandbillAsyctask(Previewhandbill_Activity previewhandbill_activity, Activity activity, HandbillRequest handbillRequest) {
        this.previewhandbill_activity = previewhandbill_activity;
        this.mActivity = activity;
        status_code = 0;
        flag = 0;
        this.handbillRequest = handbillRequest;
        globalClass = new Global(mActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = GlobalClass.ShowprogressDialog(mActivity);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String strUrl = Api.SGC + "/Persuasion/Uploadhandbill";
        android.util.Log.e(TAG, "PREVIEW IMAGE UPLOAD " + strUrl);
        InputStream inputStream = null;
        String result = "";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strUrl);

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addPart("Name", new StringBody("" + handbillRequest.getName().trim()));
            builder.addPart("Address", new StringBody("" + handbillRequest.getAddress().trim()));
            builder.addPart("Pincode", new StringBody("" + handbillRequest.getPincode().trim()));
            builder.addPart("Mobile", new StringBody("" + handbillRequest.getMobile().trim()));
            builder.addPart("Email", new StringBody("" + handbillRequest.getEmail().trim()));
            builder.addPart("Enteredby", new StringBody("" + handbillRequest.getEnteredby().trim()));
            builder.addPart("Action", new StringBody("" + handbillRequest.getAction()));
            builder.addPart("ImgStatus", new StringBody("" + handbillRequest.getImgStatus()));
            builder.addPart("Imgid", new StringBody("" + handbillRequest.getImgid()));

            builder.addBinaryBody("file", handbillRequest.getFiles());

            android.util.Log.e(TAG, "\"Post params:- " + "" + "\nName:" + handbillRequest.getName() + "\nAddress:" + handbillRequest.getAddress() + "\nPincode:" + handbillRequest.getPincode()
                    + "\nMobile:" + handbillRequest.getMobile() + "\nEmail:" + handbillRequest.getEmail() + "\nEnteredby:" + handbillRequest.getEnteredby() + "\nAction:" + handbillRequest.getAction()
                    + "\nImgStatus:" + handbillRequest.getImgStatus() + "\nImgid:" + handbillRequest.getImgid() + "\nfile:" + handbillRequest.getFiles());

            httpPost.setEntity(builder.build());
            httpPost.setHeader(Constants.HEADER_USER_AGENT,  GlobalClass.getHeaderValue(mActivity));
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            android.util.Log.e(TAG, "Status Line: " + httpResponse.getStatusLine());
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
        GlobalClass.hideProgress(mActivity, progressDialog);

        if (status_code == 200) {
            previewhandbill_activity.getUploadResponse(response);
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

