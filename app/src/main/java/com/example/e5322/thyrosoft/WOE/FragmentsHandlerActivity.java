package com.example.e5322.thyrosoft.WOE;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.Fragment.SummaryFragment;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RevisedScreenNewUser.Edit_Woe;

public class FragmentsHandlerActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    TextView mTitle;
    ImageView mLogo;
    String strHead = "", strId = "", strData = "", strType = "", strDataType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_handler);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);

        Drawable upArrow,homebutton;

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.abc_ic_ab_back_material,null);
        else
            upArrow = ContextCompat.getDrawable(FragmentsHandlerActivity.this, R.drawable.abc_ic_ab_back_material);

        upArrow.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


//        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
//            homebutton = ResourcesCompat.getDrawable(getResources(), R.drawable.home_button,null);
//        else
//            homebutton = ContextCompat.getDrawable(FragmentsHandlerActivity.this, R.drawable.home_button);
//
//        homebutton.setColorFilter(getResources().getColor(R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(homebutton);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        strHead = getIntent().getStringExtra("head");
        strType = getIntent().getStringExtra("type");
        if (getIntent().hasExtra("datatype")) {
            strDataType = getIntent().getStringExtra("datatype");
        }

        //strDataType=getIntent().getStringExtra("datatype");
        //strData=getIntent().getStringExtra("data");
        mTitle.setText(strHead);

        Bundle data = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment fragment = null;

        if (strType.equals("package")) {
          /*  fragment = new WellnessFragment();
            ft.replace(R.id.container, fragment);
            ft.commit();*/
        } else if (strType.equals("summary")) {
            fragment = new SummaryFragment();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }else if(strType.equals("finalwoesummary")){
            fragment = new Start_New_Woe();
            ft.replace(R.id.fragment_mainLayout, fragment);
            ft.commit();
        }else if(strType.equals("woe_edit")){
            fragment = new Edit_Woe();
            ft.replace(R.id.container, fragment);
            ft.commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                if (getIntent().hasExtra("back")) {
                    Intent intent = new Intent(FragmentsHandlerActivity.this, ManagingTabsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    this.finish();
                }
            }

            return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("back")) {
            Intent intent = new Intent(FragmentsHandlerActivity.this, ManagingTabsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else
        super.onBackPressed();
    }
}
