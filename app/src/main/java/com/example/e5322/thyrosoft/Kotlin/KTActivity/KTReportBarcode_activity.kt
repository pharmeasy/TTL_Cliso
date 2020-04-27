package com.example.e5322.thyrosoft.Kotlin.KTActivity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity
import com.example.e5322.thyrosoft.Fragment.ReportScansummaryFrag
import com.example.e5322.thyrosoft.Kotlin.KTFragments.KTReportScanFrag
import com.example.e5322.thyrosoft.R

class KTReportBarcode_activity : AppCompatActivity(), View.OnClickListener {

    var scan_ll_unselected: LinearLayout? = null
    var unchecked_scansumm_ll: android.widget.LinearLayout? = null
    var txt_scan: TextView? = null
    var txt_scansumm: android.widget.TextView? = null
    var back: ImageView? = null
    var home: android.widget.ImageView? = null
    var enter_arrow_enter: android.widget.ImageView? = null
    var enter_arrow_entered: android.widget.ImageView? = null
    var fragment_main: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lay_reportbarcode)
        intiView()
    }

    private fun intiView() {
        val txttitle = findViewById<TextView>(R.id.txt_name)
        txttitle.text = resources.getString(R.string.tltreport)
        txttitle.setTextColor(resources.getColor(R.color.maroon))

        scan_ll_unselected = findViewById<View>(R.id.scan_ll_unselected) as LinearLayout
        unchecked_scansumm_ll = findViewById<View>(R.id.unchecked_scansumm_ll) as LinearLayout

        enter_arrow_enter = findViewById<View>(R.id.enter_arrow_enter) as ImageView
        enter_arrow_entered = findViewById<View>(R.id.enter_arrow_entered) as ImageView

        txt_scan = findViewById<View>(R.id.txt_scan) as TextView
        txt_scansumm = findViewById<View>(R.id.txt_scansumm) as TextView
        fragment_main = findViewById<View>(R.id.fragment_mainLayout) as FrameLayout
        txt_scan!!.background = resources.getDrawable(R.drawable.enter_button)
        enter_arrow_enter!!.visibility = View.VISIBLE
        txt_scansumm!!.setBackgroundColor(resources.getColor(R.color.lightgray))
        enter_arrow_entered!!.visibility = View.GONE


        back = findViewById(R.id.back)
        home = findViewById(R.id.home)

        back!!.setOnClickListener(this)
        home!!.setOnClickListener(this)


        val reportScanFrag = KTReportScanFrag()
        replaceFragment(reportScanFrag)

        scan_ll_unselected!!.setOnClickListener {

            txt_scan!!.background = resources.getDrawable(R.drawable.enter_button)
            enter_arrow_enter!!.visibility = View.VISIBLE
            txt_scansumm!!.setBackgroundColor(resources.getColor(R.color.lightgray))
            enter_arrow_entered!!.visibility = View.GONE
            val petct_frag = KTReportScanFrag()
            replaceFragment(petct_frag)

        }

        unchecked_scansumm_ll!!.setOnClickListener {

            txt_scansumm!!.background = resources.getDrawable(R.drawable.enter_button)
            enter_arrow_entered!!.visibility = View.VISIBLE
            txt_scan!!.setBackgroundColor(resources.getColor(R.color.lightgray))
            enter_arrow_enter!!.visibility = View.GONE
            val scanSummaryActivity = ReportScansummaryFrag()
            replaceFragment(scanSummaryActivity)

        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.back -> finish()
            R.id.home -> startActivity(Intent(this@KTReportBarcode_activity, ManagingTabsActivity::class.java))
        }
    }

    fun replaceFragment(destFragment: Fragment?) { // First get FragmentManager object.
        fragment_main!!.removeAllViews()
        val fragmentManager = supportFragmentManager
        // Begin Fragment transaction.
        val fragmentTransaction = fragmentManager.beginTransaction()
        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_mainLayout, destFragment!!)
        // Commit the Fragment replace action.
        fragmentTransaction.commitAllowingStateLoss()
    }
}