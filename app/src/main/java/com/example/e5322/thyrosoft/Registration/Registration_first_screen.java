package com.example.e5322.thyrosoft.Registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by E5322 on 19-03-2018.
 */

public class Registration_first_screen extends Activity {

    EditText reg_name,reg_landline,reg_profession,reg_qualification,reg_int_location;
    Button next;
    String reg_name_shared, reg_landline_shared, reg_profession_shared, reg_qualification_shared, reg_int_location_shared;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newuser_registration_first);

        reg_name =(EditText)findViewById(R.id.reg_name);
        reg_landline =(EditText)findViewById(R.id.reg_landline);
        reg_profession =(EditText)findViewById(R.id.reg_profession);
        reg_qualification =(EditText)findViewById(R.id.reg_qualification);
        reg_int_location =(EditText)findViewById(R.id.reg_int_location);
        next=(Button)findViewById(R.id.reg_next);


        reg_landline.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String enteredString = s.toString();
                if (enteredString.startsWith(".")) {
                    TastyToast.makeText(Registration_first_screen.this,ToastFile.crt_landline_num, TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);

                    if (enteredString.length() > 0) {
                        reg_landline.setText(enteredString.substring(1));
                    } else {
                        reg_landline.setText("");
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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_name.getText().toString().equals("") && reg_name.getText().length()<5){
                    TastyToast.makeText(Registration_first_screen.this, ToastFile.ent_name, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_landline.getText().toString().equals("")){
                    TastyToast.makeText(Registration_first_screen.this,ToastFile.ent_number, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_landline.getText().length()<7){
                    TastyToast.makeText(Registration_first_screen.this,ToastFile.ent_number, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_profession.getText().toString().equals("")&& reg_profession.getText().length()<5){
                    TastyToast.makeText(Registration_first_screen.this,ToastFile.ent_profession, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else  if(reg_qualification.getText().toString().equals("")&& reg_qualification.getText().length()<2){
                    TastyToast.makeText(Registration_first_screen.this,ToastFile.ent_qualif, TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }else if(reg_int_location.getText().toString().equals("")&& reg_int_location.getText().length()<5){
                    TastyToast.makeText(Registration_first_screen.this,ToastFile.ent_loc, TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }else{

                  reg_name_shared                  =reg_name.getText().toString();
                  reg_landline_shared              =reg_landline.getText().toString();
                  reg_profession_shared                =reg_profession.getText().toString();
                  reg_qualification_shared         =reg_qualification.getText().toString();
                  reg_int_location_shared          =reg_int_location.getText().toString();

                Intent reg = new Intent(Registration_first_screen.this, Registration_second_screen.class);
                startActivity(reg);

                    SharedPreferences.Editor Registration = getSharedPreferences("Registration", 0).edit();
                    Registration.putString("name_user", reg_name_shared);
                    Registration.putString("landline", reg_landline_shared);
                    Registration.putString("profession", reg_profession_shared);
                    Registration.putString("qualification", reg_qualification_shared);
                    Registration.putString("location", reg_int_location_shared);
                    Registration.apply();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
