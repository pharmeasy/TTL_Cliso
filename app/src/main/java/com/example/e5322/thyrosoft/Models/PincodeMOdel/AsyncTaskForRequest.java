package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.e5322.thyrosoft.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskForRequest {

    private AppPreferenceManager appPreferenceManager;
    private Context context;
    private AbstractApiModel abstractApiModel;
    private ApiCallAsyncTask apiCallAsyncTask;


    public AsyncTaskForRequest(Activity activity) {
        super();
        this.context = activity;
        appPreferenceManager = new AppPreferenceManager(activity);
    }

    public ApiCallAsyncTask getdocumentspinner(String APi_key, String Data, String Type) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getdocumentspinner: ");
            // abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.DOCUMENTSPINNER + "/" + APi_key + "/" + Data + "/" + Type);
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getdocumentspinner : "+AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.DOCUMENTSPINNER + "/" + APi_key + "/" + Data + "/" + Type);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources().getString(R.string.progress_message_SGC_DATA_please_wait));
            apiCallAsyncTask.setProgressBarCancellable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }


    public ApiCallAsyncTask getDefaultupload(String APi_key, String Type) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getDefaultupload: ");
            // abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.DEFAULTUPLOAD + "/" + APi_key + "/" + Type);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources().getString(R.string.progress_message_SGC_DATA_please_wait));
            apiCallAsyncTask.setProgressBarCancellable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getned(String APi_key, String Type, String Type2, String Type3) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getned: ");
            // abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GETNED + "/" + APi_key + "/" + Type + "/" + Type2 + "/" + Type3);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources().getString(R.string.progress_message_SGC_DATA_please_wait));
            apiCallAsyncTask.setProgressBarCancellable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getsgcspinnner(String APi_key, String Type, String Type2, String Type3) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getned: ");
            // abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GETSGC + "/" + APi_key + "/" + Type + "/" + Type2 + "/" + Type3);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources().getString(R.string.progress_message_SGC_DATA_please_wait));
            apiCallAsyncTask.setProgressBarCancellable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

    public ApiCallAsyncTask getpgcspinnner(String APi_key, String Type, String Type2, String Type3) {
        apiCallAsyncTask = null;
        try {
            apiCallAsyncTask = new ApiCallAsyncTask(context);
            abstractApiModel = new AbstractApiModel();
            Log.e(AsyncTaskForRequest.class.getSimpleName(), "getpgcspinnner: ");
            // abstractApiModel.setHeader(getHeader(AbstractApiModel.APPLICATION_JSON));
            abstractApiModel.setRequestUrl(AbstractApiModel.SERVER_BASE_API_URL + abstractApiModel.GETPGC + "/" + APi_key + "/" + Type + "/" + Type2 + "/" + Type3);
            apiCallAsyncTask.setHttpMethod((APICall.GET_METHOD));
            apiCallAsyncTask.setContentType(AbstractApiModel.APPLICATION_JSON);
            apiCallAsyncTask.setApiModel(abstractApiModel);
            apiCallAsyncTask.setProgressBarMessage(context.getResources().getString(R.string.progress_message_SGC_DATA_please_wait));
            apiCallAsyncTask.setProgressBarCancellable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiCallAsyncTask;
    }

}