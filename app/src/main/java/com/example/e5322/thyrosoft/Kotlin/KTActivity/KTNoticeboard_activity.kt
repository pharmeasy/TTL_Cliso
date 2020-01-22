package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.example.e5322.thyrosoft.API.Api
import com.example.e5322.thyrosoft.API.Constants
import com.example.e5322.thyrosoft.API.Global
import com.example.e5322.thyrosoft.Kotlin.KTAdapter.Noticeboard_Adapter
import com.example.e5322.thyrosoft.Kotlin.KTGlobalclass
import com.example.e5322.thyrosoft.Kotlin.KTModels.Noticeboardmodel
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTAPIInteface
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTRetrofitClient
import com.example.e5322.thyrosoft.R
import kotlinx.android.synthetic.main.fragment_noticeboard_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class KTNoticeboard_activity : AppCompatActivity() {

    var view: View? = null
    var msgCode: String? = null
    var prefs: SharedPreferences? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var user: String? = null
    var passwrd: kotlin.String? = null
    var access: kotlin.String? = null
    var api_key: kotlin.String? = null
    //  var lin_cmsoon: LinearLayout? = null
    private val TAG = javaClass.simpleName
    private var CLIENT_TYPE: kotlin.String? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_noticeboard_)

        initView()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {

        linearLayoutManager = LinearLayoutManager(this@KTNoticeboard_activity)
        noticeboard_list.layoutManager = linearLayoutManager

        back.setOnClickListener { finish() }
        home.setOnClickListener { KTGlobalclass().goToHome(this@KTNoticeboard_activity) }

        if (Global.checkForApi21()) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.limaroon)
        }

        /*TODO Storing the data into sharedpreference */
        prefs = getSharedPreferences("Userdetails", Context.MODE_PRIVATE)
        user = prefs!!.getString("Username", null)
        passwrd = prefs!!.getString("password", null)
        access = prefs!!.getString("ACCESS_TYPE", null)
        api_key = prefs!!.getString("API_KEY", null)
        CLIENT_TYPE = prefs!!.getString("CLIENT_TYPE", null)

        if (!KTGlobalclass().isNetworkAvailable(this@KTNoticeboard_activity)) {
            offline_img.visibility = View.VISIBLE
            noticeboard_list.visibility = View.GONE
        } else {
            Log.e(TAG, "CLIENT_TYPE: $CLIENT_TYPE")
            if (CLIENT_TYPE.equals(Constants.NHF, ignoreCase = true)) {
                lin_cmsoon.visibility = View.VISIBLE
            } else {
                lin_cmsoon.visibility = View.GONE
            }

            getNoticeBoardData()
            offline_img.visibility = View.GONE
            noticeboard_list.visibility = View.VISIBLE
        }

    }

    fun getNoticeBoardData() {
        var progressDialog = KTGlobalclass().showProgressDialog(this@KTNoticeboard_activity)
        val strurl = Api.NoticeBoardData + "" + api_key + "/"
        val ktinterface = KTRetrofitClient().getInstance()!!.getClient(strurl)!!.create(KTAPIInteface::class.java)

        val responsecall: Call<Noticeboardmodel?> = ktinterface.getNoticeboard()

        responsecall.enqueue(object : Callback<Noticeboardmodel?> {
            override fun onFailure(call: Call<Noticeboardmodel?>, t: Throwable) {
                Log.e(TAG, "on error --->" + t.localizedMessage)
            }

            override fun onResponse(call: Call<Noticeboardmodel?>, response: Response<Noticeboardmodel?>) {
                KTGlobalclass().hideProgress(this@KTNoticeboard_activity, progressDialog)
                val noticeBoard_model = response.body()
                Log.e(TAG, "on Sucess --->" + noticeBoard_model!!.resId)
                if (noticeBoard_model != null) {
                    if (!KTGlobalclass().isNull(noticeBoard_model.response) && noticeBoard_model.response.equals(Constants.caps_invalidApikey, ignoreCase = true)) {
                        KTGlobalclass().redirectToLogin(this@KTNoticeboard_activity)
                    } else {
                        val array_notice = ArrayList<Noticeboardmodel>()
                        if (noticeBoard_model.messages != null) {
                            array_notice.add(noticeBoard_model)
                            if (array_notice[0].messages[0].messageCode != null) {
                                msgCode = array_notice[0].messages[0].messageCode
                                 val noticeBoard_adapter=Noticeboard_Adapter(this@KTNoticeboard_activity,array_notice,msgCode)
                            }

                        }
                    }
                }

            }

        })
    }
}