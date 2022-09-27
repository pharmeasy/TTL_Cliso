package com.example.e5322.thyrosoft.ClisoInterfaces;

import com.example.e5322.thyrosoft.Models.GetProductsRecommendedResModel;

public class AppInterfaces {
    public interface OnClickRecoTestListner {
        void onchecked(GetProductsRecommendedResModel.ProductListDTO listDTOS, boolean isChecked, boolean isMainChecked);
    }

}


