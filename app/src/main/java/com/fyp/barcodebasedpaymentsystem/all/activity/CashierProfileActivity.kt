package com.fyp.barcodebasedpaymentsystem.all.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Staff
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_cashier_profile.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class CashierProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mCashierDetails: Staff
    private var mSelectedImageFileUri: Uri? = null
    private var mCashierProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier_profile)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if(intent.hasExtra(Constants.EXTRA_CASHIER_DETAILS)){

            mCashierDetails = intent.getParcelableExtra(Constants.EXTRA_CASHIER_DETAILS)!!
        }

        cashier_fullname.setText(mCashierDetails.name1)
        cashier_email.isEnabled=false
        cashier_email.setText(mCashierDetails.email1)

        if(mCashierDetails.profileCompleted ==0){
            tv_title_cashier.text = resources.getString(R.string.title_complete_profile)
            cashier_fullname.isEnabled = false
        }else{
            tv_title_cashier.text = resources.getString(R.string.title_edit_profile)
            GlideLoader(this@CashierProfileActivity).loadUserPicture(mCashierDetails.image1, iv_cashier_photo)

            cashier_email.isEnabled = false
            cashier_email.setText(mCashierDetails.email1)

            if(mCashierDetails.mobile1 != 0L){
                cashier_phonenumber.setText(mCashierDetails.mobile1.toString())
            }

            if(mCashierDetails.gender1 == Constants.MALE){
                rb_male_cash.isChecked = true
            }else{
                rb_female_cash.isChecked = true
            }
        }

        cashier_fullname.isEnabled = false
        cashier_fullname.setText(mCashierDetails.name1)

        cashier_email.isEnabled = false
        cashier_email.setText(mCashierDetails.email1)

        iv_cashier_photo.setOnClickListener(this@CashierProfileActivity)
        btn_save_cashier.setOnClickListener(this@CashierProfileActivity)


    }

    override fun onClick(v: View?) {
        if(v !=null){
            when(v.id){
                R.id.iv_cashier_photo ->{
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //showErrorSnackBar("You already have the storage permission", false)
                        Constants.showImageChooser(this)
                    } else {

                        ActivityCompat.requestPermissions(
                            this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_save_cashier ->{
                    if(validateCashierProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait))
                        if(mSelectedImageFileUri !=null)
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.CASHIER_PROFILE_IMAGE)
                        else{
                            updateCashierProfileDetails()
                        }
                    }
                }
            }
            }
        }

    private fun updateCashierProfileDetails(){  //todo check const name
        val cashierHashMap = HashMap<String,Any>()

        val fullname = cashier_fullname.text.toString().trim { it <= ' '}
        if(fullname !=mCashierDetails.name1){
            cashierHashMap[Constants.NAME] = fullname
        }

        val mobileNumber = cashier_phonenumber.text.toString().trim { it <= ' '}

        val gender = if (rb_male_cash.isChecked){
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if(mCashierProfileImageURL.isNotEmpty()){
            cashierHashMap[Constants.IMAGE1] = mCashierProfileImageURL
        }
        if(mobileNumber.isNotEmpty() && mobileNumber !=mCashierDetails.mobile1.toString()){
            cashierHashMap[Constants.MOBILE1] = mobileNumber.toLong()
        }
        if (gender.isNotEmpty() && gender != mCashierDetails.gender1){
            cashierHashMap[Constants.GENDER1] = gender
        }

        cashierHashMap[Constants.GENDER1] = gender
        cashierHashMap[Constants.COMPLETE_PROFILE] = 1

        FirestoreClass().updateCashierProfileData(this, cashierHashMap)

    }

    fun cashierProfileUpdateSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@CashierProfileActivity,
            resources.getString(R.string.msg_profile_update_successfully),
            Toast.LENGTH_SHORT
        )
            .show()

        startActivity(Intent(this@CashierProfileActivity,CashierDashboardActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //showErrorSnackBar("The storage permission is granted.", false)
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!
                        //iv_user_photo.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_cashier_photo)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@CashierProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }


    private fun validateCashierProfileDetails(): Boolean{

        return when{
            TextUtils.isEmpty(cashier_phonenumber.text.toString().trim() { it <=' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL:String){
        //hideProgressDialog()

        mCashierProfileImageURL = imageURL
        updateCashierProfileDetails()
    }
}