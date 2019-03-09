//package com.example.e5322.thyrosoft;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
//import com.example.e5322.thyrosoft.RevisedScreenNewUser.MainActivity;
//import com.example.e5322.thyrosoft.startscreen.ConnectionDetector;
//
//import java.util.List;
//public class Vikas_Main_Screen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    private String TAG = MainActivity.class.getSimpleName();
//    private Context context;
//    private Activity activity;
//    private ImageView imageView;
//    private AppPreferenceManager appPreferenceManager;
//    private ConnectionDetector cd;
//    private AlertDialog.Builder alertdialog;
//    private TextView nav_hdr_name, nav_hdr_mobile, toolbar_title, tv_nav_ver_no;
//    private Toolbar my_toolbar;
//    private View header;
//    private Fragment fragment = null;
//    private Object currentFragment;
//    private String name = "", mobile = "";
//    NavigationView navigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        context = this;
//        activity = MainActivity.this;
//        fragment = new HomeFragment();
//        appPreferenceManager = new AppPreferenceManager(context);
//        cd = new ConnectionDetector(context);
//        alertdialog = new AlertDialog.Builder(this);
//        initUI();
//        initListners();
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, my_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        header = navigationView.getHeaderView(0);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_home);
//
//        try {
//            nav_hdr_name = (TextView) header.findViewById(R.id.nav_hdr_name);
//            nav_hdr_mobile = (TextView) header.findViewById(R.id.nav_hdr_mobile);
//            tv_nav_ver_no = (TextView) header.findViewById(R.id.tv_nav_ver_no);
//
//            if (appPreferenceManager.getLoginResponseModel() != null) {
//                name = appPreferenceManager.getLoginResponseModel().getNAME();
//                mobile = appPreferenceManager.getLoginResponseModel().getMOBILE();
//            } else if (appPreferenceManager.getReferResponseModel() != null) {
//                name = appPreferenceManager.getReferResponseModel().getName();
//                mobile = appPreferenceManager.getReferResponseModel().getMobile();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        nav_hdr_name.setText(name);
//        nav_hdr_mobile.setText(mobile);
//
//        getVersionInfo();
//
//        if (fragment != null) {
//            gotoFragment(fragment);
//        }
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//    }
//
//    private void getVersionInfo() {
//        String versionName = "";
//        try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//            versionName = packageInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        tv_nav_ver_no.setText(getString(R.string.version_no) + versionName);
//    }
//
//    private void initUI() {
//        my_toolbar = (Toolbar) findViewById(R.id.my_toolbar_main);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
//
//        toolbar_title.setText("Book Test");
//    }
//
//    private void initListners() {
//
//    }
//
//    private void logout() {
//        alertdialog.setMessage("Do you want to logout ?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        appPreferenceManager.setIsLogin(false);
//                        appPreferenceManager.setIsProfilesStored(false);
//                        appPreferenceManager.clearAllPreferences();
//                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        ConstantUtils.toastySuccess(context, getString(R.string.str_logout), false);
//                        startActivity(intent);
//                        finish();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alertdialog.create();
//        alert.show();
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        try {
//            List fragments = getSupportFragmentManager().getFragments();
//            currentFragment = fragments.get(fragments.size() - 1);
//            Log.e(TAG, "onBackPressed: " + currentFragment);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (currentFragment == null) {
//            exitApp();
//        } else if (currentFragment.toString().contains("HomeFragment")) {
//            exitApp();
//        } else if (currentFragment.toString().contains("BookingDoneFragment")) {
//            fragment = new HomeFragment();
//            navigationView.setCheckedItem(R.id.nav_home);
//            toolbar_title.setText(R.string.title_book);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("ReferFragment")) {
//            fragment = new HomeFragment();
//            navigationView.setCheckedItem(R.id.nav_home);
//            toolbar_title.setText(R.string.title_book);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("ProfileFragment")) {
//            fragment = new HomeFragment();
//            navigationView.setCheckedItem(R.id.nav_home);
//            toolbar_title.setText(R.string.title_book);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("IncentiveSummaryFragment")) {
//            fragment = new HomeFragment();
//            navigationView.setCheckedItem(R.id.nav_home);
//            toolbar_title.setText(R.string.title_book);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("IncentiveDetailsFragment")) {
//            fragment = new IncentiveSummaryFragment();
//            toolbar_title.setText(R.string.incentive_summary);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("MyLeadsFragment")) {
//            fragment = new HomeFragment();
//            navigationView.setCheckedItem(R.id.nav_home);
//            toolbar_title.setText(R.string.title_book);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("LeadDetailsFragment")) {
//            fragment = new MyLeadsFragment();
//            toolbar_title.setText(R.string.str_leads);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("FAQFragment")) {
//            fragment = new HomeFragment();
//            navigationView.setCheckedItem(R.id.nav_home);
//            toolbar_title.setText(R.string.title_book);
//            goBackToFragment(fragment);
//        } else if (currentFragment.toString().contains("CheckPincodeFragment")) {
//            fragment = new HomeFragment();
//            navigationView.setCheckedItem(R.id.nav_home);
//            toolbar_title.setText(R.string.title_book);
//            goBackToFragment(fragment);
//        } else {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
//    }
//
//    private void gotoFragment(Fragment fragment) {
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//        }
//    }
//
//    public void goBackToFragment(Fragment fragment) {
//        if (fragment != null) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//        }
//    }
//
//    private void exitApp() {
//        alertdialog.setMessage("Do you want to exit ?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        ActivityCompat.finishAffinity(activity);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alertdialog.create();
//        alert.show();
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            fragment = new HomeFragment();
//            toolbar_title.setText(R.string.title_book);
//        } else if (id == R.id.nav_profile) {
//            fragment = new ProfileFragment();
//            toolbar_title.setText(R.string.title_prof);
//        } else if (id == R.id.nav_incentive_sum) {
//            fragment = new IncentiveSummaryFragment();
//            toolbar_title.setText(R.string.incentive_summary);
//        } else if (id == R.id.nav_leads) {
//            fragment = new MyLeadsFragment();
//            toolbar_title.setText(R.string.str_leads);
//        } else if (id == R.id.nav_refer) {
//            fragment = new ReferFragment();
//            toolbar_title.setText(R.string.title_refer);
//        } else if (id == R.id.nav_faq) {
//            fragment = new FAQFragment();
//            toolbar_title.setText(R.string.title_faq);
//        } else if (id == R.id.nav_pincode_check) {
//            fragment = new CheckPincodeFragment();
//            toolbar_title.setText(getString(R.string.service_availability));
//        } else if (id == R.id.nav_logout) {
//            logout();
//        }
//        gotoFragment(fragment);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//}
