package com.example.e5322.thyrosoft.HHHtest;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.ConnectionDetector;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Activity.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.fileFromBitmap;
import com.example.e5322.thyrosoft.Fragment.RATEnteredFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.HHHtest.Controller.GetPatientDetails;
import com.example.e5322.thyrosoft.HHHtest.Controller.GetTests;
import com.example.e5322.thyrosoft.HHHtest.Model.GetTestResponseModel;
import com.example.e5322.thyrosoft.HHHtest.Model.PatientDetailRequestModel;
import com.example.e5322.thyrosoft.HHHtest.Model.PatientResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.mindorks.paracamera.Camera;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HHHEnteredFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HHHEnteredFrag extends Fragment {

    View root;
    RadioButton rd_pending, rd_done, rd_expired;
    RecyclerView rc_view;
    RadioGroup rg_radio;
    Calendar myCalendar;
    ConnectionDetector cd;
    Activity activity;
    TextView tv_norecords;
    private String TAG = RATEnteredFrag.class.getSimpleName();
    SharedPreferences preferences;
    String usercode, apikey;
    LinearLayout ll_fromDate, ll_date;
    private String myFormat = "dd-MM-yyyy";
    TextView tv_fromDate;
    Spinner spr_test;
    List<String> testlist = new ArrayList<>();
    Date fromdate;
    private Camera camera;
    ArrayList<PatientResponseModel.PatientDetailsBean> patientdata = new ArrayList<>();
    GetTestResponseModel getTestResponseModel;
    private SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    private String putDate, showDate, from_formateDate;
    PatientAdapter patientAdapter;
    AppPreferenceManager appPreferenceManager;
    File sll_file;
    private int flag = 0;
    private int status_code;
    private String patientid;
    private String testcode = "";
    private String value = "";
    ProgressDialog progress;
    private String Value = "";
    static HHHEnteredFrag fragment;
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };


    public HHHEnteredFrag() {
        // Required empty public constructor
    }

    public static HHHEnteredFrag newInstance() {
        fragment = new HHHEnteredFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_h_h_h_entered, container, false);
        activity = getActivity();
        fragment = this;
        initui(root);
        CallGetTests();
        initlistner();

        return root;
    }

    private void CallGetTests() {

        try {
            final HashSet<String> hashSet1 = new HashSet<String>();

            getTestResponseModel = appPreferenceManager.getTestResponseModel();
            if (getTestResponseModel.getPoctHHHData() != null) {
                for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                    if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                        for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                            testlist.add(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTestName().trim());
                            hashSet1.addAll(testlist);
                            testlist.clear();
                            testlist.addAll(hashSet1);
                        }
                    }
                }
                ArrayAdapter<String> adap = new ArrayAdapter<String>(getContext(), R.layout.name_age_spinner, testlist);
                adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spr_test.setAdapter(adap);
                spr_test.setSelection(0);
            }


        } catch (Exception e) {
            GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
            e.printStackTrace();
        }
        /*if (cd.isConnectingToInternet()) {
            GetTests getTests = new GetTests(HHHEnteredFrag.this);
            getTests.GetTests();
        } else {
            Toast.makeText(activity, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
        }*/
    }


    private void initlistner() {

        rd_pending.setChecked(true);
        if (rd_pending.isChecked() || rd_pending.isSelected()) {
            CallAPI("0");
            ll_date.setVisibility(View.GONE);
        }
        rg_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rd_pending.isChecked() || rd_pending.isSelected()) {
                    CallAPI("0");
                    ll_date.setVisibility(View.GONE);
                } else if (rd_done.isChecked() || rd_done.isSelected()) {
                    CallAPI("1");
                    ll_date.setVisibility(View.VISIBLE);
                } else if (rd_expired.isChecked() || rd_expired.isSelected()) {
                    CallAPI("2");
                    ll_date.setVisibility(View.VISIBLE);
                }
            }
        });

        ll_fromDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }

        });


        spr_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (rd_pending.isChecked() || rd_pending.isSelected()) {
                    CallAPI("0");
                    ll_date.setVisibility(View.GONE);
                } else if (rd_done.isChecked() || rd_done.isSelected()) {
                    CallAPI("1");
                    ll_date.setVisibility(View.VISIBLE);
                } else if (rd_expired.isChecked() || rd_expired.isSelected()) {
                    CallAPI("2");
                    ll_date.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initui(View root) {
        cd = new ConnectionDetector(getContext());
        appPreferenceManager = new AppPreferenceManager(activity);
        preferences = activity.getSharedPreferences("Userdetails", Context.MODE_PRIVATE);
        usercode = preferences.getString("USER_CODE", "");
        apikey = preferences.getString("API_KEY", "");

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATEFORMATE);
        Date today = new Date();
        myCalendar = Calendar.getInstance();
        myCalendar.setTime(today);
        showDate = sdf.format(today);

        fromdate = returndate(fromdate, showDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, showDate);

        rd_pending = root.findViewById(R.id.rd_pending);
        rd_done = root.findViewById(R.id.rd_done);
        rd_expired = root.findViewById(R.id.rd_expired);
        spr_test = root.findViewById(R.id.spr_test);
        rc_view = root.findViewById(R.id.rc_view);
        tv_norecords = root.findViewById(R.id.tv_norecords);
        ll_fromDate = root.findViewById(R.id.ll_fromDate);
        tv_fromDate = root.findViewById(R.id.tv_fromDate);
        ll_date = root.findViewById(R.id.ll_date);
        rg_radio = root.findViewById(R.id.rg_radio);
        tv_fromDate.setText(showDate);

    }


    private void updateLabel() {
        putDate = sdf.format(myCalendar.getTime());
        tv_fromDate.setText(putDate);
        from_formateDate = GlobalClass.formatDate(Constants.DATEFORMATE, Constants.YEARFORMATE, putDate);
        if (GlobalClass.isNetworkAvailable((Activity) getContext())) {
            if (cd.isConnectingToInternet()) {

                PatientDetailRequestModel patientDetailRequestModel = new PatientDetailRequestModel();
                patientDetailRequestModel.setApikey(apikey);
                patientDetailRequestModel.setSourceCode(usercode);
                if (rd_done.isChecked()) {
                    patientDetailRequestModel.setStatus(1);
                } else if (rd_expired.isChecked()) {
                    patientDetailRequestModel.setStatus(2);
                }
                if (getTestResponseModel != null) {
                    for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                        if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                            for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                                if (spr_test.getSelectedItem().toString().equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTestName())) {
                                    patientDetailRequestModel.setTest(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTest());
                                }
                            }
                        }
                    }
                }

                patientDetailRequestModel.setDate(from_formateDate);

                GetPatientDetails getPatientDetails = new GetPatientDetails(this);
                getPatientDetails.CallAPI(patientDetailRequestModel);

            } else {
                GlobalClass.toastyError(getContext(), MessageConstants.CHECK_INTERNET_CONN, false);
            }
        } else {
            GlobalClass.toastySuccess(getContext(), ToastFile.intConnection, false);
        }

    }

    private Date returndate(Date date, String putDate) {
        try {
            date = sdf.parse(putDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    private void CallAPI(String status) {
        PatientDetailRequestModel patientDetailRequestModel = new PatientDetailRequestModel();
        patientDetailRequestModel.setApikey(apikey);
        patientDetailRequestModel.setSourceCode(usercode);

        patientDetailRequestModel.setStatus(Integer.parseInt(status));
        if (getTestResponseModel != null) {
            for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                    for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                        if (spr_test.getSelectedItem().toString().equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTestName())) {
                            patientDetailRequestModel.setTest(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTest());
                            testcode = getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTest();
                        }
                    }
                }
            }
        }
        if (rd_pending.isChecked() || rd_pending.isSelected()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateandTime = sdf.format(new Date());
            patientDetailRequestModel.setDate(currentDateandTime);
        } else {
            patientDetailRequestModel.setDate(from_formateDate);
        }
        GetPatientDetails getPatientDetails = new GetPatientDetails(this);
        getPatientDetails.CallAPI(patientDetailRequestModel);

    }

    public void getresponse(GetTestResponseModel body) {
        try {
            final HashSet<String> hashSet1 = new HashSet<String>();
            if (!GlobalClass.isNull(body.getResId()) && body.getResId().equalsIgnoreCase("RES0000")) {
                getTestResponseModel = body;
                if (getTestResponseModel.getPoctHHHData() != null) {
                    for (int i = 0; i < getTestResponseModel.getPoctHHHData().size(); i++) {
                        if (Global.HHHTest.equalsIgnoreCase(getTestResponseModel.getPoctHHHData().get(i).getDisplay())) {
                            for (int j = 0; j < getTestResponseModel.getPoctHHHData().get(i).getTestDetails().size(); j++) {
                                testlist.add(getTestResponseModel.getPoctHHHData().get(i).getTestDetails().get(j).getTestName().trim());
                                hashSet1.addAll(testlist);
                                testlist.clear();
                                testlist.addAll(hashSet1);
                            }
                        }
                    }
                    ArrayAdapter<String> adap = new ArrayAdapter<String>(getContext(), R.layout.name_age_spinner, testlist);
                    adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spr_test.setAdapter(adap);
                    spr_test.setSelection(0);
                }
            } else {
                Toast.makeText(activity, "" + body.getResponse(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            GlobalClass.ShowError(activity, "Server Error", "Kindly try after some time.", false);
            e.printStackTrace();
        }
    }

    public void getPatientresponse(PatientResponseModel body) {
        try {
            if (!GlobalClass.isNull(body.getResid()) && body.getResid().equalsIgnoreCase("RES0000")) {
                rc_view.setVisibility(View.VISIBLE);
                tv_norecords.setVisibility(View.GONE);
                if (body.getPatientDetails() != null) {
                    patientdata = body.getPatientDetails();
                    patientAdapter = new PatientAdapter(this, patientdata);
                    patientAdapter.Click(new PatientAdapter.Passdata() {


                        @Override
                        public void pass(PatientResponseModel.PatientDetailsBean postmaterial, String val) {
                            patientid = postmaterial.getId();
                            value = val;
                            if (val.contains("RESULT")) {
                                flag = 2;
                            } else {
                                flag = 1;
                            }
                            openCamera();
                        }
                    });
                    rc_view.setAdapter(patientAdapter);
                    patientAdapter.notifyDataSetChanged();
                }
            } else {
                rc_view.setVisibility(View.GONE);
                tv_norecords.setVisibility(View.VISIBLE);
                Toast.makeText(activity, "" + body.getResponse(), Toast.LENGTH_SHORT).show();
                rc_view.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(false)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("img" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_PNG)
                .setCompression(Constants.setcompression)
                .setImageHeight(Constants.setheight)
                .build(this);
    }

    private void openCamera() {
        buildCamera();

        try {
            camera.takePicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = camera.getCameraBitmap();
                if (flag == 1) {
                    String imageurl = camera.getCameraBitmapPath();
                    sll_file = new File(imageurl);
                    new AsyncTaskPost_uploadfile().execute();
                } else if (flag == 2) {
                    if (bitmap != null) {
                        onCaptureImageResult(bitmap);
                    }
//                    showImageDialog(sll_file);
                } else {
                    String imageurl = camera.getCameraBitmapPath();
                    sll_file = new File(imageurl);
                    new AsyncTaskPost_uploadfile().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void onCaptureImageResult(Bitmap mbitmap) {
        try {
            mbitmap = GlobalClass.RotateImage(mbitmap);
            new fileFromBitmap(fragment, mbitmap, activity, "hhh_mg.jpg").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFileFromBitmap(File file) {
        sll_file = file;
        showImageDialog(sll_file);
    }



    public void showImageDialog(File file) {
        final Dialog dialog = new Dialog(activity);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.result_upload_dialog_hhh);
        dialog.setCancelable(true);

        ImageView imgView = (ImageView) dialog.findViewById(R.id.imageview);
        ImageView img_close = (ImageView) dialog.findViewById(R.id.img_close);
        RadioGroup rd_grp = (RadioGroup) dialog.findViewById(R.id.rd_grp);
        final RadioButton rd_positive = (RadioButton) dialog.findViewById(R.id.rd_positive);
        final RadioButton rd_negative = (RadioButton) dialog.findViewById(R.id.rd_negative);
        final RadioButton rd_invalid = (RadioButton) dialog.findViewById(R.id.rd_invalid);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);

        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if (myBitmap != null)
            imgView.setImageBitmap(myBitmap);
        else
            Global.showCustomToast(activity, "Image not found");

        rd_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rd_positive.isChecked()) {
                    Value = "+";
                } else if (rd_negative.isChecked()) {
                    Value = "-";
                } else if (rd_invalid.isChecked()) {
                    Value = "*";
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rd_negative.isChecked() && !rd_positive.isChecked() && !rd_invalid.isChecked()) {
                    Toast.makeText(activity, "Select the result status", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    new AsyncTaskPost_uploadfile().execute();

                }

            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(width, height);
        // dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    public class AsyncTaskPost_uploadfile extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = GlobalClass.progress(activity, false);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String strUrl = Api.IMAGE_UPLOAD + "PICKSO/api/PoctHhh/TRFUpload";
            Log.v("URL", strUrl);

            InputStream inputStream = null;
            String result = "";

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(strUrl);
                httpPost.setEntity(builder.build());

                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart("KEY", new StringBody("" + apikey));
                builder.addPart("SOURCECODE", new StringBody("" + usercode));
                builder.addPart("PATIENTID", new StringBody("" + patientid));
                builder.addPart("TYPE", new StringBody("" + value));
                builder.addPart("TESTCODE", new StringBody("" + testcode));

                builder.addPart("VALUE", new StringBody("" + Value));
                builder.addPart("MODE", new StringBody("CLISO APP"));
                if (sll_file != null) {
                    builder.addBinaryBody("SII", sll_file);
                }


                Log.e(TAG, "Post params:- "
                        + "KEY" + ":" + apikey + "\n"
                        + "SOURCECODE" + ":" + usercode + "\n"
                        + "PATIENTID" + ":" + patientid + "\n"
                        + "TYPE :" + value + "\n"
                        + "VALUE :" + Value + "\n"
                        + "TESTCODE :" + testcode + "\n"
                        + "MODE:CLISO APP" + "\n"
                        + "SII" + ":\"" + "" + sll_file + "\"");

                httpPost.setEntity(builder.build());
                httpPost.setHeader(Constants.HEADER_USER_AGENT, GlobalClass.getHeaderValue(activity));
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();
                Log.e(TAG, "Status Line: " + httpResponse.getStatusLine());

                status_code = httpResponse.getStatusLine().getStatusCode();
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.v(TAG, "Response : " + result);

                }
            } catch (Exception e) {
                Log.e("InputStream", e.getLocalizedMessage());
                result = "Something went wrong";

            }
            return result;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            GlobalClass.hideProgress(activity, progress);
            if (status_code == 200) {
                if (response != null && !response.isEmpty()) {
                    Log.e(TAG, "ON Response: " + response);
                    response = response.replaceAll("^\"|\"$", "");

                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("ResId").equalsIgnoreCase("RES0000")) {
                            Toast.makeText(activity, "" + obj.getString("Response"), Toast.LENGTH_SHORT).show();
                            CallAPI("0");
                            obj.getString("currentTIME");


                        } else {
                            Toast.makeText(activity, "" + obj.getString("Response"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(activity, "Result is null", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        public String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;
            inputStream.close();
            return result;
        }

    }
}