package com.example.e5322.thyrosoft;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Models.GetProductsRecommendedResModel;
import com.example.e5322.thyrosoft.SqliteDb.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class CleverTapHelper {
    Activity mActivity;
    SharedPreferences pref;
    DatabaseHelper databaseHelper;
    ArrayList<GetProductsRecommendedResModel.ProductListDTO> recoList;


    public CleverTapHelper(Activity mActivity) {
        this.mActivity = mActivity;
        try {
            pref = mActivity.getSharedPreferences("savePatientDetails", Context.MODE_PRIVATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void woeSubmitEvent(String usercode, String category, String test, String timestamp) {
        try {
            HashMap<String, Object> woeSubmitData = new HashMap<>();
            woeSubmitData.put("Source_code", usercode);
            woeSubmitData.put("Category", category);
            woeSubmitData.put("Product", test);
            woeSubmitData.put("TimeStamp", timestamp);
            woeSubmitData.put("Tenant", timestamp);
            woeSubmitData.put("Platform", Constants.CLISO_ANDROID_APP);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("WOE_SUBMIT", woeSubmitData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void recoShown_Event(String header, String patiendetails, String temp_wo_id, String cartList, String baseTest, String targetTest, String baseB2BRate, String baseB2CRate, String targetB2BRate, String targetB2CRate,boolean isRecoShown) {

        try {
            databaseHelper = new DatabaseHelper(mActivity);
            recoList = new ArrayList<>();
            HashMap<String, Object> recoShownData = new HashMap<>();
            recoShownData.put("Header", header);
            recoShownData.put("PatientDetails", patiendetails);
            recoShownData.put("Temp_wo_id", temp_wo_id);
            recoShownData.put("CartList", cartList);
            recoShownData.put("Base_Test", baseTest);
            recoShownData.put("Target_Test", targetTest);
            recoShownData.put("Base_B2BRate", baseB2BRate);
            recoShownData.put("Base_B2CRate", baseB2CRate);
            recoShownData.put("Target_B2BRate", targetB2BRate);
            recoShownData.put("Target_B2CRate", targetB2CRate);
            recoShownData.put("RecoShown", isRecoShown);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("RECO_SHOWN", recoShownData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void reconextclick_Event(String header, String patientDetails, String randomId, String cartList, boolean recoSelected, String basetest, String targetTest, String baseb2bRate, String targetb2bRate, String baseb2cRate, String targetb2cRate) {
        try {
            HashMap<String, Object> recoselectedData = new HashMap<>();
            recoselectedData.put("Header ", header);
            recoselectedData.put("PatienDetails ", patientDetails);
            recoselectedData.put("Temp_wo_Id ", randomId);
            recoselectedData.put("CartList ", cartList);
            recoselectedData.put("Reco_selected ", recoSelected);
            recoselectedData.put("BaseTest", basetest);
            recoselectedData.put("TargetTest", targetTest);
            recoselectedData.put("Base_B2BRate", baseb2bRate);
            recoselectedData.put("Base_B2CRate", baseb2cRate);
            recoselectedData.put("Target_B2BRate", targetb2bRate);
            recoselectedData.put("Target_B2CRate", targetb2cRate);
            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("RECO_NEXT_CLICK", recoselectedData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void woeFailureEvent(String message, String status, String barcode_id, String tests) {
        try {
            HashMap<String, Object> woeFailureData = new HashMap<>();
            woeFailureData.put("message", message);
            woeFailureData.put("Status", status);
            woeFailureData.put("Barcode_Id", barcode_id);
            woeFailureData.put("Tests", tests);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("WOE_FAILURE", woeFailureData);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void NullSearchEvent(String header, String patientdetails, String Temp_wo_id, String search_term, int searchTermCount) {
        try {
            HashMap<String, Object> nullSearchData = new HashMap<>();
            nullSearchData.put("Header", header);
            nullSearchData.put("PatientDetails", patientdetails);
            nullSearchData.put("Temp_wo_id", Temp_wo_id);
            nullSearchData.put("Search_Term", search_term);
            nullSearchData.put("SearchTermCount", searchTermCount);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("NULL_SEARCH", nullSearchData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SearchPageLoadEvent(String header, String patientDetails, String Temp_wo_id) {
        try {
            HashMap<String, Object> searchPageLoadData = new HashMap<>();
            searchPageLoadData.put("Header", header);
            searchPageLoadData.put("PatientDetails", patientDetails);
            searchPageLoadData.put("Temp_wo_id", Temp_wo_id);


            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("SEARCH_PAGE_LOAD", searchPageLoadData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void woeSubmitEvent(String header, String patientDetails, String Temp_wo_id, String cartList, String barcodes, String recoCount, String recoSelectedCount, String recoShownTest, String recoSelectedTest) {
        try {
            HashMap<String, Object> woeSubmitData = new HashMap<>();
            woeSubmitData.put("Header", header);
            woeSubmitData.put("PatientDetails", patientDetails);
            woeSubmitData.put("Temp_wo_id", Temp_wo_id);
            woeSubmitData.put("CartList", cartList);
            woeSubmitData.put("Barcodes", barcodes);
            woeSubmitData.put("Reco_count", recoCount);
            woeSubmitData.put("Reco_Selected_Count", recoSelectedCount);
            woeSubmitData.put("Reco_Shown_Test", recoShownTest);
            woeSubmitData.put("Reco_Selected_Test", recoSelectedTest);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("WOE_SUBMIT", woeSubmitData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void woeSubmitSuccessEvent(String header, String patientDetails, String Temp_wo_id, String cartList, String barcodes, String isSuccessful, String amountCollected, String errorMsg, String recoCount, String recoSelectedCount, String recoShownTest, String recoSelectedTest) {
        try {
            HashMap<String, Object> woeSubmitSuccessData = new HashMap<>();


            woeSubmitSuccessData.put("Header", header);
            woeSubmitSuccessData.put("PatientDetails", patientDetails);
            woeSubmitSuccessData.put("Temp_wo_id", Temp_wo_id);
            woeSubmitSuccessData.put("CartList", cartList);
            woeSubmitSuccessData.put("Barcodes", barcodes);
            woeSubmitSuccessData.put("isSuccessful", isSuccessful);
            woeSubmitSuccessData.put("AmountCollected", amountCollected);
            woeSubmitSuccessData.put("ErrorMsg", errorMsg);
            woeSubmitSuccessData.put("Reco_count", recoCount);
            woeSubmitSuccessData.put("Reco_Selected_Count", recoSelectedCount);
            woeSubmitSuccessData.put("Reco_Shown_Test", recoShownTest);
            woeSubmitSuccessData.put("Reco_Selected_Test", recoSelectedTest);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("WOE_SUBMIT_SUCCESS", woeSubmitSuccessData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void skuCheckedEvent(String header, String patientDetails, String Temp_wo_id, String cartList, String itemChecked) {
        try {
            HashMap<String, Object> checkedItemData = new HashMap<>();
            checkedItemData.put("Header", header);
            checkedItemData.put("PatientDetails", patientDetails);
            checkedItemData.put("Temp_wo_id", Temp_wo_id);
            checkedItemData.put("CartList", cartList);
            checkedItemData.put("Item_Checked", itemChecked);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("SKU_CHECKED", checkedItemData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void skuUncheckedEvent(String header, String patientDetails, String Temp_wo_id, String cartList, String itemUnchecked) {
        try {
            HashMap<String, Object> UNcheckedItemData = new HashMap<>();
            UNcheckedItemData.put("Header", header);
            UNcheckedItemData.put("PatientDetails", patientDetails);
            UNcheckedItemData.put("Temp_wo_id", Temp_wo_id);
            UNcheckedItemData.put("CartList", cartList);
            UNcheckedItemData.put("Item_Unchecked", itemUnchecked);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("SKU_UNCHECKED", UNcheckedItemData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void submitPageRedirectEvent(String header, String patientDetails, String Temp_wo_id, String cartList) {
        try {
            HashMap<String, Object> submitPageRedirectionData = new HashMap<>();
            submitPageRedirectionData.put("Header", header);
            submitPageRedirectionData.put("PatientDetails", patientDetails);
            submitPageRedirectionData.put("Temp_wo_id", Temp_wo_id);
            submitPageRedirectionData.put("CartList", cartList);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("SUBMIT_PAGE_REDIRECTION", submitPageRedirectionData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void barcodeScanEvent(String header, String patientDetails, String Temp_wo_id, String cartList, String vialType, String scanMode) {
        try {
            HashMap<String, Object> barcodeScanData = new HashMap<>();
            barcodeScanData.put("Header", header);
            barcodeScanData.put("PatientDetails", patientDetails);
            barcodeScanData.put("Temp_wo_id", Temp_wo_id);
            barcodeScanData.put("CartList", cartList);
            barcodeScanData.put("VialType", vialType);
            barcodeScanData.put("ScanMode", scanMode);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("BARCODE_SCAN", barcodeScanData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void barcodeScanSuccessEvent(String header, String patientDetails, String Temp_wo_id, String cartList, String vialType, String scanMode, String barcode, String response1) {
        try {
            HashMap<String, Object> barcodeScanSuccessData = new HashMap<>();
            barcodeScanSuccessData.put("Header", header);
            barcodeScanSuccessData.put("PatientDetails", patientDetails);
            barcodeScanSuccessData.put("Temp_wo_id", Temp_wo_id);
            barcodeScanSuccessData.put("CartList", cartList);
            barcodeScanSuccessData.put("VialType", vialType);
            barcodeScanSuccessData.put("ScanMode", scanMode);
            barcodeScanSuccessData.put("Barcode", barcode);
            barcodeScanSuccessData.put("SuccessMsg", response1);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("BARCODE_SCAN_SUCCESS", barcodeScanSuccessData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void barcodeVerifyPopup(String header, String patientDetails, String Temp_wo_id, String cartList, String barcodes) {
        try {
            HashMap<String, Object> barcodeVerifyData = new HashMap<>();
            barcodeVerifyData.put("Header", header);
            barcodeVerifyData.put("PatientDetails", patientDetails);
            barcodeVerifyData.put("Temp_wo_id", Temp_wo_id);
            barcodeVerifyData.put("CartList", cartList);
            barcodeVerifyData.put("Barcodes", barcodes);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("BARCODE_VERIFY_POP", barcodeVerifyData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void rechargeStartEvent(String header, String paymentGateway, String amount, String availableBalance, String creditLine) {
        try {
            HashMap<String, Object> barcodeVerifyData = new HashMap<>();
            barcodeVerifyData.put("Header", header);
            barcodeVerifyData.put("PaymentGateway", paymentGateway);
            barcodeVerifyData.put("Amount", amount);
            barcodeVerifyData.put("AvailableBalance", availableBalance);
            barcodeVerifyData.put("CreditLine", creditLine);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("RECHARGE_START", barcodeVerifyData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void rechargeSuccessEvent(String header, String paymentGateway, String amount, String availableBalance, String creditLine, String orderid, boolean isSuccessful, String ErrorMsg) {
        try {
            HashMap<String, Object> barcodeVerifyData = new HashMap<>();
            barcodeVerifyData.put("Header", header);
            barcodeVerifyData.put("PaymentGateway", paymentGateway);
            barcodeVerifyData.put("Amount", amount);
            barcodeVerifyData.put("AvailableBalance", availableBalance);
            barcodeVerifyData.put("CreditLine", creditLine);
            barcodeVerifyData.put("Order_ID", orderid);
            barcodeVerifyData.put("IsSuccessful", isSuccessful);
            barcodeVerifyData.put("ErrorMsg", ErrorMsg);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("RECHARGE_SUCCESS", barcodeVerifyData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void reportDownloadEvent(String header, String PatientDetails, String status, String latency, String errorMsg, String downloadFrom, String window) {
        try {
            HashMap<String, Object> barcodeVerifyData = new HashMap<>();
            barcodeVerifyData.put("Header", header);
            barcodeVerifyData.put("PatientDetails", PatientDetails);
            barcodeVerifyData.put("Status", status);
            barcodeVerifyData.put("latency", latency);
            barcodeVerifyData.put("ErrorMsg", errorMsg);
            barcodeVerifyData.put("DownloadFrom", downloadFrom);
            barcodeVerifyData.put("Window", window);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("REPORT_DOWNLOAD", barcodeVerifyData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void NovidPageLoadEvent(String header, String comingFrom) {
        try {
            HashMap<String, Object> barcodeVerifyData = new HashMap<>();
            barcodeVerifyData.put("Header", header);
            barcodeVerifyData.put("Coming_From", comingFrom);

            if (Constants.clevertapDefaultInstance != null) {
                Constants.clevertapDefaultInstance.pushEvent("NOVID_PAGE_LOAD", barcodeVerifyData);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}