package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.e5322.thyrosoft.R
import kotlinx.android.synthetic.main.fragment_contact_list_fragment.*

class KTCompanyContact_activity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_contact_list_fragment)

        initView()
    }

    fun initView() {
        back.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.back -> {
                finish()
            }
        }
    }
}