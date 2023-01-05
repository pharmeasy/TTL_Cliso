package com.example.e5322.thyrosoft.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewCertificate extends AppCompatActivity {

    WebView fullscreen_content;
    private String newString;
    Button btn_download;
    Bitmap myBitmap;
    LinearLayout ll_web;
    private static File f = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_certificate);

        btn_download=findViewById(R.id.btn_download);
        ll_web=findViewById(R.id.ll_web);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("certificate");
        }

        fullscreen_content = findViewById(R.id.fullscreen_content);
        fullscreen_content.getSettings().setJavaScriptEnabled(true);
        fullscreen_content.getSettings().setLoadWithOverviewMode(true);
        fullscreen_content.getSettings().setUseWideViewPort(true);
//        fullscreen_content.getSettings().setBuiltInZoomControls(true);
//        fullscreen_content.getSettings().setDisplayZoomControls(false);
        fullscreen_content.loadUrl("" + newString);


        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    myBitmap = captureScreen(ll_web);
                    if (myBitmap != null) {
                        saveImage(myBitmap);
                    }


                    Toast.makeText(ViewCertificate.this, "File Downloaded Successfully", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }


    public void saveImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);
            f = new File(Environment.getExternalStorageDirectory() + File.separator +  "Image.png");
            if (!f.exists())
                f.createNewFile();

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();

            AlertDialog.Builder builder = new AlertDialog.Builder(ViewCertificate.this);
            // Set the Alert Dialog Message
            builder.setMessage(ToastFile.OpenImageInGallery);
            builder.setCancelable(false);
            builder.setPositiveButton("Open",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".imageprovider", f), "image/*");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Bitmap captureScreen(View v) {
        Bitmap screenshot = null;
        try {
            if (v != null) {
                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }
        } catch (Exception e) {
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }
        return screenshot;
    }



}