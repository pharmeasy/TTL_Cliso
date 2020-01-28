package com.example.e5322.thyrosoft.Kotlin.KTFragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e5322.thyrosoft.R
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.lay_scanbarcode.*

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


    }
}