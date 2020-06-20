package com.example.e5322.thyrosoft.Kotlin

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.example.e5322.thyrosoft.*
import com.example.e5322.thyrosoft.API.Constants
import com.example.e5322.thyrosoft.API.Global
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity
import com.example.e5322.thyrosoft.MainModelForAllTests.TESTS_GETALLTESTS
import com.example.e5322.thyrosoft.Models.*
import com.example.e5322.thyrosoft.Models.PincodeMOdel.CommunicationMaster
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator
import com.example.e5322.thyrosoft.Summary_MainModel.Barcodelist
import com.example.e5322.thyrosoft.Summary_MainModel.Summary_model
import com.example.e5322.thyrosoft.TestListModel.Tests
import com.example.e5322.thyrosoft.startscreen.Login
import com.sdsmdg.tastytoast.TastyToast
import id.zelory.compressor.Compressor
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class KTGlobalclass {
    val WRITE_EXTERNAL_STORAGE_PERMISSION = 1
    var flag = 0
    var setHomeAddress: String? = null
    var getPhoneNumberTofinalPost: kotlin.String? = null
    var getFinalEmailAddressToPOst: kotlin.String? = null
    var getFinalPincode: kotlin.String? = null
    var selectedTestnamesOutlab: kotlin.String? = null
    var windupCountDataToShow: String? = null
    var saveSlectedList = ArrayList<BaseModel>()
    var saveDtaeTopass: String? = null
    var setflagToRefreshData = false
    var toCPLflag = false
    var setScp_Constant: String? = null
    var setReferenceBy_Name: String? = null
    var getSelectedItemFor_Rate_calculator: String? = null
    var CHN_Date: String? = null
    var mode: String? = null
    var routine_code: kotlin.String? = null
    var flight_name: kotlin.String? = null
    var flight_number: kotlin.String? = null
    var departure_time: kotlin.String? = null
    var arrival_time: kotlin.String? = null
    var dispatch_time: kotlin.String? = null
    var CourierName: kotlin.String? = null
    var bsv_barcode: kotlin.String? = null
    var total_sample: kotlin.String? = null
    var rpl_sample: kotlin.String? = null
    var cpl_sample: kotlin.String? = null
    //var Consignment_barcode:kotlin.String? = null
    var passDate: String? = null
    var passDateFromLead: String? = null
    var lead_date: String? = null
    var setFlagBackToWoe = false
    var setFlag_back_toWOE = false
    var syntime: Long = 0
    var curr_time: Long = 0
    var differ_millis: Long = 0
    var getCourier_name: String? = null
    var get_edt_bus_name: String? = null
    var bus_name: String? = null
    var bus_number: String? = null
    var flagtsp = 0
    var bus_name_to_pass: String? = null
    var consignment_number: String? = null
    var consignment_barcode: String? = null
    var bsv_barcode_num: String? = null
    var packaging_dtl: String? = null
    var expt_transit_tm: String? = null
    var temp_consignment: String? = null
    var getSelectedScp: String? = null
    var setFlagToClose = false
    var alert: AlertDialog? = null
    var flagforRefresh = false
    var cutString: String? = null
    // var putData: Array<String>
    var getPatientIdforDeleteDetails: String? = null
    var brandName: kotlin.String? = null
    var typeName: kotlin.String? = null
    var setFlag = true
    var items = ArrayList<String>()
    var summary_models: ArrayList<Summary_model>? = null
    var barcodelists: ArrayList<Barcodelist>? = null
    var BMC_barcodelists: ArrayList<Barcodelist>? = null
    var flagToSend = false
    var flagToSendfromnavigation = false
    var flagtoMove = false
    var getAllPhoneNumber: String? = null
    var getEmailAddre: kotlin.String? = null
    var getYesterdaysDate: kotlin.String? = null
    var branditem: String? = null
    var saveMobileNUmber: kotlin.String? = null
    var typeItem: kotlin.String? = null
    var subSourceCodeItem: kotlin.String? = null
    var srNo_item: kotlin.String? = null
    var mobile_number: kotlin.String? = null
    var email_id: kotlin.String? = null
    var id_value: kotlin.String? = null
    var globalNameAadhar: kotlin.String? = null
    var globalAgeAadhar: kotlin.String? = null
    var globalGenderAadhar: kotlin.String? = null
    var responseVariable: kotlin.String? = null
    var setSR_NO: kotlin.String? = null
    var getDate: String? = null
    var setWindUpCount: kotlin.String? = null
    var getscannedData: kotlin.String? = null
    var getPhoneofTTlDPS: String? = null
    var getEmailofTTlDPS: kotlin.String? = null
    //Selected test for WOE adapter lisy
    var selctedTestNames = ArrayList<String>()
    var selctedTestNamesILS = ArrayList<String>()
    var windupBarcodeList = ArrayList<String>()
    var testCartArrayList = ArrayList<Tests>()
    var putPopDtaa = ArrayList<TESTS_GETALLTESTS>()
    var getBtechList = ArrayList<BCT_LIST>()
    var setScannedBarcodes = ArrayList<SetBarcodeDetails>()
    var setScannedBarcodesULC = ArrayList<SetBarcodeDetails>()
    var global_camp_intimatgion_list_models_arrlst = ArrayList<Camp_Intimatgion_List_Model>()
    var getcamp_list = ArrayList<CAMP_LIST>()
    var finalspecimenttypewiselist = ArrayList<ScannedBarcodeDetails>()
    var specimenttype: String? = null
    var BMC_setScannedBarcodes = ArrayList<SetBarcodeDetails>()
    var BMC_BarcodeDetailsList = ArrayList<ScannedBarcodeDetails>()
    var BMC_BarcodeDetailsTTLOTHERSList = ArrayList<ScannedBarcodeDetails>()
    var BMC_specimenttype: String? = null
    var TRF_BITMAP: Bitmap? = null
    var RECEIPT_BITMAP: Bitmap? = null
    var mFragmentList: List<Fragment> = ArrayList()
    var mFragmentTitleList: List<String> = ArrayList()
    var scannedBarcodeDetails = ArrayList<ScannedBarcodeDetails>()
    //Selected profile for WOE adapter lisy
    var selctedProfileNames = ArrayList<String>()
    var selctedProfileNamesILS = ArrayList<String>()
    //Selected pop for WOE adapter lisy
    var selctedPopNames = ArrayList<String>()
    var selctedPopNamesILS = ArrayList<String>()
    var selctedoutLabTestNames = ArrayList<String>()
    var getPosition = ArrayList<Int>() ///for checkbox for employees in Employee adapetr

    var getPositionOutlab = ArrayList<Int>() ///for checkbox for employees in Employee adapetr

    var billingDETArray = ArrayList<billingDetailsModel>()
    var billingDETheaderArray = ArrayList<String>()
    var empList = ArrayList<String>() //new arralist for employee

    var date = ""
    var reg_name_glo: String? = null
    var reg_landline_glo: kotlin.String? = null
    var reg_profession_glo: kotlin.String? = null
    var reg_qualification_glo: kotlin.String? = null
    var reg_int_location_glo: kotlin.String? = null
    var reg_pincode_glo: kotlin.String? = null
    var reg_Addr_glo: kotlin.String? = null
    var reg_city_glo: kotlin.String? = null
    var reg_state_glo: kotlin.String? = null
    var reg_country_glo: kotlin.String? = null
    var reg_email_glo: kotlin.String? = null
    var reg_number_glo: kotlin.String? = null
    var type = ""
    var commSpinner = ArrayList<CommunicationMaster>()
    var commFromCPL: ArrayList<CommInbox_Model>? = null
    var commToCPL: ArrayList<CommToCpl_Model>? = null
    var FromCPLInt = 0
    //0 means from ,1 means to
    var MONTH = "" //0 means from ,1 means to

    var YEAR = "" //0 means from ,1 means to

    var CHANGEMONTH = 0 //0 means from ,1 means to

    var listdata = ArrayList<String>()
    var selectedTestsList = ArrayList<BaseModel>()
    var selectedTestsListRateCal = ArrayList<Base_Model_Rate_Calculator>()
    var debitlist = ArrayList<String>()
    var CREDITLIST = ArrayList<Ledger_DetailsModel>()
    var DEBIT = ArrayList<Ledger_DetailsModel>()
    private var dialog: Dialog? = null
    private val stringofconvertedTime: String? = null
    private val context: Context? = null
    var colors = intArrayOf(R.color.WOE, R.color.entered, R.color.confirmed, R.color.imported)
    var textcolors = intArrayOf(R.color.WOEtext, R.color.enteredtext, R.color.confirmedtext, R.color.importedtext)
    var selectedTestsListCampIntimation = ArrayList<Base_Model_Rate_Calculator>()
    var progressDialog: ProgressDialog? = null
/*
    fun KTGlobalclass(context: Context?) {
        context = context
    }*/


    fun dateFromString(dateStr: String?, dateFormat: SimpleDateFormat): Date? {
        var date: Date? = null
        try {
            date = dateFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                matrix, true)
    }

    fun DisplayImage(activity: Context, Url: String?, imageView: ImageView?) { //            Glide.get(mActivity).clearMemory();
        Glide.with(activity).load(Url)
                .asBitmap()
                .placeholder(R.drawable.userprofile).dontAnimate() //                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                .error(R.drawable.userprofile)
                .fitCenter()
                .into(imageView)
    }


    @Throws(IOException::class)
    fun copyFile(sourceFile: File?, destFile: File) {
        if (!destFile.parentFile.exists()) destFile.parentFile.mkdirs()
        if (!destFile.exists()) {
            destFile.createNewFile()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }

    fun rotate(bitmap: Bitmap?, photoPath: String?): Bitmap? {
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(photoPath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = ei!!.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        var rotatedBitmap: Bitmap? = null
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> GlobalClass.rotateImage(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> GlobalClass.rotateImage(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> GlobalClass.rotateImage(bitmap, 270f)
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }

    fun getReadableFileSize(size: Long): String? {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    fun getCompressedFile(activity: Activity?, actualImage: File?): File? {
        var compressedFile: File? = null
        try {
            compressedFile = Compressor(activity)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath)
                    .compressToFile(actualImage)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return compressedFile
    }


    fun convertDate(convDate: String?): String? {
        var convDate = convDate
        var date: Date? = null
        try {
            val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm aa")
            date = sdf.parse(convDate)
            //            sdf = new SimpleDateFormat("yyyy-MM-dd");
            convDate = sdf.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return convDate
    }

    fun isNetworkAvailable(activity: Activity): Boolean { // Using ConnectivityManager to check for Network Connection
        val connectivityManager = activity
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager
                .activeNetworkInfo
        return activeNetworkInfo != null
    }

    fun ShowprogressDialog(mContext: Context?): ProgressDialog? {
        val barProgressDialog = ProgressDialog(mContext)
        barProgressDialog.setTitle("Kindly wait ...")
        barProgressDialog.setMessage(ToastFile.processing_request)
        // barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.progress = 0
        barProgressDialog.max = 20
        barProgressDialog.show()
        barProgressDialog.setCanceledOnTouchOutside(false)
        barProgressDialog.setCancelable(false)
        return barProgressDialog
    }

    fun requestStoragePermission(context: Context?) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity?)!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) { // Provide an additional rationale to the user if the permission was not granted
// and the user would benefit from additional context for the use of the permission.
// For example if the user has previously denied the permission.
            ActivityCompat.requestPermissions(context!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    GlobalClass.WRITE_EXTERNAL_STORAGE_PERMISSION)
        } else { // permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(context!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    GlobalClass.WRITE_EXTERNAL_STORAGE_PERMISSION)
        }
    }

    fun clearPreference(activity: Activity) {
        val getProfileName = activity.getSharedPreferences("profilename", 0).edit()
        getProfileName.clear()
        getProfileName.commit()
        val editor1 = activity.getSharedPreferences("profilename", 0).edit()
        editor1.clear()
        editor1.commit()
        val editor = activity.getSharedPreferences("Userdetails", 0).edit()
        editor.clear()
        editor.commit() //
        val appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val prefsEditor1 = appSharedPrefs.edit()
        prefsEditor1.clear()
        prefsEditor1.commit()
        val profilenamesharedpref = PreferenceManager.getDefaultSharedPreferences(activity)
        val profile_shared = profilenamesharedpref.edit()
        profile_shared.clear()
        profile_shared.commit()
        val prifleDataShared = PreferenceManager.getDefaultSharedPreferences(activity)
        val profile_shared_pref = prifleDataShared.edit()
        profile_shared_pref.clear()
        profile_shared_pref.commit()
        val saveAlldata = PreferenceManager.getDefaultSharedPreferences(activity)
        val deletepredf = saveAlldata.edit()
        deletepredf.clear()
        deletepredf.commit()
        val myData = PreferenceManager.getDefaultSharedPreferences(activity)
        val prefsEditordata = myData.edit()
        prefsEditordata.clear()
        prefsEditordata.commit()
        val appSharedPrefsDtaa = PreferenceManager.getDefaultSharedPreferences(activity)
        val mydataModel = appSharedPrefsDtaa.edit()
        mydataModel.clear()
        mydataModel.commit()
    }

    fun redirectToLogin(activity: Activity) {
        TastyToast.makeText(activity, ToastFile.relogin, TastyToast.LENGTH_SHORT, TastyToast.INFO)
        val i = Intent(activity, Login::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(i)
        GlobalClass.clearPreference(activity)
    }


    fun hideProgress(mcontext: Context?, progressDialog: ProgressDialog?) {
        var mcontext = mcontext
        if (progressDialog != null) {
            if (progressDialog.isShowing) {
                mcontext = (progressDialog.context as ContextWrapper).baseContext
                if (mcontext is Activity) {
                    if (!mcontext.isFinishing && !mcontext.isDestroyed) progressDialog.dismiss()
                } else progressDialog.dismiss()
            }
        }
    }

    fun goToHome(activity: Activity?) {
        val intent = Intent(activity, ManagingTabsActivity::class.java)
        activity!!.startActivity(intent)
        activity.finish()
    }

    fun getTestList(): ArrayList<BSTestDataModel>? {
        val entity = ArrayList<BSTestDataModel>()
        var ent = BSTestDataModel()
        ent.testName = "Select"
        ent.minVal = 0
        ent.maxVal = 0
        ent.rangeVal = ""
        entity.add(ent)
        ent = BSTestDataModel()
        ent.testName = "FBS"
        ent.minVal = 70
        ent.maxVal = 120
        ent.rangeVal = "70-120"
        entity.add(ent)
        /*ent = new BSTestDataModel();
        ent.setTestName("PPBS");
        ent.setMinVal(90);
        ent.setMaxVal(140);
        ent.setRangeVal("90-140");
        entity.add(ent);*/
        ent = BSTestDataModel()
        ent.testName = "RBS"
        ent.minVal = 70
        ent.maxVal = 160
        ent.rangeVal = "70-160"
        entity.add(ent)
        return entity
    }


    fun getCollAmount(): java.util.ArrayList<String>? {
        val entity = java.util.ArrayList<String>()
        entity.add("Select collected amount")
        entity.add("0")
        entity.add("5")
        entity.add("10")
        entity.add("15")
        entity.add("20")
        entity.add("25")
        return entity
    }

    fun formatDate(currentFormat: String?, outputFormat: String?, date: String?): String? {
        var date = date
        val curFormater = SimpleDateFormat(currentFormat)
        val postFormater = SimpleDateFormat(outputFormat)
        var dateObj: Date? = null
        try {
            dateObj = curFormater.parse(date)
            date = postFormater.format(dateObj)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun formateDate1(currentFormat: String?, outputFormat: String?, dateStr: String?): String? {
        var date = dateStr
        val curFormater = SimpleDateFormat(currentFormat)
        val postFormater = SimpleDateFormat(outputFormat)

        var dateobj: Date? = null

        try {
            dateobj = curFormater.parse(date)
            date = postFormater.format(dateobj)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }


    fun SetText(txtview: TextView?, msg: String?) {
        var msg = msg
        try {
            if (msg == null) {
                msg = ""
            }
            if (txtview != null) {
                if (msg.equals("null", ignoreCase = true)) {
                    txtview.text = ""
                } else {
                    txtview.text = "" + msg
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month - 1] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }

    fun SetEditText(txtview: EditText?, msg: String?) {
        var msg = msg
        try {
            if (msg == null) {
                msg = ""
            }
            if (txtview != null) {
                if (msg.equals("null", ignoreCase = true)) {
                    txtview.setText("")
                } else {
                    txtview.setText("" + msg)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun IsNull(msg: String?): String? {
        var msg = msg
        try {
            if (msg == null) {
                msg = ""
            }
            if (msg != null) {
                if (msg.equals("null", ignoreCase = true)) {
                    msg = ""
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return msg
    }

    fun getStringwithOutSpace(str_gbls: String): String? {
        var str = ""
        str = str_gbls.replace(" ".toRegex(), "%20")
        return str
    }


    fun toastySuccess(context: Context?, string: String?, b: Boolean) {
        TastyToast.makeText(context, string, KTGlobalclass().ToastLength(b), TastyToast.SUCCESS)
    }

    fun toastyInfo(context: Context?, string: String?, b: Boolean) {
        TastyToast.makeText(context, string, KTGlobalclass().ToastLength(b), TastyToast.INFO)
    }

    fun toastyError(context: Context?, string: String?, b: Boolean) {
        TastyToast.makeText(context, string, KTGlobalclass().ToastLength(b), TastyToast.INFO)
    }

    fun ToastLength(b: Boolean): Int {
        return if (b) {
            Toast.LENGTH_LONG
        } else {
            Toast.LENGTH_SHORT
        }
    }

    fun printLog(Type: String, Tag: String, Label: String, msg: String) {

        try {
            if (BuildConfig.DEBUG) {
                if (Type.equals("Error", ignoreCase = true)) {
                    Log.e(Tag, " $Label: $msg")
                } else if (Type.equals("Info", ignoreCase = true)) {
                    Log.i(Tag, " $Label: $msg")
                } else if (Type.equals("Debug", ignoreCase = true)) {
                    Log.d(Tag, " $Label: $msg")
                } else if (Type.equals("Warning", ignoreCase = true)) {
                    Log.w(Tag, " $Label: $msg")
                } else if (Type.equals("sout", ignoreCase = true)) {
                    println("$Tag $Label: $msg")
                }
            } else {
                println("Live....")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    fun showImageDialog(activity: Activity, file: File, url: String?, flag: Int) {
        dialog = Dialog(activity)
        dialog!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.trf_img_dialog)
        dialog!!.setCancelable(true)
        val imgView = KTGlobalclass().dialog!!.findViewById<View>(R.id.imageview) as ImageView
        val img_close = KTGlobalclass().dialog!!.findViewById<View>(R.id.img_close) as ImageView

        if (flag == 1) {
            val myBitmap = BitmapFactory.decodeFile(file.absolutePath)
            if (myBitmap != null) imgView.setImageBitmap(myBitmap) else Global.showCustomToast(activity, "Image not found")
        } else {
            Glide.with(activity)
                    .load(url)
                    .placeholder(activity.resources.getDrawable(R.drawable.img_no_img_aval))
                    .into(imgView)
        }
        img_close.setOnClickListener { KTGlobalclass().dialog!!.dismiss() }
        val width = (activity.resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = (activity.resources.displayMetrics.heightPixels * 0.90).toInt()
        KTGlobalclass().dialog!!.window.setLayout(width, height)
        // dialog.getWindow().setLayout(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        KTGlobalclass().dialog!!.show()

    }

    fun storeProductsCachingTime(activity: Activity) {
        val editor = activity.getSharedPreferences(Constants.PREF_PRODUCTS_CACHING, 0).edit()
        editor.putLong("offer_millis", System.currentTimeMillis())
        editor.apply()
    }

    fun Dayscnt(context: Context): Int {
        var offer_millis: Long = 0
        var current_millis: Long = 0
        var differ_millis: Long = 0
        val pref_prod_caching = context.getSharedPreferences(Constants.PREF_PRODUCTS_CACHING, Context.MODE_PRIVATE)
        offer_millis = pref_prod_caching.getLong("offer_millis", 0)
        current_millis = System.currentTimeMillis()
        differ_millis = current_millis - offer_millis
        val days = (differ_millis / (1000 * 60 * 60 * 24)).toInt()
        Log.e("TAG11", "<< DAYS >> $days")
        return days
    }

    fun dynamicolordot(context: Context?, lincolor: LinearLayout, color: Int) {
        val imageView = ImageView(context)
        imageView.setPadding(2, 0, 2, 2)
        val layoutParams = LinearLayout.LayoutParams(50, 50)
        layoutParams.gravity = Gravity.CENTER
        layoutParams.marginStart = 10
        imageView.layoutParams = layoutParams
        imageView.setImageDrawable(GlobalClass.drawCircle(context, 50, 50, color))
        lincolor.addView(imageView)
    }

    fun getStoreSynctime1(context: Context): Int {
        val synpref = context.getSharedPreferences("Syncpref", Context.MODE_PRIVATE)
        KTGlobalclass().syntime = synpref.getLong("synctime", 0)
        curr_time = System.currentTimeMillis()
        KTGlobalclass().differ_millis = KTGlobalclass().curr_time - KTGlobalclass().syntime
        val days = (KTGlobalclass().differ_millis / (1000 * 60 * 60 * 24)).toInt()
        return days
    }


    fun drawCircle(context: Context?, width: Int, height: Int, color: Int): ShapeDrawable? { //////Drawing oval & Circle programmatically /////////////
        val oval = ShapeDrawable(OvalShape())
        oval.intrinsicHeight = height
        oval.intrinsicWidth = width
        oval.paint.color = color
        return oval
    }

    fun volleyRetryPolicy(request: JsonObjectRequest) {
        val policy: RetryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.retryPolicy = policy
    }

    fun volleyRetryPolicy(request: StringRequest) {
        val policy: RetryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.retryPolicy = policy
    }

    fun isNull(str: String?): Boolean {
        return if (str == null || str == null || str.trim { it <= ' ' } == "" || str.trim { it <= ' ' } == "null" || str.trim { it <= ' ' } === "" || str.trim { it <= ' ' } === "null") true else false
    }

    fun showProgressDialog(activity: Activity?) : ProgressDialog?{
        if (progressDialog != null && !progressDialog!!.isShowing) if (!(context as Activity).isFinishing) {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setTitle("Kindly wait ...")
            progressDialog!!.setMessage(ToastFile.processing_request)
            progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progressDialog!!.progress = 0
            progressDialog!!.max = 20
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        }
        return  progressDialog
    }

    fun dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun DisplayVideoImage(activity: Activity, url: String, imageView: ImageView) {
        Glide.with(activity).load(url).asBitmap().fitCenter().into(imageView)
    }

    fun showcenterCustomToast(activity: Activity, message: String?) {
        val context = activity.applicationContext
        val inflater = activity.layoutInflater
        val toastRoot = inflater.inflate(R.layout.custom_toast, null)
        val relItem = toastRoot.findViewById<View>(R.id.relItem) as RelativeLayout
        val txtToast = toastRoot.findViewById<View>(R.id.txtToast) as TextView
        relItem.background.alpha = 204
        txtToast.text = message
        val toast = Toast(context)
        toast.view = toastRoot
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    /*  fun currencyFormat(amount: String): String? {
          var amount = amount
          var formatter: DecimalFormat? = null
          if (!TextUtils.isEmpty(amount)) {
              formatter = DecimalFormat("##,##,##,###.##")
              return formatter.format(amount.toDouble())
          } else {
              amount = "0"
          }
          return formatter!!.format(amount.toDouble())
      }*/


    fun currencyFormat(amount: String): String? {
        var amount = amount
        var formatter: DecimalFormat? = null
        if (!TextUtils.isEmpty(amount)) {
            formatter = DecimalFormat("##,##,##,###.##")
            return formatter.format(amount.toDouble())
        } else {
            amount = "0"
        }
        return formatter!!.format(amount.toDouble())
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun hideProgressDialog(barProgressDialog: ProgressDialog?, context: Context?) {
        var barProgressDialog = barProgressDialog
        var context = context
        if (barProgressDialog != null) {
            if (barProgressDialog.isShowing) {
                context = (barProgressDialog.context as ContextWrapper).baseContext
                if (context is Activity) {
                    if (!context.isFinishing && !context.isDestroyed) barProgressDialog.dismiss()
                } else barProgressDialog.dismiss()
            }
            barProgressDialog = null
        }
    }

    fun showupdate(msg: String?) {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setTitle(null)
        progressDialog!!.setMessage(msg)
        progressDialog!!.isIndeterminate = false
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        if (progressDialog != null && !progressDialog!!.isShowing) if (!(context as Activity).isFinishing) {
            progressDialog!!.show()
        }
    }

    fun hideProgressDialog1() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.dismiss()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun showShortToast(activity: Activity?, message: String?) {
        if (activity != null) {
            val context = activity.applicationContext
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }




}