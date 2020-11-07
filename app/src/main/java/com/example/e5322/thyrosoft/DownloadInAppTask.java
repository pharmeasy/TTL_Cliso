package com.example.e5322.thyrosoft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;

import androidx.core.content.FileProvider;

import com.example.e5322.thyrosoft.startscreen.SplashScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

public class DownloadInAppTask extends AsyncTask<String, Integer, String> {

    private String TAG = DownloadInAppTask.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private SplashScreen mActivity;
    private String appnameAPK;
    private Context context;
    int fileLength;
    private PowerManager.WakeLock mWakeLock;
    private GlobalClass globalClass;

    long downloadedsize, filesize;

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        CallInstallApp();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    mActivity.finish();
                    break;
            }
        }
    };

    public DownloadInAppTask(SplashScreen pactivity, Context pcontext, ProgressDialog pProgressDialog, String appNAMEAPK) {
        this.mActivity = pactivity;
        this.context = pcontext;
        this.mProgressDialog = pProgressDialog;
        this.appnameAPK = appNAMEAPK;

    }

    @Override
    protected String doInBackground(String... sUrl) {

        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }
//            connection.setRequestProperty(Constants.HEADER_USER_AGENT, GlobalClass.getHeaderValue(context));//todo facing issue while update
            // this will be useful to display download percentage
            // might be -1: server did not report the length
            fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream("/sdcard/" + appnameAPK);

            byte data[] = new byte[4096];
            int count;
            downloadedsize = ByteBuffer.wrap(data).getLong();

            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }

                downloadedsize += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (100 * downloadedsize / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgress(progress[0]);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressNumberFormat((GlobalClass.bytes2long(downloadedsize)) + "/" + (GlobalClass.bytes2long(fileLength)));

    }

    private void setProgressMessage(ProgressDialog ProgressDialog, Integer progress) {
        try {
            if (progress % 4 == 0) {
                ProgressDialog.setMessage("Downloading .");
            } else if (progress % 4 == 1) {
                ProgressDialog.setMessage("Downloading . .");
            } else if (progress % 4 == 2) {
                ProgressDialog.setMessage("Downloading . . .");
            } else if (progress % 4 == 3) {
                ProgressDialog.setMessage("Downloading . . . .");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(String result) {
        try {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null) {
                GlobalClass.showShortToast(mActivity, "Download error: " + result);
                globalClass.printLog("Error", TAG, "onPostExecute: ", "" + result);
            } else {
                CallAlertDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CallAlertDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage("Once installation is finished, You are requested to manually Restart the app if not started automatically.\nClick Install to Update")
                    .setCancelable(false)
                    .setPositiveButton("Install", dialogClickListener)
                    .setNegativeButton("Cancel", dialogClickListener).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallInstallApp() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + appnameAPK);
            if (file.exists()) {
                Uri path;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    path = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".imageprovider", file);
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setDataAndType(path, "application/vnd.android.package-archive");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    mActivity.startActivity(intent);
                } else {
                    path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(intent);
                }
            } else {
                globalClass.printLog("sout", TAG, "", "<< Not Present >>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}