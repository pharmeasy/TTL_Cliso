package com.example.e5322.thyrosoft.RevisedScreenNewUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.RequestModels.ClientRegisterRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.ClientRegisterResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.e5322.thyrosoft.API.Constants.small_invalidApikey;
import static com.example.e5322.thyrosoft.ToastFile.ent_pin;

public class Sgc_Pgc_Entry_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    public static InputFilter EMOJI_FILTER = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };

    ImageView correct_img, cross_img;
    Spinner sgc_pgc_spinner, brand_spinner, qualification_spinner;
    LinearLayout brand_name_layout, pincode_layout, mobile_linear_layout, email_linear_layout, client_name_layout, dr_name_layout, brand_name, incharbe_layout, addres_layout_data, qualification_layout, phone_number_layout, website_layout, visiting_card_layout;
    EditText pincode_edt, mobile_edt, email_edt, client_name_edt, dr_name, incharge_name, phone_number, website_edt, dr_name_edt, addressEdt;
    Button next_btn_sgc_pgc, visiting_card_btn;
    TextView lattitude_txt, longitude_txt;
    ProgressDialog barProgressDialog;
    String blockCharacterSetdata = "~#^|$%&*!+:`-/|><";
    ImageView back, home;
    LinearLayout latlong;
    private String encodedImage;

    private InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSetdata.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private String get_Registration_type;
    private String getpincode, getmobile_number, email_address, getclient_name, get_brand_name, get_incharge_name, get_Address, get_phone_number, get_website, get_visiting_card, get_dr_name, get_qualification;
    private String imageName;
    private String image;
    private RequestQueue PostQueSGC;
    private RequestQueue PostQueCheckEmail;
    private RequestQueue PostQuePGC;
    private SharedPreferences prefs;
    private String user;
    private String passwrd;
    private String access;
    private String api_key;
    private String TAG = Sgc_Pgc_Entry_Activity.class.getSimpleName().toString();
    private String finalJson;
    private String entered_mobile_String;
    private String entered_Type_String;
    private String docStatus;
    private String country, pincode, city;
    private GoogleApiClient mGoogleApiClient;
    private String getLatitude;
    private String getLongitude;
    private double lat, log;

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
        setContentView(R.layout.activity_sgc__pgc__entry_);

        correct_img = (ImageView) findViewById(R.id.correct_img);
        cross_img = (ImageView) findViewById(R.id.cross_img);
        next_btn_sgc_pgc = (Button) findViewById(R.id.next_btn_sgc_pgc);
        visiting_card_btn = (Button) findViewById(R.id.visiting_card_btn);
        pincode_edt = (EditText) findViewById(R.id.pincode_edt);
        mobile_edt = (EditText) findViewById(R.id.mobile_edt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        dr_name_edt = (EditText) findViewById(R.id.dr_name_edt);
        client_name_edt = (EditText) findViewById(R.id.client_name_edt);
        incharge_name = (EditText) findViewById(R.id.incharge_name);
        lattitude_txt = (TextView) findViewById(R.id.lattitude_txt);
        longitude_txt = (TextView) findViewById(R.id.longitude_txt);

        phone_number = (EditText) findViewById(R.id.phone_number);
        website_edt = (EditText) findViewById(R.id.website_edt);
        addressEdt = (EditText) findViewById(R.id.address);
        sgc_pgc_spinner = (Spinner) findViewById(R.id.sgc_pgc_spinner);
        brand_spinner = (Spinner) findViewById(R.id.brand_spinner);
        qualification_spinner = (Spinner) findViewById(R.id.qualification_spinner);
        dr_name_layout = (LinearLayout) findViewById(R.id.dr_name_layout);
        brand_name = (LinearLayout) findViewById(R.id.brand_name);
        visiting_card_layout = (LinearLayout) findViewById(R.id.visiting_card_layout);
        back = (ImageView) findViewById(R.id.back);
        home = (ImageView) findViewById(R.id.home);
        latlong = (LinearLayout) findViewById(R.id.latlong);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.goToHome(Sgc_Pgc_Entry_Activity.this);
            }
        });

        if (GlobalClass.checkForApi21()) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.limaroon));
        }

        correct_img.setVisibility(View.GONE);
        cross_img.setVisibility(View.GONE);
        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", null);
        passwrd = prefs.getString("password", null);
        access = prefs.getString("ACCESS_TYPE", null);
        api_key = prefs.getString("API_KEY", null);


        ArrayAdapter sgc_pgc_spinner_adapter = ArrayAdapter.createFromResource(Sgc_Pgc_Entry_Activity.this, R.array.sgc_pgc_entry, R.layout.whitetext_spin);
        sgc_pgc_spinner_adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        sgc_pgc_spinner.setAdapter(sgc_pgc_spinner_adapter);

        ArrayAdapter qualification_adapter = ArrayAdapter.createFromResource(Sgc_Pgc_Entry_Activity.this, R.array.dr_qualification, R.layout.whitetext_spin);
        qualification_adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        qualification_spinner.setAdapter(qualification_adapter);

        ArrayAdapter sgc_brand_adapter = ArrayAdapter.createFromResource(Sgc_Pgc_Entry_Activity.this, R.array.sgc_brand, R.layout.whitetext_spin);
        sgc_brand_adapter.setDropDownViewResource(R.layout.pop_up_spin_sgc);
        brand_spinner.setAdapter(sgc_brand_adapter);

        email_edt.setFilters(new InputFilter[]{filter1});
        client_name_edt.setFilters(new InputFilter[]{filter1});
        incharge_name.setFilters(new InputFilter[]{filter1});

        dr_name_edt.setFilters(new InputFilter[]{filter1});

        website_edt.setFilters(new InputFilter[]{filter1});

        email_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        client_name_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        incharge_name.setFilters(new InputFilter[]{EMOJI_FILTER});

        dr_name_edt.setFilters(new InputFilter[]{EMOJI_FILTER});

        website_edt.setFilters(new InputFilter[]{EMOJI_FILTER});

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .build();

        latlong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(Sgc_Pgc_Entry_Activity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }
        });

        barProgressDialog = new ProgressDialog(Sgc_Pgc_Entry_Activity.this);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);

        pincode_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("9")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            ent_pin,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        pincode_edt.setText(enteredString.substring(1));
                    } else {
                        pincode_edt.setText("");
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

        mobile_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            ToastFile.crt_mob_num,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        mobile_edt.setText(enteredString.substring(1));
                    } else {
                        mobile_edt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                entered_Type_String = sgc_pgc_spinner.getSelectedItem().toString();
                entered_mobile_String = s.toString();
                if (entered_mobile_String.length() == 10) {
                    barProgressDialog.show();
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String URL = Api.checkValidEmail;
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("type", entered_Type_String);
                        jsonBody.put("mobile", entered_mobile_String);
                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "onResponse: " + response);
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                if (response.contains("\"TRUE\"")) {

                                } else {

                                    mobile_edt.setError(response);
                                    mobile_edt.setText("");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                Log.e("VOLLEY", error.toString());
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }

                        };

                        requestQueue.add(stringRequest);
                        Log.e(TAG, "afterTextChanged: url" + stringRequest);
                        Log.e(TAG, "afterTextChanged: url" + jsonBody);
                        Log.e(TAG, "afterTextChanged: url" + finalJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")
                        || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5")
                        || enteredString.startsWith("6") || enteredString.startsWith("7") || enteredString.startsWith("8")
                        || enteredString.startsWith("9")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            ToastFile.crt_mob_num,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        phone_number.setText(enteredString.substring(1));
                    } else {
                        phone_number.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteerdstring = s.toString();
            }
        });


        email_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter email id",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        email_edt.setText(enteredString.substring(1));
                    } else {
                        email_edt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 50) {
                    email_edt.setText(enteredString.substring(1));
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter maximum 50 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        client_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter client name",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        client_name_edt.setText(enteredString.substring(1));
                    } else {
                        client_name_edt.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 30) {
                    client_name_edt.setText(enteredString.substring(1));
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter maximum 30 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dr_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter client name",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        dr_name_edt.setText(enteredString.substring(1));
                    } else {
                        dr_name_edt.setText("");
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


        incharge_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter incharge name",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        incharge_name.setText(enteredString.substring(1));
                    } else {
                        incharge_name.setText("");
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


        website_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(Sgc_Pgc_Entry_Activity.this,
                            "Please enter website",
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        website_edt.setText(enteredString.substring(1));
                    } else {
                        website_edt.setText("");
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

        sgc_pgc_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sgc_pgc_spinner.getSelectedItem().equals("SGC")) {
                    brand_spinner.setVisibility(View.VISIBLE);
                    qualification_spinner.setVisibility(View.GONE);
                    pincode_edt.setVisibility(View.VISIBLE);
                    mobile_edt.setVisibility(View.VISIBLE);
                    email_edt.setVisibility(View.VISIBLE);
                    client_name_edt.setVisibility(View.VISIBLE);
                    incharge_name.setVisibility(View.VISIBLE);
                    addressEdt.setVisibility(View.VISIBLE);
                    phone_number.setVisibility(View.VISIBLE);
                    website_edt.setVisibility(View.VISIBLE);
                    visiting_card_layout.setVisibility(View.VISIBLE);
                    dr_name_layout.setVisibility(View.GONE);
                    pincode_edt.setText("");
                    mobile_edt.setText("");
                    email_edt.setText("");
                    dr_name_edt.setText("");
                    client_name_edt.setText("");
                    incharge_name.setText("");
                    addressEdt.setText("");
                    phone_number.setText("");
                    website_edt.setText("");
                    lattitude_txt.setText("");
                    longitude_txt.setText("");

                } else if (sgc_pgc_spinner.getSelectedItem().equals("PGC")) {
                    brand_spinner.setVisibility(View.GONE);
                    qualification_spinner.setVisibility(View.VISIBLE);
                    pincode_edt.setVisibility(View.VISIBLE);
                    mobile_edt.setVisibility(View.VISIBLE);
                    email_edt.setVisibility(View.VISIBLE);
                    dr_name_layout.setVisibility(View.VISIBLE);
                    addressEdt.setVisibility(View.VISIBLE);
                    phone_number.setVisibility(View.VISIBLE);
                    website_edt.setVisibility(View.VISIBLE);
                    visiting_card_layout.setVisibility(View.VISIBLE);
                    client_name_edt.setVisibility(View.GONE);
                    incharge_name.setVisibility(View.GONE);
                    pincode_edt.setText("");
                    mobile_edt.setText("");
                    email_edt.setText("");
                    dr_name_edt.setText("");
                    client_name_edt.setText("");
                    incharge_name.setText("");
                    lattitude_txt.setText("");
                    longitude_txt.setText("");
                    addressEdt.setText("");
                    phone_number.setText("");
                    website_edt.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        visiting_card_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, 0);
            }
        });

        email_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else if (!email_edt.getText().toString().equals("")) {
                    get_Registration_type = sgc_pgc_spinner.getSelectedItem().toString();
                    email_address = email_edt.getText().toString();

                    String example = email_address;
                    String getremainingstring = example.substring(example.lastIndexOf("@") + 1);

                    String s = getremainingstring;
                    String[] getCharacters = s.split("\\.");

                    if (getCharacters != null) {
                        for (int i = 0; i < getCharacters.length; i++) {
                            for (int j = i + 1; j < getCharacters.length; j++) {
                                if (getCharacters[i].equals(getCharacters[j])) {
                                    email_edt.requestFocus();
                                    email_edt.setError("Invalid email id");
                                    email_edt.setText("");
                                }
                            }
                        }
                    }
                    barProgressDialog.show();
                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        String URL = Api.checkValidEmail;
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("type", get_Registration_type);
                        jsonBody.put("email", email_address);
                        final String requestBody = jsonBody.toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG, "onResponse: " + response);
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                if (response.equalsIgnoreCase("\"TRUE\"")) {

                                } else {

                                    email_edt.requestFocus();
                                    email_edt.setError(response);
                                    email_edt.setText("");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                    barProgressDialog.dismiss();
                                }
                                Log.e("VOLLEY", error.toString());
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                    return null;
                                }
                            }
                        };
                        requestQueue.add(stringRequest);
                        Log.e(TAG, "onFocusChange: " + stringRequest);
                        Log.e(TAG, "onFocusChange: " + jsonBody);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        next_btn_sgc_pgc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                get_Registration_type = sgc_pgc_spinner.getSelectedItem().toString();
                if (get_Registration_type.equals("SGC")) {

                    getpincode = pincode_edt.getText().toString();
                    getmobile_number = mobile_edt.getText().toString();
                    email_address = email_edt.getText().toString();
                    getclient_name = client_name_edt.getText().toString();
                    get_brand_name = brand_spinner.getSelectedItem().toString();
                    get_incharge_name = incharge_name.getText().toString();
                    get_Address = addressEdt.getText().toString();
                    get_phone_number = phone_number.getText().toString();
                    get_website = website_edt.getText().toString();
                    getLatitude = lattitude_txt.getText().toString();
                    getLongitude = longitude_txt.getText().toString();
                    getclient_name = getclient_name.replaceAll("\\s+", " ");
                    get_incharge_name = get_incharge_name.replaceAll("\\s+", " ");
                    get_Address = get_Address.replaceAll("\\s+", " ");

                    getclient_name = getclient_name.replaceAll("\\.", "");
                    get_incharge_name = get_incharge_name.replaceAll("\\.", "");

                    String enteredUrl = website_edt.getText().toString();

                    if (!enteredUrl.equals("")) {
                        if (Patterns.WEB_URL.matcher(enteredUrl).matches()) {

                        } else {
                            Toast.makeText(Sgc_Pgc_Entry_Activity.this, "URL is invalid!", Toast.LENGTH_LONG).show();
                        }
                    }
//                     get_visiting_card = encodedImage.toString();
                    if (getLatitude.equalsIgnoreCase("")) {
                        lattitude_txt.setError("Enter latitude");
                    } else if (getLongitude.equalsIgnoreCase("")) {
                        longitude_txt.setError("Enter correct longitude");
                    } else if (getpincode.equals("")) {
                        pincode_edt.setError("Enter correct Pincode");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct Pincode", Toast.LENGTH_SHORT).show();
                    } else if (getpincode.length() < 6) {
                        pincode_edt.setError("Enter 6 digits Pincode");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.ent_pin, Toast.LENGTH_SHORT).show();
                    } else if (getmobile_number.equals("")) {
                        mobile_edt.requestFocus();
                        mobile_edt.setError("Enter correct Mobile number");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct Mobile number", Toast.LENGTH_SHORT).show();
                    } else if (getmobile_number.length() < 10) {
                        mobile_edt.requestFocus();
                        mobile_edt.setError("Enter 10 digits Mobile number");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct Mobile number", Toast.LENGTH_SHORT).show();
                    } else if (email_address.equals("")) {
                        email_edt.requestFocus();
                        email_edt.setError("Please enter Email address");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter Email address", Toast.LENGTH_SHORT).show();
                    } else if (getclient_name.equals("")) {
                        client_name_edt.requestFocus();
                        client_name_edt.setError("Please enter correct Client name");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct Client name", Toast.LENGTH_SHORT).show();
                    } else if (getclient_name.length() < 2) {
                        client_name_edt.requestFocus();
                        client_name_edt.setError("Please enter correct Client name");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct Client name", Toast.LENGTH_SHORT).show();
                    } else if (get_incharge_name.equals("")) {
                        incharge_name.requestFocus();
                        incharge_name.setError("Please enter correct Incharge name");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct Incharge name", Toast.LENGTH_SHORT).show();
                    } else if (get_incharge_name.length() < 2) {
                        incharge_name.requestFocus();
                        incharge_name.setError("Please enter correct Incharge name");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct Incharge name", Toast.LENGTH_SHORT).show();
                    } else if (get_Address.equals("")) {
                        addressEdt.requestFocus();
                        addressEdt.setError(ToastFile.ent_addr);
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.ent_addr, Toast.LENGTH_SHORT).show();
                    } else if (get_Address.length() < 25) {
                        addressEdt.requestFocus();
                        addressEdt.setError(ToastFile.addre25long);
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.ent_addr, Toast.LENGTH_SHORT).show();
                    } else if (image == null) {
                        cross_img.setVisibility(View.VISIBLE);
                        correct_img.setVisibility(View.GONE);
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please upload visiting card", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!email_address.equals("")) {
//                        isValidMail(email_address);
                        boolean check;
                        Pattern p;
                        Matcher m;

                        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                        p = Pattern.compile(EMAIL_STRING);

                        m = p.matcher(email_address);
                        check = m.matches();

                        if (!check) {
                            email_edt.requestFocus();
                            email_edt.setError("Not Valid Email");
                        } else {
                            uploadClientEntry("SGCEntry", get_brand_name, getclient_name, "", get_incharge_name);
                        }

                    }

                } else if (get_Registration_type.equals("PGC")) {
                    getpincode = pincode_edt.getText().toString();//
                    getmobile_number = mobile_edt.getText().toString();//
                    email_address = email_edt.getText().toString();//
                    get_dr_name = dr_name_edt.getText().toString();//
                    get_qualification = qualification_spinner.getSelectedItem().toString();
                    get_Address = addressEdt.getText().toString();
                    get_phone_number = phone_number.getText().toString();
                    get_website = website_edt.getText().toString();
                    getLatitude = lattitude_txt.getText().toString();
                    getLongitude = lattitude_txt.getText().toString();
                    get_dr_name = get_dr_name.replaceAll("\\s+", " ");
                    get_Address = get_Address.replaceAll("\\s+", " ");
                    get_dr_name = get_dr_name.replaceAll("\\.", "");


//                     get_visiting_card = encodedImage.toString();

                    if (getLatitude.equalsIgnoreCase("")) {
                        lattitude_txt.setError("Enter latitude");
                    } else if (getLongitude.equalsIgnoreCase("")) {
                        longitude_txt.setError("Enter correct longitude");
                    } else if (getpincode.equals("")) {
                        pincode_edt.requestFocus();
                        pincode_edt.setError("Enter correct Pincode");
                    } else if (getpincode.length() < 6) {
                        pincode_edt.requestFocus();
                        pincode_edt.setError("Enter 6 digits Pincode");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct Pincode", Toast.LENGTH_SHORT).show();
                    } else if (getmobile_number.equals("")) {
                        mobile_edt.requestFocus();
                        mobile_edt.setError("Enter correct Mobile number");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct Mobile number", Toast.LENGTH_SHORT).show();
                    } else if (getmobile_number.length() < 10) {
                        mobile_edt.requestFocus();
                        mobile_edt.setError("Enter 10 digits Mobile number");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Enter correct Mobile number", Toast.LENGTH_SHORT).show();
                    } else if (email_address.equals("")) {
                        email_edt.requestFocus();
                        email_edt.setError("Please enter Email address");
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter Email address", Toast.LENGTH_SHORT).show();
                    } else if (get_dr_name.equals("")) {
                        dr_name_edt.requestFocus();
                        dr_name_edt.setError(ToastFile.crt_name_woe);
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct Client name", Toast.LENGTH_SHORT).show();
                    } else if (get_dr_name.length() < 2) {
                        dr_name_edt.requestFocus();
                        dr_name_edt.setError(ToastFile.crt_name_woe);
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, "Please enter correct Client name", Toast.LENGTH_SHORT).show();
                    } else if (get_Address.equals("")) {
                        addressEdt.requestFocus();
                        addressEdt.setError(ToastFile.ent_addr);
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.ent_addr, Toast.LENGTH_SHORT).show();
                    } else if (get_Address.length() < 25) {
                        addressEdt.requestFocus();
                        addressEdt.setError(ToastFile.addre25long);
//                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.ent_addr, Toast.LENGTH_SHORT).show();
                    } else if (image == null) {
                        cross_img.setVisibility(View.VISIBLE);
                        correct_img.setVisibility(View.GONE);
                        TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "Please upload visiting card", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    } else if (!email_address.equals("")) {

                        boolean check;
                        Pattern p;
                        Matcher m;

                        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                        p = Pattern.compile(EMAIL_STRING);

                        m = p.matcher(email_address);
                        check = m.matches();

                        if (!check) {
                            email_edt.requestFocus();
                            email_edt.setError("Not Valid Email");
                        } else {
                            uploadClientEntry("PGCEntry", "", "DR " + get_dr_name, get_qualification, "");
                        }
                    }
                }
            }
        });

    }

    private void uploadClientEntry(final String type, String brandName, String name, String qualification, String inchargeName) {
        barProgressDialog.show();

        PostQuePGC = Volley.newRequestQueue(Sgc_Pgc_Entry_Activity.this);
        JSONObject jsonObject = null;
        try {
            ClientRegisterRequestModel requestModel = new ClientRegisterRequestModel();
            requestModel.setAddress(get_Address.replace("+", ""));
            requestModel.setBrand(brandName);
            requestModel.setChannel("MOBILE APPLICATION");
            requestModel.setCountry("India");
            requestModel.setEmail(email_address);
            requestModel.setInchargeName(inchargeName);
            requestModel.setName(name);
            requestModel.setTspOLc(user);
            requestModel.setVerificationSource("");
            requestModel.setQualification(qualification);
            requestModel.setSpeciality("");
            requestModel.setApikey(api_key);
            requestModel.setMobile(getmobile_number);
            requestModel.setPhone_no(get_phone_number);
            requestModel.setEntryType(get_Registration_type);
            requestModel.setPincode(getpincode);
            requestModel.setWebsite(get_website);
            requestModel.setAPP("B2B");
            requestModel.setFile(imageName);
            requestModel.setLatitute(String.valueOf(lat));
            requestModel.setLongitude(String.valueOf(log));
            requestModel.setOpType("entry");
            requestModel.setVisiting_Card(image);

            String json = new Gson().toJson(requestModel);
            jsonObject = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.SGCRegistration, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: " + response);
                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                        barProgressDialog.dismiss();
                    }
                    ClientRegisterResponseModel responseModel = new Gson().fromJson(String.valueOf(response), ClientRegisterResponseModel.class);
                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {

                            if (type.equalsIgnoreCase("PGCEntry")) {
                                TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "PGC Registered Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            } else {
                                TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "SGC Registered Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            }

                            Intent i = new Intent(Sgc_Pgc_Entry_Activity.this, Sgc_Pgc_Entry_Activity.class);
                            startActivity(i);
                            finish();
                        } else if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase(small_invalidApikey)) {
                            GlobalClass.redirectToLogin(Sgc_Pgc_Entry_Activity.this);
                        } else {
                            TastyToast.makeText(Sgc_Pgc_Entry_Activity.this, "" + responseModel.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                    } else {
                        Toast.makeText(Sgc_Pgc_Entry_Activity.this, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show();
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
        PostQuePGC.add(jsonObjectRequest1);
        Log.e(TAG, "uploadClientEntry: " + jsonObject);
        Log.e(TAG, "uploadClientEntry: " + jsonObjectRequest1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(Sgc_Pgc_Entry_Activity.this, data);


            Geocoder geocoder = new Geocoder(Sgc_Pgc_Entry_Activity.this);
            try {
                List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);

                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                String attributions = (String) place.getAttributions();
                if (attributions == null) {
                    attributions = "";
                }

                if (addresses != null && addresses.size() > 0) {
                    if (addresses.get(0).getPostalCode() != null) {
                        pincode = addresses.get(0).getPostalCode();
                        country = addresses.get(0).getCountryName().toUpperCase();
                        city = addresses.get(0).getLocality().toUpperCase();
                    }

                }

            } catch (Exception e) {

                e.printStackTrace();
            }


            lat = place.getLatLng().latitude;
            log = place.getLatLng().longitude;


            String address = place.getAddress().toString().toUpperCase();

            if (pincode != null && !pincode.isEmpty()) {

                if (address.contains(pincode)) {
                    address = address.replace(pincode, "");
                }


                if (country != null && !country.isEmpty()) {
                    if (address.contains(", " + country)) {
                        address = address.replace(", " + country, "");
                    } else if (address.toUpperCase().contains(country)) {
                        address = address.replace(country, "");
                    }

                }


            }


            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }

            // edt_long.setText((int) Double.parseDouble(String.valueOf(log)));
           /* edt_pincode.setText(pincode);
            edt_addr.setText(address);
            edt_lat.setText("" + lat);
            edt_long.setText("" + log);*/
            pincode_edt.setText(pincode);
            addressEdt.setText(address);
            lattitude_txt.setText("Lat :" + String.valueOf(lat));
            lattitude_txt.setError(null);

            longitude_txt.setText("Long :" + String.valueOf(log));
            longitude_txt.setError(null);
            //Toast.makeText(activity, ""+name, Toast.LENGTH_SHORT).show();

        }
        /*else {
            super.onActivityResult(requestCode, resultCode, data);
        }*/


        if (data != null && requestCode == 0) {

            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();

                //ImageName Procedure
                String picturePath = null;
                try {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(targetUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Uptill Here

                try {
                    imageName = picturePath.substring(picturePath.lastIndexOf("/") + 1, picturePath.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    image = ConvertBitmapToString(resizedBitmap);

                    correct_img.setVisibility(View.VISIBLE);

                    cross_img.setVisibility(View.GONE);


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(Sgc_Pgc_Entry_Activity.this);
        mGoogleApiClient.disconnect();
    }
/*    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//
////            Bitmap converetdImage = getResizedBitmap(photo, 100);
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
////            converetdImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//            byte[] b = baos.toByteArray();
//
//            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//            System.out.println("" + encodedImage);
//
//        }
//    }

//    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
//        int width = image.getWidth();
//        int height = image.getHeight();
//        System.out.println("height of image" + height);
//
//        float bitmapRatio = (float) width / (float) height;
//        if (bitmapRatio > 1) {
//            width = maxSize;
//            height = (int) (width / bitmapRatio);
//        } else {
//            height = maxSize;
//            width = (int) (height * bitmapRatio);
//        }
//        return Bitmap.createScaledBitmap(image, width, height, true);
//    }*/


}
