package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.UploadDoc_Controller;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
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
    String Data = "data";
    String Data1 = "doc_list";
    String type2 = "ned";
    String type6 = "sgc";
    ProgressDialog barProgressDialog;
    String type7 = "pgc";
    Spinner document_spr, category_spr1, ned_spr, sgc_spr, pgc_spr;
    String type4 = "Tam03";
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
    private int mYear, mMonth, mDay;
    Activity mActivity;
    private Calendar fromDt;
    private SharedPreferences prefs;
    private String api_key, user;
    private String passSpinnervalue;
    private String TAG = UploadDocument.class.getSimpleName();
    private String picturePath;
    private String selectedSpinItem;
    private String ned_value;
    private String document_spin_value;
    private String exp_Date;
    private RequestQueue PostQueOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__document);

        initui();
        initlistner();
        fetchnedspinner();
        fetchsgcspinner();
        fetchpgcspinner();
        Defaultupload();
    }


    public void getUploadDocResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);

            UploadDocumentResponseModel responseModel = new Gson().fromJson(String.valueOf(response), UploadDocumentResponseModel.class);
            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {

                    GlobalClass.showTastyToast(mActivity, MessageConstants.DOC_SUCC, 1);
                    Intent i = new Intent(UploadDocument.this, UploadDocument.class);
                    startActivity(i);
                    finish();
                } else {
                    GlobalClass.showTastyToast(mActivity, ToastFile.IMAGEERROR, 2);
                }
            } else {
                GlobalClass.showTastyToast(mActivity, ToastFile.something_went_wrong, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initlistner() {

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

        edt_purpose.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8")
                        || enteredString.startsWith("9")) {
                    Toast.makeText(UploadDocument.this,
                            ToastFile.ent_purpose,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        edt_purpose.setText(enteredString.substring(1));
                    } else {
                        edt_purpose.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edt_expiry.setOnClickListener(new View.OnClickListener() {
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
                                edt_expiry.setText(DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "dd-MM-yyyy"));
                                exp_Date = DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "yyyy-MM-dd");
                                fromDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
                                edt_expiry.setText(DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "dd-MM-yyyy"));
                                exp_Date = DateUtils.Req_Date_Req(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, "yyyy-MM-dd", "yyyy-MM-dd");
                                fromDt.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);//back date Disabled
                datePickerDialog.show();
            }
        });

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
//                GlobalClass.cropImageFullScreenActivity(UploadDocument.this, 2);

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
                    alertDialogBuilder.setView(promptsView);
                    final ImageView userInput = (ImageView) promptsView
                            .findViewById(R.id.img_show);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    userInput.setImageBitmap(myBitmap);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!GlobalClass.isNull(category_spr1.getSelectedItem().toString())) {
                    selectedSpinItem = category_spr1.getSelectedItem().toString();
                }
                if (!GlobalClass.isNull(document_spr.getSelectedItem().toString())) {
                    document_spin_value = document_spr.getSelectedItem().toString();
                }
                if (selectedSpinItem != null && document_spin_value != null) {
                    try {
                        if (selectedSpinItem.equalsIgnoreCase("NED")) {
                            if (ned_spr.getSelectedItem().toString().length() > 0)
                                ned_value = ned_spr.getSelectedItem().toString();
                            String[] parts = ned_value.split("-");
                            spincode = parts[0];

                            if (ned_value.equalsIgnoreCase("-Select-")) {
                                TastyToast.makeText(UploadDocument.this, "Please select NED", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            } else if (document_spin_value.equalsIgnoreCase("-Select-")) {
                                TastyToast.makeText(UploadDocument.this, "Please select document type", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            } else if (imageName == null) {
                                TastyToast.makeText(UploadDocument.this, "Please upload document", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            } else if (GlobalClass.isNull(edt_remarks.getText().toString())) {
                                TastyToast.makeText(UploadDocument.this, "Please enter purpose", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            } else if (exp_Date.equalsIgnoreCase("")) {
                                TastyToast.makeText(UploadDocument.this, "Please select expiry date", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            } else {
                                uploadDocument();
                            }
                        } else if (!GlobalClass.isNull(selectedSpinItem) && selectedSpinItem.equalsIgnoreCase("SGC")) {
                            try {
                                ned_value = sgc_spr.getSelectedItem().toString();
                                exp_Date = "";
                                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(ned_value);
                                while (m.find()) {
                                    spincode = m.group(1);
                                }
                                if(Validate()){
                                    uploadDocument();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (selectedSpinItem.equalsIgnoreCase("PGC")) {
                            try {
                                ned_value = pgc_spr.getSelectedItem().toString();
                                exp_Date = "";
                                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(ned_value);
                                while (m.find()) {
                                    spincode = m.group(1);
                                }
                                if(Validate()){
                                    uploadDocument();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private boolean Validate() {
        if (ned_value.equalsIgnoreCase("-Select-")) {
            TastyToast.makeText(UploadDocument.this, "Please select PGC", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return false;
        } else if (document_spin_value.equalsIgnoreCase("-Select-")) {
            TastyToast.makeText(UploadDocument.this, "Please document type", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return false;
        } else if (imageName == null) {
            TastyToast.makeText(UploadDocument.this, "Please upload document", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return false;
        } else if (edt_remarks.getText().toString().equalsIgnoreCase("")) {
            TastyToast.makeText(UploadDocument.this, "Please enter purpose", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return false;
        }

        return true;
    }

    @SuppressLint("NewApi")
    private void initui() {
        mActivity = this;
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
        gtick.setVisibility(View.GONE);
        preview_img_txt.setVisibility(View.GONE);

        GlobalClass.setStatusBarcolor(mActivity);

        fromDt = Calendar.getInstance();
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        api_key = prefs.getString("API_KEY", null);
        APi_key = api_key;
        title.setText("YOUR CLIENT/FLEBO KYC UPLOAD");

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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> spinnerArray2 = new ArrayList<String>();
        spinnerArray2.add("NED");
        spinnerArray2.add("SGC");
        spinnerArray2.add("PGC");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                UploadDocument.this, R.layout.upload_document_spin, spinnerArray2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spr1.setAdapter(adapter2);
    }

    private void uploadDocument() {

        PostQueOtp = GlobalClass.setVolleyReq(UploadDocument.this);
        JSONObject jsonObject = null;

        try {
            UploadDocumentRequestModel requestModel = new UploadDocumentRequestModel();
            requestModel.setApikey(api_key);
            requestModel.setCode(spincode);
            requestModel.setLetter_type(document_spin_value);
            requestModel.setPurpose(edt_remarks.getText().toString().trim());
            requestModel.setAttached_file(imageName);
            requestModel.setRemarks(edt_remarks.getText().toString().trim());
            requestModel.setENTERED_BY(user);
            requestModel.setIsB2B("y");
            requestModel.setExpiryDate(exp_Date);
            requestModel.setAttached_data(image);
            Gson gson = new Gson();
            String json = gson.toJson(requestModel);
            jsonObject = new JSONObject(json);

            if (ControllersGlobalInitialiser.uploadDoc_controller != null) {
                ControllersGlobalInitialiser.uploadDoc_controller = null;
            }
            ControllersGlobalInitialiser.uploadDoc_controller = new UploadDoc_Controller(mActivity, UploadDocument.this);
            ControllersGlobalInitialiser.uploadDoc_controller.UploadDocController(jsonObject, PostQueOtp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchpgcspinner() {

        try {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
            ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getpgcspinnner(api_key, Data, type7, user);
            fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchpgcspinnerdataApiAsyncTaskDelegateResult());
            if (isNetworkAvailable(UploadDocument.this)) {
                fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
            } else {
                CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchsgcspinner() {
        try {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
            ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getsgcspinnner(api_key, Data, type6, user);
            fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchsgcspinnerdataApiAsyncTaskDelegateResult());
            if (isNetworkAvailable(UploadDocument.this)) {
                fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
            } else {
                CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchnedspinner() {

        try {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
            ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getned(api_key, Data, type2, user);
            fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchnedspinnerdataApiAsyncTaskDelegateResult());
            if (isNetworkAvailable(UploadDocument.this)) {
                fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
            } else {
                CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchtDocumentpinner() {
        try {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
            ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getdocumentspinner(APi_key, Data1, passSpinnervalue);
            fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchdocumentspinnerdataApiAsyncTaskDelegateResult());

            if (isNetworkAvailable(UploadDocument.this)) {
                fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
            } else {
                CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Defaultupload() {
        try {
            AsyncTaskForRequest asyncTaskForRequest = new AsyncTaskForRequest(UploadDocument.this);
            ApiCallAsyncTask fetchSgcDetailApiAsyncTask = asyncTaskForRequest.getDefaultupload(APi_key, type4);
            fetchSgcDetailApiAsyncTask.setApiCallAsyncTaskDelegate(new FetchdefaultdataApiAsyncTaskDelegateResult());

            if (isNetworkAvailable(UploadDocument.this)) {
                fetchSgcDetailApiAsyncTask.execute(fetchSgcDetailApiAsyncTask);
            } else {
                CommonUtils.toastytastyError(UploadDocument.this, getString(R.string.internet_connetion_error), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
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

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imageInByte = byteArrayOutputStream.toByteArray();
                    long lengthbmp = imageInByte.length;
                    String imglength = readableFileSize(lengthbmp);
                    if (imglength.contains("MB")) {
                        image = GlobalClass.ConvertBitmapToString(resizedBitmap);
                    } else {
                        image = GlobalClass.getBase64Image(resizedBitmap);
                    }
                    gtick.setVisibility(View.VISIBLE);
                    preview_img_txt.setVisibility(View.VISIBLE);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        String result = null;
        result = new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        return result;
    }


    private class FetchnedspinnerdataApiAsyncTaskDelegateResult implements ApiCallAsyncTaskDelegate {

        @Override
        public void apiCallResult(String json, int statusCode) {

            if (statusCode == 200) {
                ResponseParser responseParser = new ResponseParser(UploadDocument.this);
                neddatamodelsarr = new ArrayList<>();
                nedspinnerdata = new ArrayList<>();
                nedResponseModel = responseParser.getNedResponseModel(json, statusCode);
                if (nedResponseModel.getNedlist() != null) {
                    neddatamodelsarr = nedResponseModel.getNedlist();
                    nedspinnerdata.add("-Select-");
                    for (int i = 0; i < neddatamodelsarr.size(); i++) {
                        nedspinnerdata.add(neddatamodelsarr.get(i).getNAME().toUpperCase());
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
                        Pgcspinnerdata.add(pgcdatamodelsarr.get(i).getNAME().toUpperCase());
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
                        sgcspinnerdatagetName.add(sgcdatamodelsarr.get(i).getSGC().toUpperCase());
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
                uploadspinnerResponseModels = responseParser.getuploadspinnerResponseModel(json, statusCode);
                uploadDocumentdatamodelsarr = uploadspinnerResponseModels.getDoclist();

                if (uploadDocumentdatamodelsarr != null) {
                    spinnerdata1.add("-Select-");
                    for (int i = 0; i < uploadDocumentdatamodelsarr.size(); i++) {
                        spinnerdata1.add(uploadDocumentdatamodelsarr.get(i).getDocument().toUpperCase());
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

