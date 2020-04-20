package com.example.e5322.thyrosoft.Activity;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class MyHurlStack  extends HurlStack {

    private Context mContext;

    public MyHurlStack(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {

        Map<String, String> headers = new HashMap<>(additionalHeaders);
        headers.put(Constants.HEADER_USER_AGENT, Constants.APPNAME + "/" + GlobalClass.getversion(mContext) + "(" + GlobalClass.getversioncode(mContext) + ")" + GlobalClass.getSerialnum(mContext));

        System.out.println(headers);
        try {
            return super.performRequest(request, headers);
        } catch (IOException e) {

        } catch (AuthFailureError authFailureError) {
        }


        return null;
    }
}
