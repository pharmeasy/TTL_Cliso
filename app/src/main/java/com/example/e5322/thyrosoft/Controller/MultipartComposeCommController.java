package com.example.e5322.thyrosoft.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.ComposeCommunication_activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ComposeCommPOSTModel;

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

public class MultipartComposeCommController extends AsyncTask<Void, Void, String> {

    private ProgressDialog progressDialog = null;
    private String TAG = MultipartComposeCommController.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    @SuppressLint("StaticFieldLeak")
    private ComposeCommunication_activity composeCommunicationActivity;
    private int status_code;
    private ComposeCommPOSTModel composeCommPOSTModel;

    public MultipartComposeCommController(Activity Activity, ComposeCommunication_activity composeCommunicationActivity, ComposeCommPOSTModel composeCommPOSTModel) {
        this.mActivity = Activity;
        this.composeCommunicationActivity = composeCommunicationActivity;
        status_code = 0;
        this.composeCommPOSTModel = composeCommPOSTModel;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = GlobalClass.ShowprogressDialog(mActivity);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String strUrl = Api.POST_COMM_MULTIPART;
        System.out.println(strUrl);

        InputStream inputStream = null;
        String result = "";

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strUrl);

            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addPart("apiKey", new StringBody("" + composeCommPOSTModel.getApiKey()));
            builder.addPart("userCode", new StringBody("" + composeCommPOSTModel.getUserCode()));
            builder.addPart("type", new StringBody("" + composeCommPOSTModel.getType()));
            builder.addPart("communication", new StringBody("" + composeCommPOSTModel.getCommunication()));
            builder.addPart("forwardTo", new StringBody("" + composeCommPOSTModel.getForwardTo()));
            builder.addPart("Source", new StringBody("" + composeCommPOSTModel.getSource()));

            if (composeCommPOSTModel.getFile() != null && composeCommPOSTModel.getFile().exists()) {
                builder.addBinaryBody("file", composeCommPOSTModel.getFile());
            }

            Log.e(TAG, "Post params:- " + "\napiKey:" + composeCommPOSTModel.getApiKey() + "\nuserCode:" + composeCommPOSTModel.getUserCode()
                    + "\ntype:" + composeCommPOSTModel.getType() + "\ncommunication:" + composeCommPOSTModel.getCommunication() + "\nforwardTo:" + composeCommPOSTModel.getForwardTo()
                    + "\nSource:" + composeCommPOSTModel.getSource() + "\nfile:" + composeCommPOSTModel.getFile());

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
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (status_code == 200) {
            if (response != null && !response.isEmpty()) {
                composeCommunicationActivity.getPOSTCommunicationResponse(response);
            } else {
                Toast.makeText(mActivity, response, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mActivity, "Failed", Toast.LENGTH_LONG).show();
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
