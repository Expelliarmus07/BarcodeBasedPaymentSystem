package com.fyp.barcodebasedpaymentsystem.all.activity

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
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
import com.fyp.barcodebasedpaymentsystem.all.model.User
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException
import java.util.jar.Manifest


class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
       )

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){

            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        user_fullname.setText(mUserDetails.name)

        user_email.isEnabled = false
        user_email.setText(mUserDetails.email)

        if(mUserDetails.profileCompleted == 0){
            tv_title1.text = resources.getString(R.string.title_complete_profile)
            user_fullname.isEnabled = false
        } else {
            tv_title1.text = resources.getString(R.string.title_edit_profile)
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, iv_user_photo)

            user_email.isEnabled = false
            user_email.setText(mUserDetails.email)

            if(mUserDetails.mobile != 0L){
                user_phonenumber.setText(mUserDetails.mobile.toString())
            }
            if(mUserDetails.gender == Constants.MALE){
                rb_male.isChecked = true
            } else {
                rb_female.isChecked =true
            }
        }

        user_fullname.isEnabled = false
        user_fullname.setText(mUserDetails.name)

        user_email.isEnabled = false
        user_email.setText(mUserDetails.email)

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_save.setOnClickListener(this@UserProfileActivity)

    }

    override fun onClick(v: View?) {
        if(v !=null){
            when(v.id){
                R.id.iv_user_photo -> {

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

                R.id.btn_save -> {

                    if (validateUserProfileDetails()){

                        showProgressDialog(resources.getString(R.string.please_wait))

                        if(mSelectedImageFileUri !=null)
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE)
                        else{
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }


    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String,Any>()

        val fullname= user_fullname.text.toString().trim { it <= ' '}
        if (fullname != mUserDetails.name){
            userHashMap[Constants.NAME] = fullname
        }

        val mobileNumber = user_phonenumber.text.toString().trim { it <= ' '}
        val gender = if (rb_male.isChecked){
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if(mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty() && mobileNumber !=mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender !=mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }

        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        //showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_successfully),
            Toast.LENGTH_SHORT
        )
            .show()

        //startActivity(Intent(this@UserProfileActivity, OnBoarding::class.java)) //todo please check here
        startActivity(Intent(this@UserProfileActivity, Dashboard::class.java))
        finish()


        /*
        val onBoardingScreen: SharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE)

        val isFirstTime:Boolean = onBoardingScreen.getBoolean("firstTime",true)

        if (isFirstTime){
            val editor:SharedPreferences.Editor = onBoardingScreen.edit()
            editor.putBoolean("firstTime",false)
            editor.commit()

            startActivity(Intent(this@UserProfileActivity, OnBoarding::class.java))
            finish()
        }
        else{
            startActivity(Intent(this@UserProfileActivity, Dashboard::class.java))

        }

         */



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
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_user_photo)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
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

    private fun validateUserProfileDetails(): Boolean {
                    return when {
                        TextUtils.isEmpty(user_phonenumber.text.toString().trim() { it <=' '})  -> {
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

        mUserProfileImageURL = imageURL
        updateUserProfileDetails()

    }
}


