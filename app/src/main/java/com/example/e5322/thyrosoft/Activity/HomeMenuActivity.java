package com.example.e5322.thyrosoft.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.e5322.thyrosoft.R;

public class HomeMenuActivity extends AppCompatActivity {

    private int positionInt;
    CarouselFragment carouselFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        try {
            if (getIntent() != null) {
                positionInt = getIntent().getIntExtra("position", 0);
            }
            redirectToFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectToFragment() {
        carouselFragment = new CarouselFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", positionInt);
        carouselFragment.setArguments(bundle);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, carouselFragment)
                .commit();
    }
}