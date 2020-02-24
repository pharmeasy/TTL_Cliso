package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.e5322.thyrosoft.API.Api
import com.example.e5322.thyrosoft.API.Constants
import com.example.e5322.thyrosoft.API.Global
import com.example.e5322.thyrosoft.Activity.MessageConstants
import com.example.e5322.thyrosoft.GlobalClass
import com.example.e5322.thyrosoft.Kotlin.KTAdapter.KTExpandableListAdapter_FAQ
import com.example.e5322.thyrosoft.Kotlin.KTGlobalclass
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTFAQandANSArray
import com.example.e5322.thyrosoft.Kotlin.KTModels.PromoterFaq
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTAPIInteface
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTRetrofitClient
import com.example.e5322.thyrosoft.R
import kotlinx.android.synthetic.main.activity_faq_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FAQ_activity : AppCompatActivity() {

    var type_spinner_value: ArrayList<String>? = null
    var user: String? = null
    var passwrd: kotlin.String? = null
    var access: kotlin.String? = null
    var api_key: kotlin.String? = null
    var client_type: kotlin.String? = null
    private val TAG = javaClass.simpleName
    var expandableListAdapter: KTExpandableListAdapter_FAQ? = null


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_)

        initview()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun initview() {

        back!!.setOnClickListener { finish() }

        home!!.setOnClickListener { KTGlobalclass().goToHome(this@FAQ_activity) }

        if (Global.checkForApi21()) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.limaroon)
        }


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        faq_list_expandable.setIndicatorBounds(width - GetPixelFromDips(50f), width - GetPixelFromDips(10f))


        if (KTGlobalclass().isNetworkAvailable(this@FAQ_activity)) {
            getAll_FAQ_data()
        } else {
            GlobalClass.toastyError(this@FAQ_activity, MessageConstants.CHECK_INTERNET_CONN, false)
        }

    }


    fun GetPixelFromDips(pixels: Float): Int { // Get the screen's density scale
        val scale = resources.displayMetrics.density
        return (pixels * scale + 0.5f).toInt()
    }

    fun getAll_FAQ_data() {

        val progressDialog = KTGlobalclass().ShowprogressDialog(this@FAQ_activity)
        val apiInteface = KTRetrofitClient().getInstance()!!.getClient(Api.FAQAPI + Constants.DAC + "/")!!.create(KTAPIInteface::class.java)
        val responsecall: Call<KTFAQandANSArray?> = apiInteface.getFAQ()
        Log.e(TAG, "FAQ URL ---->" + responsecall.request().url())

        responsecall.enqueue(object : Callback<KTFAQandANSArray?> {
            override fun onFailure(call: Call<KTFAQandANSArray?>, t: Throwable) {
                KTGlobalclass().hideProgress(this@FAQ_activity, progressDialog)

            }

            override fun onResponse(call: Call<KTFAQandANSArray?>, response: Response<KTFAQandANSArray?>) {
                try {

                    if (response.body()!!.RESPONSE.equals(Constants.SUCCESS, ignoreCase = true)) {
                        val faqmodel = response.body()
                        if (!faqmodel!!.promoterFaqList.isEmpty()) {
                            KTGlobalclass().hideProgress(this@FAQ_activity, progressDialog)
                            faq_list_expandable.visibility = View.VISIBLE
                            expandableListAdapter = KTExpandableListAdapter_FAQ(response.body()!!.promoterFaqList as java.util.ArrayList<PromoterFaq>, this@FAQ_activity)
                            faq_list_expandable.setAdapter(expandableListAdapter)
                        } else {
                            KTGlobalclass().hideProgress(this@FAQ_activity, progressDialog)
                            GlobalClass.toastyError(this@FAQ_activity, MessageConstants.NODATA, false)
                            faq_list_expandable.visibility = View.GONE
                        }

                    } else {
                        KTGlobalclass().hideProgress(this@FAQ_activity, progressDialog)
                    }
                } catch (e: Exception) {
                    KTGlobalclass().hideProgress(this@FAQ_activity, progressDialog)
                }

            }

        })


    }

}