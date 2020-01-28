package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.e5322.thyrosoft.API.Api
import com.example.e5322.thyrosoft.API.Global
import com.example.e5322.thyrosoft.Adapter.ExpandableListAdapter_FAQ
import com.example.e5322.thyrosoft.FAQ_Main_Model.FAQ_Model
import com.example.e5322.thyrosoft.FAQ_Main_Model.FAQandANSArray
import com.example.e5322.thyrosoft.Fragment.FAQ_Fragment
import com.example.e5322.thyrosoft.GlobalClass
import com.example.e5322.thyrosoft.Kotlin.KTGlobalclass
import com.example.e5322.thyrosoft.Kotlin.KTModels.FAQSpinmodel
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTAPIInteface
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTRetrofitClient
import com.example.e5322.thyrosoft.R
import com.example.e5322.thyrosoft.ToastFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.activity_faq_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.AdapterView as AdapterView1

class FAQ_activity : AppCompatActivity() {
    var type_spinner_value: ArrayList<String>? = null
    var add: ImageView? = null
    //  var back: ImageView? = null
    // var home: android.widget.ImageView? = null
    var viewfab: View? = null
    var faq_model: FAQ_Model? = null
    var expandableListAdapter: ExpandableListAdapter_FAQ? = null
    var viewMain: View? = null
    var view: View? = null
    //var expandable_list_faq: ExpandableListView? = null
    //  var category_spinner: Spinner? = null
    var user: String? = null
    var passwrd: kotlin.String? = null
    var access: kotlin.String? = null
    var api_key: kotlin.String? = null
    var client_type: kotlin.String? = null
    var barProgressDialog: ProgressDialog? = null
    private val requestQueueSpinner_value: RequestQueue? = null
    private var requestQueue_FAQ: com.android.volley.RequestQueue? = null
    private var prefs: SharedPreferences? = null
    private val mListener: FAQ_Fragment.OnFragmentInteractionListener? = null
    private val TAG = javaClass.simpleName
    private val globalClass: Global? = null


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_)

        initview()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun initview() {
        back!!.setOnClickListener {
            finish()
        }

        home!!.setOnClickListener { KTGlobalclass().goToHome(this@FAQ_activity) }

        prefs = getSharedPreferences("Userdetails", Context.MODE_PRIVATE)
        user = prefs!!.getString("Username", null)
        passwrd = prefs!!.getString("password", null)
        access = prefs!!.getString("ACCESS_TYPE", null)
        api_key = prefs!!.getString("API_KEY", null)
        client_type = prefs!!.getString("CLIENT_TYPE", null)

        if (Global.checkForApi21()) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.limaroon)
        }

        val sharedPrefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@FAQ_activity)
        val gson = Gson()
        val json = sharedPrefs.getString(TAG, "")
        val type = object : TypeToken<List<String?>?>() {}.type
        val arrayList = gson.fromJson<List<String>>(json, type)
        type_spinner_value = ArrayList()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        faq_list_expandable.setIndicatorBounds(width - GetPixelFromDips(50f), width - GetPixelFromDips(10f))


        if (arrayList != null) {
            for (i in arrayList.indices) {
                type_spinner_value!!.add(arrayList[i])
            }
            val type_value_spinner: ArrayAdapter<*> = ArrayAdapter<Any?>(this@FAQ_activity, R.layout.spinnerproperty, type_spinner_value as List<Any?>)
            category_spinner.adapter = type_value_spinner
            getAll_FAQ_data()
        } else {
            getSpinnerdata()
        }


        category_spinner.onItemSelectedListener = object : AdapterView1.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView1<*>?) {

            }

            override fun onItemSelected(parent: AdapterView1<*>?, view: View?, position: Int, id: Long) {
                getAll_FAQ_data()
            }

        }

    }

    private fun getSpinnerdata() {
        barProgressDialog = KTGlobalclass().showProgressDialog(this@FAQ_activity)
        val url = Api.static_pages_link + client_type + "/"
        Log.e(TAG, "SPINNER URL ----->" + url)
        val KTinterface: KTAPIInteface = KTRetrofitClient().getInstance()!!.getClient(url)!!.create(KTAPIInteface::class.java)

        val responseCall: Call<FAQSpinmodel?>? = KTinterface.getFAQspin()

        responseCall!!.enqueue(object : Callback<FAQSpinmodel?> {

            override fun onResponse(call: Call<FAQSpinmodel?>, response: Response<FAQSpinmodel?>) {
                val responseModel = response.body()

                if (responseModel != null) {
                    if (responseModel!!.response.equals("Success", ignoreCase = true)) {

                        if (responseModel.FAQ_type_spinnner_names != null && responseModel.FAQ_type_spinnner_names.size > 0) {

                            Log.e(TAG, "SUCCESS RESPONSE -->")
                            KTGlobalclass().hideProgress(this@FAQ_activity, barProgressDialog)

                            type_spinner_value = ArrayList()
                            for (i in responseModel.FAQ_type_spinnner_names.indices) {
                                type_spinner_value!!.add(responseModel.FAQ_type_spinnner_names[i])
                            }

                            val type_value_spinner: ArrayAdapter<*> = ArrayAdapter<Any?>(this@FAQ_activity, R.layout.spinnerproperty, type_spinner_value as List<Any?>)
                            category_spinner.adapter = type_value_spinner

                            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this@FAQ_activity)
                            val editor = sharedPrefs.edit()
                            val gson = Gson()
                            val json = gson.toJson(type_spinner_value)
                            editor.putString(TAG, json)
                            editor.apply()

                            getAll_FAQ_data()
                        } else {
                            Toast.makeText(this@FAQ_activity, responseModel.response, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    barProgressDialog!!.dismiss()
                }

            }

            override fun onFailure(call: Call<FAQSpinmodel?>, t: Throwable) {
                Log.e(TAG, "on error " + t.localizedMessage)
            }

        })
    }

    fun GetPixelFromDips(pixels: Float): Int { // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }

    fun getAll_FAQ_data() {
        val getSpinner_value = category_spinner.selectedItem.toString()
        requestQueue_FAQ = Volley.newRequestQueue(this@FAQ_activity)
        val jsonObjectRequestFAQ = JsonObjectRequest(Request.Method.GET, Api.static_pages_link + client_type + "/" + getSpinner_value + "/GetAllFAQ", com.android.volley.Response.Listener { response ->
            Log.e(TAG, "onResponse: response$response")
            val gson = Gson()
            faq_model = gson.fromJson(response.toString(), FAQ_Model::class.java)
            val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this@FAQ_activity)
            val prefsEditor1 = appSharedPrefs.edit()
            val gson22 = Gson()
            val json22 = gson22.toJson(faq_model)
            prefsEditor1.putString("FAQ_DATA", json22)
            prefsEditor1.apply()
            if (faq_model != null) {
                if (faq_model!!.response == "Success") {
                    if (faq_model!!.faQandANSArray != null) {
                        val faq_list = java.util.ArrayList<FAQandANSArray>()
                        for (i in faq_model!!.faQandANSArray.indices) {
                            faq_list.add(faq_model!!.faQandANSArray[i])
                        }
                        expandableListAdapter = ExpandableListAdapter_FAQ(faq_list, this@FAQ_activity)
                        faq_list_expandable.setAdapter(expandableListAdapter)
                    }
                }
            } else {
                Toast.makeText(this@FAQ_activity, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        }, com.android.volley.Response.ErrorListener { error ->
            if (error.networkResponse == null) {
                if (error.javaClass == TimeoutError::class.java) {
                    TastyToast.makeText(this@FAQ_activity, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                }
            }
        })
        GlobalClass.volleyRetryPolicy(jsonObjectRequestFAQ)
        requestQueue_FAQ!!.add(jsonObjectRequestFAQ)
        Log.e(TAG, "getAll_FAQ_data: URL$jsonObjectRequestFAQ")
    }

}