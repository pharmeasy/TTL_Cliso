package com.example.e5322.thyrosoft.Controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.e5322.thyrosoft.HHHtest.HHHEnteredFrag;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by e5233@thyrocare.com on 12/9/18.
 */

public class fileFromBitmap extends AsyncTask<Void, Integer, String> {


    Context context;
    File file;
    Bitmap bitmap;
    String path_external = "";
    HHHEnteredFrag mHHHEnteredFrag;

    int flagF = 0;



    public fileFromBitmap(HHHEnteredFrag pfragment, Bitmap mbitmap, Activity mActivity, String s) {
        this.mHHHEnteredFrag = pfragment;
        this.bitmap = mbitmap;
        this.context = mActivity;
        path_external = Environment.getExternalStorageDirectory() + File.separator + s;

        flagF = 1;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // before executing doInBackground
        // update your UI
        // exp; make progressbar visible
    }

    @Override
    protected String doInBackground(Void... params) {

        ByteArrayOutputStream bytes = null;
        try {
            bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        file = new File(path_external);
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if (flagF == 1) {
                mHHHEnteredFrag.getFileFromBitmap(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
