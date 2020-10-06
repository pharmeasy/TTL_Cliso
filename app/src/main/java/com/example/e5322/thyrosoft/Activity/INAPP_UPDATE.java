package com.example.e5322.thyrosoft.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.example.e5322.thyrosoft.Controller.Log;
import android.widget.Toast;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.VersionResponseModel;
import com.example.e5322.thyrosoft.startscreen.Login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

public class INAPP_UPDATE implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int MEGABYTE = 1024 * 1024;
    Context context;
    String TAG = getClass().getSimpleName();
    ProgressDialog barProgressDialog;
    File apkFile;
    String USER_CODE = "";
    String newToken, storetoken;
    int SCRID = 0;
    boolean isFromNotification;
    private String ApkUrl, version;
    private int versionCode = 0;

    public INAPP_UPDATE(Context context, int SCRID, boolean isFromNotification) {
        this.context = context;
        this.SCRID = SCRID;
        this.isFromNotification = isFromNotification;
        new Versioncheck(context).execute(Api.checkVersion);
    }

    public void allowStoragePermission() {
        //Check if permission is granted or not
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            GlobalClass.requestStoragePermission(context);
        else
            new DownloadFile(context).execute(ApkUrl, Constants.APK_NAME);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GlobalClass.WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new DownloadFile(context).execute(ApkUrl, Constants.APK_NAME);
                }
                break;
        }
    }

    public class Versioncheck extends AsyncTask<String, String, String> {

        String version1;
        Context context;

        public Versioncheck(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if (buffer != null) {
                    String finalJson = buffer.toString();
                    /*JSONObject parentObject = new JSONObject(finalJson);
                    version1 = parentObject.getString("Version");
                    ApkUrl = parentObject.getString("url");*/
                    Gson gson = new Gson();
                    VersionResponseModel versionResponseModel = gson.fromJson(finalJson, VersionResponseModel.class);

                    version1 = versionResponseModel.getVersion();
                    ApkUrl = versionResponseModel.getUrl();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {

                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {
            super.onPostExecute(result);

            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            version = pInfo.versionName;
            versionCode = pInfo.versionCode;

            try {
                if (version1 != null && version != null) {

                    int intApiVersion = 0;//api
                    int intPkgversion = 0;//pkg
                    try {
                        String newVersion = version1.replace(".", "");//api
                        String preversion = version.replace(".", "");//package

                        intApiVersion = Integer.parseInt(newVersion);
                        intPkgversion = Integer.parseInt(preversion);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    if (intPkgversion >= intApiVersion) {
                        Log.e(TAG, "intPkgversion -->" + intPkgversion + " intApiVersion--->" + intApiVersion);
                        SharedPreferences prefs = context.getSharedPreferences("Userdetails", MODE_PRIVATE);
                        final String user = prefs.getString("Username", null);
                        final String passwrd = prefs.getString("password", null);
                        String access = prefs.getString("ACCESS_TYPE", null);
                        String api_key = prefs.getString("API_KEY", null);
                        USER_CODE = prefs.getString("USER_CODE", "");

                        SharedPreferences fire_pref = context.getSharedPreferences(Constants.SH_FIRE, MODE_PRIVATE);
                        storetoken = fire_pref.getString(Constants.F_TOKEN, "");

                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                newToken = instanceIdResult.getToken();
                                Log.e("NewToken----->", newToken);
                                if (user != null && passwrd != null) {
                                    Intent prefe = new Intent(context, ManagingTabsActivity.class);
                                    prefe.putExtra("Screen_category", SCRID);
                                    prefe.putExtra(Constants.IsFromNotification,isFromNotification);
                                    context.startActivity(prefe);
                                    ((Activity) context).finish();
                                } else {
                                    Intent i = new Intent(context, Login.class);
                                    context.startActivity(i);
                                    ((Activity) context).finish();
                                }
                            }
                        });
                    } else {
                        //oh yeah we do need an upgrade, let the user know send an alert message
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(false);
                        builder.setMessage("There is newer version of this application available, click OK to upgrade now?")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    //if the user agrees to upgrade
                                    public void onClick(DialogInterface dialog, int id) {
                                        allowStoragePermission();
                                    }

                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ((Activity) context).finish();
                                    }
                                });
                        //show the alert message
                        builder.create().show();

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        Context context;

        public DownloadFile(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = new ProgressDialog(context);
            barProgressDialog.setTitle("Downloading file");
            barProgressDialog.setCancelable(false);
            barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            barProgressDialog.setMax(100);
            barProgressDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {

            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            apkFile = new File(folder, fileName);

            try {
                apkFile.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();

            }

            try {
                int count;
                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = null;
                fileOutputStream = new FileOutputStream(apkFile);
                int totalSize = urlConnection.getContentLength();

                byte[] buffer = new byte[MEGABYTE];
                long bufferLength = 0;
                while ((count = inputStream.read(buffer)) != -1) {
                    bufferLength += count;
                    publishProgress((int) (bufferLength * 100 / totalSize));
                    fileOutputStream.write(buffer, 0, count);
                }

                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        private void publishProgress(int i) {
            barProgressDialog.setProgress(i);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            barProgressDialog.dismiss();

            if (apkFile.exists()) {
                Uri uri_path;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    uri_path = FileProvider.getUriForFile(context, context.getPackageName() + ".imageprovider", apkFile);
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setDataAndType(uri_path, Constants.APKTYPE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
                } else {
                    uri_path = Uri.fromFile(apkFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + Constants.APK_NAME)), Constants.APKTYPE);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            } else {
                Toast.makeText(context, "The file not exists! ", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
