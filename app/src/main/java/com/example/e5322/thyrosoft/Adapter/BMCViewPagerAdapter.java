package com.example.e5322.thyrosoft.Adapter;

import android.content.res.Resources;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.e5322.thyrosoft.Cliso_BMC.BMC_NEW_WOEFragment;
import com.example.e5322.thyrosoft.Fragment.CHNfragment;
import com.example.e5322.thyrosoft.Fragment.FilterReport;
import com.example.e5322.thyrosoft.R;

public class BMCViewPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public BMCViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new BMC_NEW_WOEFragment();
                break;
            case 1:
                result = new FilterReport();
                break;
            case 2:
                result = new CHNfragment();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {

            case 0:
                return resources.getString(R.string.page_1);
            case 1:
                return resources.getString(R.string.page_4);
            case 2:
                return resources.getString(R.string.page_7);

            default:
                return null;
        }
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