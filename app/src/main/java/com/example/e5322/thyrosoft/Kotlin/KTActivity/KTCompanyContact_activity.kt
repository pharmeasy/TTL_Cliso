package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.e5322.thyrosoft.API.Api
import com.example.e5322.thyrosoft.API.Global
import com.example.e5322.thyrosoft.Kotlin.KTAdapter.KtCompanyAdapter
import com.example.e5322.thyrosoft.Kotlin.KTGlobalclass
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTCompanydt_Response
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTAPIInteface
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTRetrofitClient
import com.example.e5322.thyrosoft.R
import kotlinx.android.synthetic.main.fragment_contact_list_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class KTCompanyContact_activity : AppCompatActivity(), View.OnClickListener {

    var passSpinner_value: String? = null
    var TAG: String? = javaClass.simpleName
    var activty: Activity? =null;
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_contact_list_fragment)
        activty=this@KTCompanyContact_activity;
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun initView() {
        back.setOnClickListener(this)
        home.setOnClickListener(this)

        var linearLayoutManager = LinearLayoutManager(this@KTCompanyContact_activity)
        contact_list.layoutManager = linearLayoutManager
        var company_spinner = ArrayAdapter.createFromResource(activty, R.array.company_contact_spinner_values, R.layout.spinnerproperty)
        contact_type_spinner.adapter = company_spinner

        if (Global.checkForApi21()) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.limaroon)
        }


        contact_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                getCompany_contact_details()
            }

        }
    }

    fun getCompany_contact_details() {

        var getSpinnertype = contact_type_spinner.selectedItem.toString()

        passSpinner_value = if (getSpinnertype.equals("STATE OFFICER", true))
            "STATE OFFICER"
        else
            getSpinnertype

        var progressDialog = KTGlobalclass().ShowprogressDialog(this@KTCompanyContact_activity)

        var apiInteface: KTAPIInteface = KTRetrofitClient().getInstance()!!.getClient(activty, Api.static_pages_link + passSpinner_value + "/")!!.create(KTAPIInteface::class.java)
        var callcontactlist: Call<KTCompanydt_Response> = apiInteface.getnoticedetail();

        //Log.e(TAG, "Company contact URL---->" + callcontactlist.request().url())


        callcontactlist.enqueue(object : Callback<KTCompanydt_Response> {

            override fun onFailure(call: Call<KTCompanydt_Response>, t: Throwable) {
               // Log.e(TAG, "On faillure--->" + t.localizedMessage)
                KTGlobalclass().hideProgress(this@KTCompanyContact_activity, progressDialog)
            }

            override fun onResponse(call: Call<KTCompanydt_Response>, response: Response<KTCompanydt_Response>) {
                try {
                    if (response.body()!!.response.equals("Success", true)) {
                        KTGlobalclass().hideProgress(this@KTCompanyContact_activity, progressDialog)
                        var company_adapter = KtCompanyAdapter(this@KTCompanyContact_activity, response.body()!!.Contact_Array_list as ArrayList<KTCompanydt_Response.ContactArray>)
                        contact_list.adapter = company_adapter
                        company_adapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    KTGlobalclass().hideProgress(this@KTCompanyContact_activity, progressDialog)
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.back -> {
                finish()
            }

            R.id.home -> {
                KTGlobalclass().goToHome(this@KTCompanyContact_activity)
            }
        }
    }
}