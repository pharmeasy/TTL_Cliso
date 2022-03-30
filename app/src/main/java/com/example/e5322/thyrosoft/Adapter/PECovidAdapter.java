package com.example.e5322.thyrosoft.Adapter;

import android.content.res.Resources;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.e5322.thyrosoft.Activity.CovidReg_Activity;
import com.example.e5322.thyrosoft.Activity.RATWOEActivity;
import com.example.e5322.thyrosoft.Fragment.CHNfragment;
import com.example.e5322.thyrosoft.Fragment.FilterReport;
import com.example.e5322.thyrosoft.Fragment.RateCalculatorFragment;
import com.example.e5322.thyrosoft.Fragment.Start_New_Woe;
import com.example.e5322.thyrosoft.Fragment.TrackDetails;
import com.example.e5322.thyrosoft.Fragment.Wind_up_fragment;
import com.example.e5322.thyrosoft.R;

public class PECovidAdapter extends FragmentPagerAdapter {

    private final Resources resources;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public PECovidAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        final Fragment result;
        switch (i) {
            case 0:
                result = new CovidReg_Activity();
                break;
            case 1:
                result = new RATWOEActivity();
                break;
            case 2:
                result = new Start_New_Woe();
                break;
            case 3:
                result = new RateCalculatorFragment();
                break;
            case 4:
                result = new TrackDetails();
                break;
            case 5:
                result = new FilterReport();
                break;
            case 6:
                result = new Wind_up_fragment();
                break;
            case 7:
                result = new CHNfragment();
                break;
            default:
                result = null;
                break;
        }

        return result;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.covid);
            case 1:
                return "RAT WOE";
            case 2:
                return resources.getString(R.string.page_1);
            case 3:
                return resources.getString(R.string.page_9);
            case 4:
                return resources.getString(R.string.page_3);
            case 5:
                return resources.getString(R.string.page_4);
            case 6:
                return resources.getString(R.string.page_6);
            case 7:
                return resources.getString(R.string.page_7);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
