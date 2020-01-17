package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity
import com.example.e5322.thyrosoft.R

/**
 * Created by kalpesh Borane
 */
class AccreditationActivity : AppCompatActivity(), View.OnClickListener {
    var TAG = javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lay_accredation)
        initview()
    }

    private fun initview() {
        Log.e(TAG, "AccreditationActivity  ")
        findViewById<View>(R.id.back).setOnClickListener(this)
        findViewById<View>(R.id.home).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> finish()
            R.id.home -> startActivity(Intent(this@AccreditationActivity, ManagingTabsActivity::class.java))
        }
    }
}