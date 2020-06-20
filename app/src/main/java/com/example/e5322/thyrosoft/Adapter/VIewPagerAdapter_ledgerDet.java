package com.example.e5322.thyrosoft.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.e5322.thyrosoft.Fragment.FirstFragment;
import com.example.e5322.thyrosoft.Fragment.SecondFragment;
import com.example.e5322.thyrosoft.Fragment.ThirdFragment;

/**
 * Created by e5426@thyrocare.com on 25/5/18.
 */

public class VIewPagerAdapter_ledgerDet extends FragmentPagerAdapter {
    FragmentManager fm;

    public VIewPagerAdapter_ledgerDet(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment oldFragment = fm.findFragmentByTag("FirstFragment");
                if (oldFragment!= null && oldFragment.isAdded()) {
                    fm.beginTransaction().remove(oldFragment).commit();
                }

               return FirstFragment.newInstance("FirstFragment, Instance 1");
            case 1:
               // return SecondFragment.newInstance("SecondFragment, Instance 1");
                return SecondFragment.newInstance("SecondFragment, Instance 1");
            case 2:

                return ThirdFragment.newInstance("ThirdFragment, Instance 1");
        }
        return SecondFragment.newInstance("SecondFragment, Instance 1");

    }    @Override
    public int getCount() {
        return 3;
    }
}
