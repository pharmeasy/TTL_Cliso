package com.example.e5322.thyrosoft.Activity.frags;

import android.support.v4.app.Fragment;

import com.example.e5322.thyrosoft.Activity.BackPressImpl;
import com.example.e5322.thyrosoft.Activity.OnBackPressListener;


/**
 * Created by shahabuddin on 6/6/14.
 */
public class RootFragment extends Fragment implements OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}
