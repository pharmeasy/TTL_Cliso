package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.ApiCallAsyncTaskDelegate;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.ApiCallAsyncTask;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AsyncTaskForRequest;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommonUtils;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DateUtils;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DefaultUploaddatamodel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DefaultuploadResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.NedResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.Neddatamodel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.PgcsResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.ResponseParser;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.UploadDocumentdatamodel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.pgcdatamodel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.sgcdatamodel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.sgcsResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.uploadspinnerResponseModel;
import com.example.e5322.thyrosoft.Models.RequestModels.UploadDocumentRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.UploadDocumentResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UploadDocument extends AbstractActivity {

    public static final String TAG_FRAGMENT = UploadDocument.class.getSimpleName();


    String APi_key;
    String type = "tsp";
    String Data = "data";
    String Data1 = "doc_list";
    String type1 = "tcc";
    String type2 = "ned";
    String type3 = "";
    String type6 = "sgc";
    ProgressDialog barProgressDialog;


    String type7 = "pgc";
    String type8 = "staff";
    Spinner document_spr, category_spr1, ned_spr, sgc_spr, pgc_spr;
    TableLayout upload_table;
    String type4 = "Tam03";
    TextView mis, mis2;
    Button btn_submit;
    String image = "";
    String imageName;
    ImageView edt_expiry1;
    EditText edt_purpose, edt_remarks;
    TextView edt_expiry, title, preview_img_txt;
    LinearLayout nedll, sgcll, pgcll, expiryll, ll_upload;
    ImageView gtick, back, home;
    String spincode;
    String fromdate1;
    String finalsetfromdate;
    ArrayList<String> nedspinnerdata;
    ArrayList<String> sgcspinnerdata;
    ArrayList<String> sgcspinnerdatagetName;
    ArrayList<String> Pgcspinnerdata;
    uploadspinnerResponseModel uploadspinnerResponseModels;
    ArrayList<UploadDocumentdatamodel> uploadDocumentdatamodelsarr;
    ArrayList<String> spinnerdata1;
    DefaultuploadResponseModel defaultuploadResponseModel;
    ArrayList<DefaultUploaddatamodel> defaultUploaddatamodelsarr;
    NedResponseModel nedResponseModel;
    ArrayList<Neddatamodel> neddatamodelsarr;
    sgcsResponseModel sgcsResponseModels;
    ArrayList<sgcdatamodel> sgcdatamodelsarr;
    PgcsResponseModel pgcsResponseModel;
    ArrayList<pgcdatamodel> pgcdatamodelsarr;
    private TableRow trm, th;
    private int mYear, mMonth, mDay;
    private Calendar fromDt;
    private SharedPreferences prefs;
    private String passwrd, api_key, user, mobile_pref, access, email_pref;
    private String passSpinnervalue;
    private String TAG = UploadDocument.class.getSimpleName();
    private String picturePath;
    private String selectedSpinItem;
    private String ned_value;
    private String document_spin_value;
    private String edt_remarksUpdate;
    private String exp_Date;
    private RequestQueue PostQueOtp;

    public static String ConvertBitmapToString(Bitmap bitmap) {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__document);

        nedll = (LinearLayout) findViewById(R.id.nedll);
        sgcll = (LinearLayout) findViewById(R.id.sgcll);
        pgcll = (LinearLayout) findViewById(R.id.pgcll);

        expiryll = (LinearLayout) findViewById(R.id.expiryll);
        ned_spr = (Spinner) findViewById(R.id.ned_spr);

        sgc_spr = (Spinner) findViewById(R.id.sgc_spr);
        pgc_spr = (Spinner) findViewById(R.id.pgc_spr);
        document_spr = (Spinner) findViewById(R.id.document_spr);
        category_spr1 = (Spinner) findViewById(R.id.category_spr1);

        ll_upload = (LinearLayout) findViewById(R.id.ll_upload);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_purpose = (EditText) findViewById(R.id.edt_purpose);
        edt_remarks = (EditText) findViewById(R.id.edt_remarks);
        edt_expiry = (TextView) findViewById(R.id.edt_expiry);
        edt_expiry1 = (ImageView) findViewById(R.id.edt_expiry1);
        title = (TextView) findViewById(R.id.title);
        preview_img_txt = (TextView) findViewById(R.id.preview_img_txt);
        gtick = (ImageView) findViewById(R.id.gtick);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(UploadDocument.this);
            }
        });

        gtick.setVisibility(View.GONE);
        preview_img_txt.setVisibility(View.GONE);

