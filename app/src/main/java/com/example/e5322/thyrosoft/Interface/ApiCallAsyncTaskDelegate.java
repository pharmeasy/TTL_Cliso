package com.example.e5322.thyrosoft.Interface;

import org.json.JSONException;

/**
 * Created by Orion on 16/3/15.
 */
public interface ApiCallAsyncTaskDelegate {
	public void apiCallResult(String json, int statusCode) throws JSONException;
	public void onApiCancelled();
}