package com.example.e5322.thyrosoft.Activity.frags;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BSTestDataModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.example.e5322.thyrosoft.API.Api.checkNumber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Enter_Blood_sugar_data_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Enter_Blood_sugar_data_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Enter_Blood_sugar_data_Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner spin_bs_test;
    String[] arr;
    EditText mobile_edt, client_name_edt, edt_val, edt_age, edt_email;
    Button btn_verify, btn_choose_file, btn_submit_bs;
    TextView txt_ref_msg, tvUploadImageText;
    ImageView male, male_red, female, female_red, correct_img, cross_img;
    LinearLayout choose_file_ll, value_ll;
    public static final int PICK_IMAGE = 1;

    File file;
    Uri fileUri;

    private OnFragmentInteractionListener mListener;
    private String mobile_number;
    private String TAG = Enter_Blood_sugar_data_Fragment.class.getSimpleName().toString();
    private boolean validMobileNumber;
    private String getResponse;
    private boolean setFlagTocheckMobileNumber = false;
    private ProgressDialog progressDialog;
    private String imageName, image;
    int PIC_SELECTION = 100, minValue = 0, maxValue = 0, enteredVal = 0;
    private Uri targetUri;
    private String gender = "";

    public Enter_Blood_sugar_data_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Enter_Blood_sugar_data_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Enter_Blood_sugar_data_Fragment newInstance(String param1, String param2) {
        Enter_Blood_sugar_data_Fragment fragment = new Enter_Blood_sugar_data_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewMain = (View) inflater.inflate(R.layout.fragment_entered__blood_sugar_activity, container, false);

        spin_bs_test = (Spinner) viewMain.findViewById(R.id.spin_bs_test);
        mobile_edt = (EditText) viewMain.findViewById(R.id.mobile_edt);
        client_name_edt = (EditText) viewMain.findViewById(R.id.client_name_edt);
        edt_val = (EditText) viewMain.findViewById(R.id.edt_val);
        edt_age = (EditText) viewMain.findViewById(R.id.edt_age);
        edt_email = (EditText) viewMain.findViewById(R.id.edt_email);

        btn_verify = (Button) viewMain.findViewById(R.id.btn_verify);
        btn_choose_file = (Button) viewMain.findViewById(R.id.btn_choose_file);
        btn_submit_bs = (Button) viewMain.findViewById(R.id.btn_submit_bs);

        txt_ref_msg = (TextView) viewMain.findViewById(R.id.txt_ref_msg);
        tvUploadImageText = (TextView) viewMain.findViewById(R.id.tvUploadImageText);

        male = (ImageView) viewMain.findViewById(R.id.male);
        male_red = (ImageView) viewMain.findViewById(R.id.male_red);
        female = (ImageView) viewMain.findViewById(R.id.female);
        female_red = (ImageView) viewMain.findViewById(R.id.female_red);
        correct_img = (ImageView) viewMain.findViewById(R.id.correct_img);
        cross_img = (ImageView) viewMain.findViewById(R.id.cross_img);

        choose_file_ll = (LinearLayout) viewMain.findViewById(R.id.choose_file_ll);
        value_ll = (LinearLayout) viewMain.findViewById(R.id.value_ll);


        ArrayAdapter<BSTestDataModel> adap = new ArrayAdapter<BSTestDataModel>(getActivity().getApplicationContext(), R.layout.name_age_spinner, GlobalClass.getTestList());
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_bs_test.setAdapter(adap);

        value_ll.setVisibility(View.GONE);

        mobile_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                        || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5") || enteredString.startsWith("6")) {
                    mobile_edt.setText(enteredString.substring(1));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                validMobileNumber = false;
                String enteredString = s.toString();
                if (enteredString.length() == 10) {
                    if (enteredString.startsWith("0") || enteredString.startsWith("1") || enteredString.startsWith("2")
                            || enteredString.startsWith("3") || enteredString.startsWith("4") || enteredString.startsWith("5") || enteredString.startsWith("6")) {
                        TastyToast.makeText(getActivity(), ToastFile.crt_mob_num, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        validMobileNumber = false;
                    } else {
                        verifyMobileNumber(enteredString);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_number = mobile_edt.getText().toString();
                if ((mobile_number.length() > 0) && (mobile_number.length() < 10)) {
                    TastyToast.makeText(getContext(), "Enter valid mobile number !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    validMobileNumber = false;
                } else if (mobile_number.length() == 0) {
                    TastyToast.makeText(getContext(), "Mobile number should not be blank !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    validMobileNumber = false;
                } else {
                    verifyMobileNumber(mobile_number);
                }
            }
        });

        client_name_edt.setFilters(new InputFilter[]{EMOJI_FILTER});
        client_name_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    if (enteredString.length() == 0) {
                        client_name_edt.setText("");
                    } else {
                        client_name_edt.setText(enteredString.substring(1));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    if (enteredString.length() > 1) {
                        edt_age.setText(enteredString.substring(1));
                    } else {
                        edt_age.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                if (enteredString.length() > 0) {
                    int result = Integer.parseInt(enteredString);
                    if (result > 200) {
                        edt_age.setText(enteredString.substring(0, 2));
                        edt_age.setText("");
                        //TastyToast.makeText(getActivity(), "Age must be less than 200 yrs !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                    }
                }
            }
        });

        edt_val.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith("0")) {
                    if (enteredString.length() > 0) {
                        edt_val.setText(enteredString.substring(1));
                    } else {
                        edt_val.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = s.toString();
                /*enteredVal=Integer.parseInt(enteredString);
                if (!spin_bs_test.getSelectedItem().toString().equals("Select Test type")) {
                    if ((enteredVal < minValue) || (enteredVal > maxValue)) {
                        edt_val.requestFocus();
                        edt_val.setText("");
                        TastyToast.makeText(getContext(),"Entered value should be between"+minValue+"-"+maxValue+"",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                    }
                }*/
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setVisibility(View.GONE);
                male_red.setVisibility(View.VISIBLE);
                female.setVisibility(View.VISIBLE);
                female_red.setVisibility(View.GONE);
                gender = "male";
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setVisibility(View.GONE);
                female_red.setVisibility(View.VISIBLE);
                male.setVisibility(View.VISIBLE);
                male_red.setVisibility(View.GONE);
                gender = "female";
            }
        });

        spin_bs_test.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    txt_ref_msg.setText("");
                    value_ll.setVisibility(View.GONE);
                } else {
                    value_ll.setVisibility(View.VISIBLE);
                    minValue = GlobalClass.getTestList().get(position).getMinVal();
                    maxValue = GlobalClass.getTestList().get(position).getMaxVal();
                    txt_ref_msg.setText("Ref. Range: " + GlobalClass.getTestList().get(position).getRangeVal());
                    edt_val.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PIC_SELECTION);
            }
        });

        tvUploadImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (targetUri != null) {
                    String path = getPathFromURI(targetUri);
                    File imgFile = new File(path);
                    if (imgFile.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View promptsView = li.inflate(R.layout.custom_dialog_imageviewer, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getContext());
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
            }
        });

        btn_submit_bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Valdation()) {
                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return viewMain;
    }

    private boolean Valdation() {
        if (mobile_edt.getText().toString().length() == 0) {
            TastyToast.makeText(getContext(), "Enter mobile number !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            mobile_edt.requestFocus();
            mobile_edt.setText("");
            return false;
        } else if (mobile_edt.getText().toString().length() < 10) {
            TastyToast.makeText(getContext(), "Enter valid mobile number !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            mobile_edt.requestFocus();
            mobile_edt.setText("");
            return false;
        } else if (validMobileNumber == false) {
            TastyToast.makeText(getContext(), "Entered mobile number not verified !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            mobile_edt.requestFocus();
            mobile_edt.setText("");
            return false;
        } else if (client_name_edt.getText().toString().length() == 0) {
            TastyToast.makeText(getContext(), "Enter name !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            client_name_edt.requestFocus();
            client_name_edt.setText("");
            return false;
        } else if (edt_age.getText().toString().length() == 0) {
            TastyToast.makeText(getContext(), "Enter age !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            edt_age.requestFocus();
            edt_age.setText("");
            return false;
        } else if (gender.equals("") || gender.equals(null)) {
            TastyToast.makeText(getContext(), "Select gender !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return false;
        } else if (spin_bs_test.getSelectedItem().toString().equals("Select Test type")) {
            TastyToast.makeText(getContext(), "Select test type !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return false;
        } else if (edt_val.getText().toString().equals("")) {
            TastyToast.makeText(getContext(), "Enter value !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            edt_val.requestFocus();
            return false;
        } else if (correct_img.getVisibility() == View.GONE) {
            TastyToast.makeText(getContext(), "Upload the image !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            return false;
        } else if (edt_email.getText().toString().length() == 0 || edt_email.getText().toString() == null) {
            TastyToast.makeText(getContext(), "Enter email id !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            edt_email.requestFocus();
            edt_email.setText("");
            return false;
        } else if (emailValidation(edt_email.getText().toString()) == false) {
            TastyToast.makeText(getContext(), "Enter valid email id !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            edt_email.requestFocus();
            edt_email.setText("");
            return false;
        } else if ((edt_val.getText().toString().length() > 0)) {
            enteredVal = Integer.parseInt(edt_val.getText().toString());
            if (((enteredVal > minValue) && (enteredVal < maxValue))) {
                return true;
            } else {
                TastyToast.makeText(getContext(), "Enter valid value !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                edt_val.requestFocus();
                edt_val.setText("");
                return false;
            }

        }
        return true;
    }

    private void verifyMobileNumber(final String mobile_number) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Kindly wait ...");
        progressDialog.setMessage(ToastFile.processing_request);
        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(20);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue reques5tQueueCheckNumber = Volley.newRequestQueue(getActivity());
        StringRequest jsonObjectRequestPop = new StringRequest(StringRequest.Method.GET, checkNumber + mobile_number, new
                Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(TAG, "onResponse: response" + response);

                        getResponse = response.replaceAll("\"", "");
                        if (getResponse.equalsIgnoreCase("proceed")) {
                            validMobileNumber = true;
                        } else {
                            validMobileNumber = false;
                            TastyToast.makeText(getActivity(), getResponse, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }
                        System.out.println("in funct:" + validMobileNumber);

                        if (validMobileNumber == false) {
                            mobile_edt.setText("");
                        }

                        if (!((Activity) getActivity()).isFinishing()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                    }
                }
            }
        });
        jsonObjectRequestPop.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        reques5tQueueCheckNumber.add(jsonObjectRequestPop);
        Log.e(TAG, "afterTextChanged: URL" + jsonObjectRequestPop);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == PIC_SELECTION) {
            if (resultCode == RESULT_OK) {
                targetUri = data.getData();

                //ImageName Procedure
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(targetUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                //Uptill Here

                imageName = picturePath.substring(picturePath.lastIndexOf("/") + 1, picturePath.length());

                Bitmap bitmap;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    image = ConvertBitmapToString(resizedBitmap);

                    if (image != null) {
                        correct_img.setVisibility(View.VISIBLE);
                    } else {
                        cross_img.setVisibility(View.GONE);
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

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

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static boolean emailValidation(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
