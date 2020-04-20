package com.example.e5322.thyrosoft.ViewModel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.e5322.thyrosoft.Activity.VerticalViewPager;
import com.example.e5322.thyrosoft.Adapter.HealthTipsPagerAdapter;
import com.example.e5322.thyrosoft.Models.HealthTipsApiResponseModel;
import com.example.e5322.thyrosoft.Repository.HealthRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By kalpesh Borane
 */
public class HealthviewModel extends AndroidViewModel {

    MutableLiveData<List<HealthTipsApiResponseModel.HArt>> listMutableLiveData;
    HealthRepository healthRepository;
    List<HealthTipsApiResponseModel.HArt> HealthTipsArrylst = new ArrayList<>();
    Context context;
    VerticalViewPager healthTipViewpager;

    /**
     * Constructor
     */
    public HealthviewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Below Method is for Health Article Activity
     */
    public MutableLiveData<List<HealthTipsApiResponseModel.HArt>> getData(Context context, VerticalViewPager healthTipViewpager) {
        if (this.listMutableLiveData == null) {
            this.context = context;
            this.healthTipViewpager = healthTipViewpager;
            listMutableLiveData = new MutableLiveData<>();
            healthRepository = new HealthRepository();
            listMutableLiveData = healthRepository.getHealthrepositoy(context);

        }
        return this.listMutableLiveData;
    }

    /**
     * Set List Data
     */
    public void setListdata(List<HealthTipsApiResponseModel.HArt> healthTipsApiResponseModel) {
        if (healthTipsApiResponseModel != null && healthTipsApiResponseModel.size() > 0) {
            HealthTipsArrylst = new ArrayList<>();
            for (int i = 0; i < healthTipsApiResponseModel.size(); i++) {
                HealthTipsApiResponseModel.HArt model = new HealthTipsApiResponseModel.HArt();
                model.setArtical_Name(healthTipsApiResponseModel.get(i).getArtical_Name());
                model.setArtical_link(healthTipsApiResponseModel.get(i).getArtical_link());
                model.setImageurl(healthTipsApiResponseModel.get(i).getImageurl());
                model.setTeaser(healthTipsApiResponseModel.get(i).getTeaser());
                HealthTipsArrylst.add(model);
            }
        }

        if (HealthTipsArrylst.size() > 0) {
            SetViewpager();
        }
    }


    private void SetViewpager() {

        if (HealthTipsArrylst != null && HealthTipsArrylst.size() > 0) {
            healthTipViewpager.setAdapter(new HealthTipsPagerAdapter(context, HealthTipsArrylst));
            healthTipViewpager.setCurrentItem(0, true);
            try {
                healthTipViewpager.setPageTransformer(true, new ViewPager.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        if (position < -1) {
                            page.setAlpha(0);

                        } else if (position <= 0) {    // [-1,0]
                            page.setAlpha(1);
                            page.setTranslationX(0);
                            page.setScaleX(1);
                            page.setScaleY(1);
                        } else if (position <= 1) {    // (0,1]
                            page.setTranslationX(-position * page.getWidth() / 50);
                            page.setAlpha(1 - Math.abs(position));
                            page.setScaleX(1 - Math.abs(position));
                            page.setScaleY(1 - Math.abs(position));
                        } else {
                            page.setAlpha(0);

                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
