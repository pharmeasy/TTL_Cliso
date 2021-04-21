package com.example.e5322.thyrosoft.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.AdminCovidAdapter;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreferenceManager;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.StaffCovidadapter;
import com.google.android.material.tabs.TabLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarouselFragment extends Fragment {

    public static Object currentFragment;
    private static ManagingTabsActivity mContext;
    /**
     * TabPagerIndicator
     * <p>
     * Please refer to ViewPagerIndicator library
     */
    protected TabLayout tabLayout;
    protected ViewPager pager;
    String TAG = ManagingTabsActivity.class.getSimpleName();
    String user, passwrd, access, api_key, user_code, CLIENT_TYPE;


    private ViewPagerAdapter adapter;
    private StaffViewPagerAdapter adapterStafff;
    private StaffCovidadapter covidadapter;
    private AdminCovidAdapter adminCovidAdapter;


    NHF_pageradapter nhf_pageradapter;
    private int positionInt = 0;
    private SharedPreferences prefs;
    AppPreferenceManager appPreferenceManager;
    private boolean covidacc;

    public CarouselFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_carousel, container, false);
        mContext = (ManagingTabsActivity) getActivity();
        tabLayout = rootView.findViewById(R.id.tabs);
        pager = rootView.findViewById(R.id.vp_pages);

        prefs = getActivity().getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        CLIENT_TYPE = prefs.getString("CLIENT_TYPE", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        user_code = prefs.getString("USER_CODE", "");
        appPreferenceManager = new AppPreferenceManager(getActivity());

        covidacc = getOfModulesAccess();

        if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

                Log.e(TAG, "positionInt-->" + position);
                Log.e(TAG, "pager positionInt-->" + pager.getCurrentItem());
            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return rootView;
    }

    private boolean getOfModulesAccess() {
        try {
            if (appPreferenceManager.getCovidAccessResponseModel().isCovidRegistration() || appPreferenceManager.getCovidAccessResponseModel().isRat()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
            nhf_pageradapter = new NHF_pageradapter(getResources(), getChildFragmentManager());
            pager.setAdapter(nhf_pageradapter);
        } else {
            if (access.equalsIgnoreCase(Constants.STAFF)) {

                if (covidacc) {
                    covidadapter = new StaffCovidadapter(getResources(), getChildFragmentManager());
                    pager.setAdapter(covidadapter);
                } else {
                    adapterStafff = new StaffViewPagerAdapter(getResources(), getChildFragmentManager());
                    pager.setAdapter(adapterStafff);
                }
            } else {

                if (covidacc) {
                    adminCovidAdapter = new AdminCovidAdapter(getResources(), getChildFragmentManager());
                    pager.setAdapter(adminCovidAdapter);
                } else {
                    adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
                    pager.setAdapter(adapter);
                }
            }
        }

        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(Color.parseColor("#ffffff"), mContext.getResources().getColor(R.color.tabindicatorColor));

        //TODO if tab menu are present in navigatoion drawer then get Intent from managingtab activity and set the fragment
        try {
            if (getArguments() != null) {
                positionInt = getArguments().getInt("position");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (Constants.tab_flag.equalsIgnoreCase("1")) {
            if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                pager.setCurrentItem(0);
            } else {
                pager.setCurrentItem(4);
            }
            Constants.tab_flag = "0";
        } else {
            Constants.tab_flag = "0";


            if (covidacc) {
                if (Constants.covid_redirection == 1) {
                    pager.setCurrentItem(0);
                    Constants.covid_redirection = 0;
                } else if (Constants.covidfrag_flag.equalsIgnoreCase("1")) {
                    pager.setCurrentItem(0);
                    Constants.covidfrag_flag = "0";
                } else {
                    pager.setCurrentItem(2);
                }

                if (Constants.covidwoe_flag.equalsIgnoreCase("1")) {
                    Constants.covidwoe_flag = "0";
                }

                if (Constants.offline_flag.equalsIgnoreCase("1")) {
                    pager.setCurrentItem(3);
                    Constants.offline_flag = "0";
                }

                if (Constants.universal == 1) {
                    if (Constants.ratfrag_flag.equalsIgnoreCase("1")) {
                        pager.setCurrentItem(1);
                        Constants.ratfrag_flag = "1";
                        Constants.universal = 0;
                    }
                } else {
                    if (Constants.pushrat_flag == 1) {
                        if (Constants.ratfrag_flag.equalsIgnoreCase("1")) {
                            pager.setCurrentItem(1);
                            Constants.ratfrag_flag = "1";
                            Constants.pushrat_flag = 0;
                        }
                    }
                }

            } else {

                if (pager != null && pager.getCurrentItem() != positionInt) {
                    pager.setCurrentItem(positionInt);
                } else {
                    pager.setCurrentItem(0);
                }

                if (Constants.offline_flag.equalsIgnoreCase("1")) {
                    pager.setCurrentItem(1);
                    Constants.offline_flag = "0";
                }
            }
        }
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = null;
        try {
            {
                if (CLIENT_TYPE.equalsIgnoreCase(Constants.NHF)) {
                    currentFragment = (OnBackPressListener) nhf_pageradapter.getRegisteredFragment(pager.getCurrentItem());
                } else {
                    if (access.equalsIgnoreCase(Constants.STAFF)) {

                        if (covidacc) {
                            currentFragment = (OnBackPressListener) covidadapter.getRegisteredFragment(pager.getCurrentItem());
                        } else {
                            currentFragment = (OnBackPressListener) adapterStafff.getRegisteredFragment(pager.getCurrentItem());
                        }

                    } else {

                        if (covidacc) {
                            currentFragment = (OnBackPressListener) adminCovidAdapter.getRegisteredFragment(pager.getCurrentItem());
                        } else {
                            currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());
                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            boolean a = currentFragment.onBackPressed();
            if (!a) {
                if (pager != null && pager.getCurrentItem() != 0) {
                    pager.setCurrentItem(0);
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}