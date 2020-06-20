package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.RequestQueue
import com.example.e5322.thyrosoft.API.Api
import com.example.e5322.thyrosoft.API.Constants
import com.example.e5322.thyrosoft.API.Global
import com.example.e5322.thyrosoft.GlobalClass
import com.example.e5322.thyrosoft.Interface.RefreshNoticeBoard
import com.example.e5322.thyrosoft.Kotlin.KTGlobalclass
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTAcknowledeResponse
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTAcknowledmentreq
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTAPIInteface
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTRetrofitClient
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model
import com.example.e5322.thyrosoft.R
import com.example.e5322.thyrosoft.ToastFile
import com.sdsmdg.tastytoast.TastyToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class KTNoticeboard_activity : AppCompatActivity() {
    var view: View? = null
    var noticeboard_list: RecyclerView? = null
    var back: ImageView? = null
    var home: android.widget.ImageView? = null
    var requestQueueNoticeBoard: RequestQueue? = null
    var prefs: SharedPreferences? = null
    var offline_img: LinearLayout? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var user: String? = null
    var passwrd: kotlin.String? = null
    var access: kotlin.String? = null
    var api_key: kotlin.String? = null
    var lin_cmsoon: LinearLayout? = null
    private val TAG = javaClass.simpleName
    private var CLIENT_TYPE: kotlin.String? = null
    private var PostQueOtp: RequestQueue? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_noticeboard_)
        noticeboard_list = findViewById(R.id.noticeboard_list)
        linearLayoutManager = LinearLayoutManager(this@KTNoticeboard_activity)
        noticeboard_list!!.setLayoutManager(linearLayoutManager)
        back = findViewById(R.id.back)
        offline_img = findViewById(R.id.offline_img)
        lin_cmsoon = findViewById(R.id.lin_cmsoon)
        home = findViewById(R.id.home)
        back!!.setOnClickListener(View.OnClickListener { finish() })
        home!!.setOnClickListener { GlobalClass.goToHome(this@KTNoticeboard_activity) }

        if (Global.checkForApi21()) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.limaroon)
        }
        prefs = getSharedPreferences("Userdetails", Context.MODE_PRIVATE)
        user = prefs!!.getString("Username", null)
        passwrd = prefs!!.getString("password", null)
        access = prefs!!.getString("ACCESS_TYPE", null)
        api_key = prefs!!.getString("API_KEY", null)
        CLIENT_TYPE = prefs!!.getString("CLIENT_TYPE", null)
        if (!GlobalClass.isNetworkAvailable(this@KTNoticeboard_activity)) {
            offline_img!!.setVisibility(View.VISIBLE)
            noticeboard_list!!.setVisibility(View.GONE)
        } else {
            Log.e(TAG, "CLIENT_TYPE: $CLIENT_TYPE")
            if (CLIENT_TYPE.equals(Constants.NHF, ignoreCase = true)) {
                lin_cmsoon!!.setVisibility(View.VISIBLE)
            } else {
                lin_cmsoon!!.setVisibility(View.GONE)
            }
            if (KTGlobalclass().isNetworkAvailable(this@KTNoticeboard_activity)) {
                getNoticeBoardData()
            } else {
                Toast.makeText(this@KTNoticeboard_activity, ToastFile.intConnection, Toast.LENGTH_SHORT).show()
            }
            offline_img!!.setVisibility(View.GONE)
            noticeboard_list!!.setVisibility(View.VISIBLE)
        }
    }


    fun getNoticeBoardData() {
        var progressDialog = KTGlobalclass().showProgressDialog(this@KTNoticeboard_activity)
        val strurl = Api.NoticeBoardData + "" + api_key + "/"
        val ktinterface = KTRetrofitClient().getInstance()!!.getClient(this@KTNoticeboard_activity, strurl)!!.create(KTAPIInteface::class.java)

        val responsecall: Call<NoticeBoard_Model?> = ktinterface.getNoticeboard()

       // Log.e(TAG, "NOTICE BOARD API ------>" + responsecall.request().url())

        responsecall.enqueue(object : Callback<NoticeBoard_Model?> {
            override fun onFailure(call: Call<NoticeBoard_Model?>, t: Throwable) {
              //  Log.e(TAG, "on error --->" + t.localizedMessage)
            }

            override fun onResponse(call: Call<NoticeBoard_Model?>, response: retrofit2.Response<NoticeBoard_Model?>) {
                KTGlobalclass().hideProgress(this@KTNoticeboard_activity, progressDialog)
                val noticeBoard_model = response.body()
                Log.e(TAG, "on Sucess --->" + noticeBoard_model!!.resId)
                if (noticeBoard_model != null) {
                    if (!KTGlobalclass().isNull(noticeBoard_model.response) && noticeBoard_model.response.equals(Constants.caps_invalidApikey, ignoreCase = true)) {
                        KTGlobalclass().redirectToLogin(this@KTNoticeboard_activity)
                    } else {
                        val array_notice = ArrayList<NoticeBoard_Model>()
                        if (noticeBoard_model.messages != null) {
                            array_notice.add(noticeBoard_model)
                            if (array_notice[0].messages[0].messageCode != null) {
                                 var msgCode = array_notice[0].messages[0].messageCode
                                val noticeBoard_adapter = com.example.e5322.thyrosoft.Kotlin.KTAdapter.NoticeBoard_Adapter(this@KTNoticeboard_activity, array_notice, msgCode)
                                noticeboard_list!!.adapter = noticeBoard_adapter


                                noticeBoard_adapter!!.clickListerforAckNoticeboard(object : RefreshNoticeBoard {
                                    override fun onClickAcknowledge(msgCode: String?) {
                                        postack(msgCode);
                                    }
                                })
                            }

                        }
                    }
                }

            }

        })
    }

    private fun postack(msgCode: String?) {
        val requestModel = KTAcknowledmentreq(api_key, msgCode, user)

        val ktapiInteface: KTAPIInteface = KTRetrofitClient().getInstance()!!.getClient(this@KTNoticeboard_activity, Api.LIVEAPI)!!.create(KTAPIInteface::class.java)
        val callack: Call<KTAcknowledeResponse> = ktapiInteface.postack(requestModel)

        callack.enqueue(object : Callback<KTAcknowledeResponse> {
            override fun onFailure(call: Call<KTAcknowledeResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<KTAcknowledeResponse>, response: Response<KTAcknowledeResponse>) {
                try {
                    var responseModel = response.body()
                    if (responseModel != null) {
                        if (!GlobalClass.isNull(responseModel.resId) && responseModel.resId.equals(Constants.RES0000, ignoreCase = true)) {
                            TastyToast.makeText(this@KTNoticeboard_activity, responseModel.response, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                            getNoticeBoardData()
                        } else {
                            TastyToast.makeText(this@KTNoticeboard_activity, responseModel.response, TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                        }
                    } else {
                        Toast.makeText(this@KTNoticeboard_activity, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                }
            }

        })
    }



/*    private fun getNoticeBoardData() {
        val progressDialog = GlobalClass.ShowprogressDialog(this@KTNoticeboard_activity)
        requestQueueNoticeBoard = GlobalClass.setVolleyReq(this@KTNoticeboard_activity)
        val jsonObjectRequestProfile = JsonObjectRequest(Request.Method.GET, Api.NoticeBoardData + "" + api_key + "/getNoticeMessages", Response.Listener { response ->
            Log.e(TAG, "onResponse: $response")
            GlobalClass.hideProgress(this@KTNoticeboard_activity, progressDialog)
            val noticeBoard_model = Gson().fromJson(response.toString(), NoticeBoard_Model::class.java)
            if (noticeBoard_model != null) {
                if (!GlobalClass.isNull(noticeBoard_model.response) && noticeBoard_model.response.equals(Constants.caps_invalidApikey, ignoreCase = true)) {
                    GlobalClass.redirectToLogin(this@KTNoticeboard_activity)
                } else {
                    val array_notice = ArrayList<NoticeBoard_Model>()
                    if (noticeBoard_model.messages != null) {
                        array_notice.add(noticeBoard_model)
                        if (array_notice[0].messages[0].messageCode != null) {
                            msgCode = array_notice[0].messages[0].messageCode
                            val noticeBoard_adapter = NoticeBoard_Adapter(this@KTNoticeboard_activity, array_notice, msgCode)


                            noticeBoard_adapter.clickListerforAckNoticeboard(RefreshNoticeBoard { msgCode ->
                                PostQueOtp = GlobalClass.setVolleyReq(this@KTNoticeboard_activity)
                                var jsonObject: JSONObject? = null
                                try {
                                    val requestModel = AckNoticeRequestModel()
                                    requestModel.apiKey = api_key
                                    requestModel.messageCode = msgCode
                                    requestModel.userCode = user
                                    val json = Gson().toJson(requestModel)
                                    jsonObject = JSONObject(json)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                val jsonObjectRequest1 = JsonObjectRequest(Request.Method.POST, Api.acknowledgeNoticeBoard,
                                        jsonObject, Response.Listener { response ->
                                    try {
                                        Log.e(TAG, "onResponse: response$response")
                                        val responseModel = Gson().fromJson(response.toString(), AckNoticeResponseModel::class.java)
                                        if (responseModel != null) {
                                            if (!GlobalClass.isNull(responseModel.resId) && responseModel.resId.equals(Constants.RES0000, ignoreCase = true)) {
                                                TastyToast.makeText(this@KTNoticeboard_activity, responseModel.response, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS)
                                                getNoticeBoardData()
                                            } else {
                                                TastyToast.makeText(this@KTNoticeboard_activity, responseModel.response, TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                                            }
                                        } else {
                                            Toast.makeText(this@KTNoticeboard_activity, ToastFile.something_went_wrong, Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }, Response.ErrorListener { error ->
                                    if (error != null) {
                                    } else {
                                        println(error)
                                    }
                                })
                                GlobalClass.volleyRetryPolicy(jsonObjectRequest1)
                                PostQueOtp!!.add(jsonObjectRequest1)
                                Log.e(TAG, "onClick: URL$jsonObjectRequest1")
                                Log.e(TAG, "post data: $jsonObject")
                            })
                            noticeboard_list!!.adapter = noticeBoard_adapter

                        } else {
                            Toast.makeText(this@KTNoticeboard_activity, "" + noticeBoard_model.response, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }, Response.ErrorListener { error ->
            GlobalClass.hideProgress(this@KTNoticeboard_activity, progressDialog)
            if (error.networkResponse == null) {
                if (error.javaClass == TimeoutError::class.java) {
                    TastyToast.makeText(this@KTNoticeboard_activity, "Timeout Error", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                    // Show timeout error message
                }
            }
        })
        GlobalClass.volleyRetryPolicy(jsonObjectRequestProfile)
        requestQueueNoticeBoard!!.add(jsonObjectRequestProfile)
        Log.e(TAG, "getNoticeBoardData: url$jsonObjectRequestProfile")
    }*/
}