//        back = (ImageView) findViewById(R.id.back);
//        home = (ImageView) findViewById(R.id.home);

        if (Global.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        fromDt = Calendar.getInstance();

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);
        email_pref = prefs.getString("email", null);
        mobile_pref = prefs.getString("mobile_user", null);
        APi_key = api_key;

        title.setText("Upload Document");
      /*  back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(UploadDocument.this);
            }
        });*/


        //btn_submit.setBackground(getResources().getDrawable(R.drawable.btn_disable_border));

        edt_purpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8")
                        || enteredString.startsWith("9")) {
                    Toast.makeText(UploadDocument.this,
                            ToastFile.ent_purpose,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        edt_purpose.setText(enteredString.substring(1));
                       /*btn_submit.setBackground(getResources().getDrawable(R.drawable.next_button));
                        btn_submit.setEnabled(true);
                        btn_submit.setClickable(true);*/
                    } else {
                        edt_purpose.setText("");
                       /* btn_submit.setBackground(getResources().getDrawable(R.drawable.btn_disable_border));
                        btn_submit.setEnabled(false);
                        btn_submit.setClickable(false);*/
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8")
                        || enteredString.startsWith("9")) {
                    Toast.makeText(UploadDocument.this, ToastFile.remark, Toast.LENGTH_SHORT).show();

                    if (enteredString.length() > 0) {
                        edt_remarks.setText(enteredString.substring(1));
                    } else {
                        edt_remarks.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        finalsetfromdate = appPreferenceManager.getLeaveFromDate();

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = (Date) formatter.parse(finalsetfromdate);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            fromDt.setTime(date1);
            edt_expiry.setEnabled(false);
            edt_expiry.setClickable(false);
            fromdate1 = sdf.format(date1);
            edt_expiry.setText("" + fromdate1);
            //calNumDays(toDt.getTimeInMillis(), fromDt.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        edt_expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UploadDocument.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //  edt_expiry.setText(DateUtils.Req_Date_Req(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, "dd-MM-yyyy", "dd-MM-yyyy"));
                                edt_expiry.setText(DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "yyyy-MM-dd"));

                                fromDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);//back date Disabled
                datePickerDialog.show();

            }
        });

        edt_expiry1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UploadDocument.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edt_expiry.setText(DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "yyyy-MM-dd"));

                                fromDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);//back date Disabled
                datePickerDialog.show();

            }
        });


        fetchnedspinner();
        fetchsgcspinner();
        fetchpgcspinner();

        Defaultupload();

        List<String> spinnerArray2 = new ArrayList<String>();
        spinnerArray2.add("NED");
        spinnerArray2.add("SGC");
        spinnerArray2.add("PGC");

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                UploadDocument.this, R.layout.upload_document_spin, spinnerArray2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spr1.setAdapter(adapter2);

        category_spr1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (category_spr1.getSelectedItem().equals("NED")) {
                    nedll.setVisibility(View.VISIBLE);
                    sgcll.setVisibility(View.GONE);
                    pgcll.setVisibility(View.GONE);

                    expiryll.setVisibility(View.VISIBLE);
                    passSpinnervalue = category_spr1.getSelectedItem().toString();
                    fetchtDocumentpinner();

                } else if (category_spr1.getSelectedItem().equals("SGC")) {
                    nedll.setVisibility(View.GONE);
                    sgcll.setVisibility(View.VISIBLE);
                    pgcll.setVisibility(View.GONE);
                    passSpinnervalue = category_spr1.getSelectedItem().toString();
                    fetchtDocumentpinner();
                    expiryll.setVisibility(View.GONE);
                } else if (category_spr1.getSelectedItem().equals("PGC")) {
                    nedll.setVisibility(View.GONE);
                    sgcll.setVisibility(View.GONE);
                    pgcll.setVisibility(View.VISIBLE);
                    passSpinnervalue = category_spr1.getSelectedItem().toString();
                    fetchtDocumentpinner();
                    expiryll.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ll_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, 0);

            }
        });

        preview_img_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File imgFile = new File(picturePath);

                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    LayoutInflater li = LayoutInflater.from(UploadDocument.this);
                    View promptsView = li.inflate(R.layout.custom_dialog_imageviewer, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            UploadDocument.this);
                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);
                    final ImageView userInput = (ImageView) promptsView
                            .findViewById(R.id.img_show);
                    // set dialog message
                    // crete alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                    userInput.setImageBitmap(myBitmap);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                selectedSpinItem = category_spr1.getSelectedItem().toString();
                document_spin_value = document_spr.getSelectedItem().toString();
                edt_remarksUpdate = edt_remarks.getText().toString();

                if (selectedSpinItem != null && document_spin_value != null && edt_remarksUpdate != null) {
                    if (selectedSpinItem.equalsIgnoreCase("NED")) {
                        if (ned_spr.getSelectedItem().toString().length() > 0)
                            ned_value = ned_spr.getSelectedItem().toString();
                        exp_Date = edt_expiry.getText().toString();

                        String[] parts = ned_value.split("-");
                        spincode = parts[0];

                        if (ned_value.equalsIgnoreCase("-Select-")) {
                            TastyToast.makeText(UploadDocument.this, "Please select NED", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (document_spin_value.equalsIgnoreCase("-Select-")) {
                            TastyToast.makeText(UploadDocument.this, "Please select document type", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (imageName == null) {
                            TastyToast.makeText(UploadDocument.this, "Please upload document", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (TextUtils.isEmpty(edt_remarksUpdate)) {
                            TastyToast.makeText(UploadDocument.this, "Please enter purpose", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (exp_Date.equalsIgnoreCase("")) {
                            TastyToast.makeText(UploadDocument.this, "Please select expiry date", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            uploadDocument();
                        }
                    } else if (selectedSpinItem.equalsIgnoreCase("SGC")) {
                        ned_value = sgc_spr.getSelectedItem().toString();
                        exp_Date = "";

                        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(ned_value);
                        while (m.find()) {
                            spincode = m.group(1);
                        }

                        if (ned_value.equalsIgnoreCase("-Select-")) {
                            TastyToast.makeText(UploadDocument.this, "Please select SGC", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (document_spin_value.equalsIgnoreCase("-Select-")) {
                            TastyToast.makeText(UploadDocument.this, "Please document type", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (imageName == null) {
                            TastyToast.makeText(UploadDocument.this, "Please upload document", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (edt_remarksUpdate.equalsIgnoreCase("")) {
                            TastyToast.makeText(UploadDocument.this, "Please enter purpose", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            uploadDocument();
                        }
                    } else if (selectedSpinItem.equalsIgnoreCase("PGC")) {
                        ned_value = pgc_spr.getSelectedItem().toString();
                        exp_Date = "";

                        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(ned_value);
                        while (m.find()) {
                            spincode = m.group(1);
                        }

                        if (ned_value.equalsIgnoreCase("-Select-")) {
                            TastyToast.makeText(UploadDocument.this, "Please select PGC", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (document_spin_value.equalsIgnoreCase("-Select-")) {
                            TastyToast.makeText(UploadDocument.this, "Please document type", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (imageName == null) {
                            TastyToast.makeText(UploadDocument.this, "Please upload document", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else if (edt_remarksUpdate.equalsIgnoreCase("")) {
                            TastyToast.makeText(UploadDocument.this, "Please enter purpose", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        } else {
                            uploadDocument();
                        }
                    }
                }
            }
        });

    }

    private void uploadDocument() {
        barProgressDialog = new ProgressDialog(UploadDocument.this, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        barProgressDialog.show();

        PostQueOtp = Volley.newRequestQueue(UploadDocument.this);
        JSONObject jsonObject = null;

        try {
            UploadDocumentRequestModel requestModel = new UploadDocumentRequestModel();
            requestModel.setApikey(api_key);
            requestModel.setCode(spincode);
            requestModel.setLetter_type(document_spin_value);
            requestModel.setPurpose(edt_remarksUpdate);
            requestModel.setAttached_file(imageName);
            requestModel.setRemarks(edt_remarksUpdate);
            requestModel.setENTERED_BY(user);
            requestModel.setIsB2B("y");
            requestModel.setExpiryDate(exp_Date);
            requestModel.setAttached_data(image);

            Gson gson = new Gson();
            String json = gson.toJson(requestModel);
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(TAG + jsonObject);
        System.out.println(TAG + Api.uploadDocument);

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.uploadDocument, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }

                try {
                    Log.e(TAG, "onResponse: " + response);

                    UploadDocumentResponseModel responseModel = new Gson().fromJson(String.valueOf(response), UploadDocumentResponseModel.class);

                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {
                            TastyToast.makeText(UploadDocument.this, "Document uploaded successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            Intent i = new Intent(UploadDocument.this, UploadDocument.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Toast.makeText(UploadDocument.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                    barProgressDialog.dismiss();
                }
                if (error != null) {
                } else {
                    System.out.println(error);
                }
            }
        });

        GlobalClass.volleyRetryPolicy(jsonObjectRequest1);
        PostQueOtp.add(jsonObjectRequest1);
        Log.e(TAG, "SendFeedbackToAPI: url" + jsonObjectRequest1);
        Log.e(TAG, "SendFeedbackToAPI: json" + jsonObject);
    }

    private void fetchpgcspinner() {

        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
        ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getpgcspinnner(api_key, Data, type7, user);
        fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchpgcspinnerdataApiAsyncTaskDelegateResult());

        if (isNetworkAvailable(UploadDocument.this)) {
            fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
        } else {
            CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
        }
    }

    private void fetchsgcspinner() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
        ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getsgcspinnner(api_key, Data, type6, user);
        fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchsgcspinnerdataApiAsyncTaskDelegateResult());

        if (isNetworkAvailable(UploadDocument.this)) {
            fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
        } else {
            CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
        }
    }

    private void fetchnedspinner() {

        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
        ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getned(api_key, Data, type2, user);
        fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchnedspinnerdataApiAsyncTaskDelegateResult());

        if (isNetworkAvailable(UploadDocument.this)) {
            fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);

        } else {
            CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
        }
    }

    private void fetchtDocumentpinner() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
        ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getdocumentspinner(APi_key, Data1, passSpinnervalue);
        fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchdocumentspinnerdataApiAsyncTaskDelegateResult());

        if (isNetworkAvailable(UploadDocument.this)) {
            fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
        } else {
            CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
        }
    }

    //method to convert the selected image to base64 encoded string

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = UploadDocument.this.getContentResolver().query(targetUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();

                imageName = picturePath.substring(picturePath.lastIndexOf("/") + 1, picturePath.length());

                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(UploadDocument.this.getContentResolver().openInputStream(targetUri));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    image = ConvertBitmapToString(resizedBitmap);
                    gtick.setVisibility(View.VISIBLE);
                    preview_img_txt.setVisibility(View.VISIBLE);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private void Defaultupload() {
        AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
        ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getDefaultupload(APi_key, type4);
        fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchdefaultdataApiAsyncTaskDelegateResult());

        if (isNetworkAvailable(UploadDocument.this)) {
            fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
        } else {
            CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
        }
    }

    private class FetchnedspinnerdataApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(UploadDocument.this);


                neddatamodelsarr = new ArrayList<>();
                nedspinnerdata = new ArrayList<>();
                nedResponseModel = responseParser.getNedResponseModel(json, statusCode);
                /*if (nedResponseModel.getResponse().equalsIgnoreCase("")) {
                }*/
                if (nedResponseModel.getNedlist() != null) {
                    neddatamodelsarr = nedResponseModel.getNedlist();
                    nedspinnerdata.add("-Select-");
                    for (int i = 0; i < neddatamodelsarr.size(); i++) {
                        nedspinnerdata.add(neddatamodelsarr.get(i).getNAME());
                    }
                } else
                    nedspinnerdata.add("-Select-");

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                        UploadDocument.this, R.layout.upload_document_spin, nedspinnerdata);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ned_spr.setAdapter(adapter3);
            }
        }

        @Override
        public void onApiCancelled() {


            CommonUtils.toastytastyError(UploadDocument.this, "" + R.string.network_error, false);
        }
    }

    private class FetchpgcspinnerdataApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(UploadDocument.this);


                pgcdatamodelsarr = new ArrayList<>();
                Pgcspinnerdata = new ArrayList<>();
                pgcsResponseModel = responseParser.getPgcsResponseModel(json, statusCode);


                if (pgcsResponseModel != null) {
                    pgcdatamodelsarr = pgcsResponseModel.getPgclist();
                    Pgcspinnerdata.add("-Select-");
                    for (int i = 0; i < pgcdatamodelsarr.size(); i++) {
                        Pgcspinnerdata.add(pgcdatamodelsarr.get(i).getNAME());
                    }
                    ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(
                            UploadDocument.this, R.layout.upload_document_spin, Pgcspinnerdata);
                    adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pgc_spr.setAdapter(adapter6);
                }
            }
        }

        @Override
        public void onApiCancelled() {


            CommonUtils.toastytastyError(UploadDocument.this, "" + R.string.network_error, false);
        }
    }


    private class FetchsgcspinnerdataApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(UploadDocument.this);
                sgcdatamodelsarr = new ArrayList<>();
                sgcspinnerdata = new ArrayList<>();
                sgcspinnerdatagetName = new ArrayList<>();
                sgcsResponseModels = responseParser.getsgcsResponseModel(json, statusCode);


                if (sgcsResponseModels != null) {
                    sgcdatamodelsarr = sgcsResponseModels.getSgclist();
                    sgcspinnerdata.add("-Select-");
                    for (int i = 0; i < sgcdatamodelsarr.size(); i++) {
                        sgcspinnerdata.add(sgcdatamodelsarr.get(i).getNAME());
                        sgcspinnerdatagetName.add(sgcdatamodelsarr.get(i).getSGC());
                    }
                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(
                            UploadDocument.this, R.layout.upload_document_spin, sgcspinnerdata);
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sgc_spr.setAdapter(adapter4);

                }
            }
        }

        @Override
        public void onApiCancelled() {

            CommonUtils.toastytastyError(UploadDocument.this, "" + R.string.network_error, false);
        }
    }

    private class FetchdocumentspinnerdataApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(UploadDocument.this);

                uploadDocumentdatamodelsarr = new ArrayList<>();
                spinnerdata1 = new ArrayList<>();
//                String getRepsonse=json.
                uploadspinnerResponseModels = responseParser.getuploadspinnerResponseModel(json, statusCode);
                uploadDocumentdatamodelsarr = uploadspinnerResponseModels.getDoclist();

                if (uploadDocumentdatamodelsarr != null) {
                    spinnerdata1.add("-Select-");
                    for (int i = 0; i < uploadDocumentdatamodelsarr.size(); i++) {
                        spinnerdata1.add(uploadDocumentdatamodelsarr.get(i).getDocument());
                    }

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                            UploadDocument.this, R.layout.upload_document_spin, spinnerdata1);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    document_spr.setAdapter(adapter2);
                }
            }
        }

        @Override
        public void onApiCancelled() {


            CommonUtils.toastytastyError(UploadDocument.this, "" + R.string.network_error, false);
        }
    }

    private class FetchdefaultdataApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) throws JSONException {

            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(UploadDocument.this);


                defaultUploaddatamodelsarr = new ArrayList<>();
                defaultuploadResponseModel = responseParser.getDefaultuploadResponseModel(json, statusCode);


                if (defaultuploadResponseModel != null) {
                    defaultUploaddatamodelsarr = defaultuploadResponseModel.getUploadDocumentList();


                }
            }
        }

        @Override
        public void onApiCancelled() {
            CommonUtils.toastytastyError(UploadDocument.this, "" + R.string.network_error, false);
        }
    }
}

