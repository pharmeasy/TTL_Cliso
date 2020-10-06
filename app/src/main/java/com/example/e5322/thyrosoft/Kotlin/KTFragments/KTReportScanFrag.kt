package com.example.e5322.thyrosoft.Kotlin.KTFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.e5322.thyrosoft.API.Api
import com.example.e5322.thyrosoft.API.Constants
import com.example.e5322.thyrosoft.Activity.MessageConstants
import com.example.e5322.thyrosoft.GlobalClass
import com.example.e5322.thyrosoft.Kotlin.KTActivity.FAQ_activity
import com.example.e5322.thyrosoft.Kotlin.KTGlobalclass
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTInsertReasonsReq
import com.example.e5322.thyrosoft.Kotlin.KTModels.KTInsertreasonResponse
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTAPIInteface
import com.example.e5322.thyrosoft.Kotlin.KTRetrofit.KTRetrofitClient
import com.example.e5322.thyrosoft.R
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.lay_scanbarcode.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KTReportScanFrag : Fragment() {

    var scanIntegrator: IntentIntegrator? = null
    var TAG = javaClass.simpleName
    var usercode: kotlin.String? = null


    var EMOJI_FILTER = InputFilter { source, start, end, dest, dstart, dend ->
        for (index in start until end) {
            val type = Character.getType(source[index])
            if (type == Character.SURROGATE.toInt()) {
                return@InputFilter ""
            }
        }
        null
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.lay_scanbarcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanIntegrator = IntentIntegrator.forSupportFragment(this)
        initView(view)

    }

    fun initView(view: View) {
        val sharedPreferences = activity!!.getSharedPreferences("Userdetails", Context.MODE_PRIVATE)
        usercode = sharedPreferences.getString("USER_CODE", "")


        lin_reason.setOnClickListener {
            edt_reason.requestFocus()
            edt_reason.isFocusable = true
            edt_reason.isCursorVisible = true
        }

        element1_iv.setOnClickListener {
            scanIntegrator!!.initiateScan()
        }

        edt_wastecnt.filters = arrayOf(EMOJI_FILTER)
        val maxLength = 4
        edt_wastecnt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))


        edt_wastecnt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                val enteredString = s.toString()
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(activity, "Please enter valid Waste count", Toast.LENGTH_SHORT).show()

                    if (enteredString.length > 0) {
                        edt_wastecnt.setText(enteredString.substring(1))
                    } else {
                        edt_wastecnt.setText("")
                    }

                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        edt_reason.filters = arrayOf(EMOJI_FILTER)
        val maxreason = 500
        edt_reason.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxreason))

        edt_reason.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                val enteredString = s.toString()
                if (enteredString.startsWith(" ")) {
                    Toast.makeText(activity, "Please enter valid reason", Toast.LENGTH_SHORT).show()

                    if (enteredString.length > 0) {
                        edt_reason.setText(enteredString.substring(1))
                    } else {
                        edt_reason.setText("")
                    }

                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
//Tejas
        btn_scansubmit.setOnClickListener {
            if (!KTGlobalclass().isNetworkAvailable(activity!!)) {
                if (GlobalClass.isNull(txt_barcode!!.text.toString())) {
                    KTGlobalclass().toastyError(activity, "Please Scan barcode", false)
                } else {
                    insertscandetailData()
                }
            } else {
                KTGlobalclass().toastyError(activity, MessageConstants.CHECK_INTERNET_CONN, false)
            }
        }


        btn_reason_submit.setOnClickListener {
            if (KTGlobalclass().isNetworkAvailable(activity!!)) {
                if (GlobalClass.isNull(edt_wastecnt.text.toString())) {
                    KTGlobalclass().toastyError(activity, "Please enter Waste count", false)
                } else if (GlobalClass.isNull(edt_reason.text.toString())) {
                    KTGlobalclass().toastyError(activity, "Please enter reason", false)
                } else {
                    insertreason()
                }
            } else {
                KTGlobalclass().toastyError(activity, MessageConstants.CHECK_INTERNET_CONN, false)
            }
        }


    }

    fun insertreason() {

        Log.e(TAG, "Reason lenght ------>" + edt_reason.text.length)
        Log.e(TAG, "  ------>" + edt_reason.text.length)

        btn_reason_submit.isClickable = false
        btn_reason_submit.isEnabled = false

        val p_dialog = KTGlobalclass().ShowprogressDialog(activity)
        val insertreasonreq = KTInsertReasonsReq(edt_reason.text.toString(), edt_wastecnt.text.toString(), usercode!!)

        val apiInterface: KTAPIInteface = KTRetrofitClient().getInstance()!!.getClient(activity, Api.insertscandetail)!!.create(KTAPIInteface::class.java)
        val insertScandetailResCall: Call<KTInsertreasonResponse> = apiInterface.insertreason(insertreasonreq)

       // Log.e(TAG, "URL ---->" + insertScandetailResCall.request().url())
       // Log.e(TAG, "REQUEST BODY ---->" + GsonBuilder().create().toJson(insertreasonreq))


        insertScandetailResCall.enqueue(object : Callback<KTInsertreasonResponse> {
            override fun onFailure(call: Call<KTInsertreasonResponse>, t: Throwable) {
                KTGlobalclass().hideProgress(context, p_dialog)
            }

            override fun onResponse(call: Call<KTInsertreasonResponse>, response: Response<KTInsertreasonResponse>) {

                try {
                    if (response.body()!!.red_id.equals(Constants.RES0000, ignoreCase = true)) {
                        btn_reason_submit.isClickable = true
                        btn_reason_submit.isEnabled = true
                        KTGlobalclass().hideProgress(context, p_dialog)
                       // Log.e(TAG, "DATA SUBMITEED SUCCESSS ")
                        GlobalClass.toastySuccess(context, "Data submitted Successfully", false)
                        txt_barcode.text = null
                        clearField()

                    } else {
                        btn_reason_submit.isClickable = true
                        btn_reason_submit.isEnabled = true
                        KTGlobalclass().toastyError(context, response.body()!!.response, false)
                        KTGlobalclass().hideProgress(context, p_dialog)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        })

    }

    fun insertscandetailData() {

    }

    private fun clearField() {
        txt_barcode.text = ""
        edt_reason.text.clear()
        edt_wastecnt.text.clear()
    }
}