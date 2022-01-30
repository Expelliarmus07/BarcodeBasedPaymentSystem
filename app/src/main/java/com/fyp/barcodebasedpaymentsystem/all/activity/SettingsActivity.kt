package com.fyp.barcodebasedpaymentsystem.all.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.User
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_customer_invoice.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.custom_settings.*
import kotlinx.android.synthetic.main.rating_custom.*


class SettingsActivity : BaseActivity(), View.OnClickListener{
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        tv_edit.setOnClickListener(this)
        logout.setOnClickListener(this)

    }

    private fun custom(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.custom_settings)
        customDialog.tv_edit_profile.setOnClickListener {
            customDialog.dismiss()
            val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
            startActivity(intent)
        }

        customDialog.tv_change_email_pwd.setOnClickListener {
            customDialog.dismiss()
            val intent = Intent(this@SettingsActivity, ChangeEmailPassword::class.java)
            startActivity(intent)
        }
        customDialog.show()
    }





    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User){
        mUserDetails = user
        hideProgressDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image,iv_user_photo)
        user_fullname1.text = "${user.name}"
        user_email1.text=user.email
        user_phonenumber1.text="0${user.mobile}"
        user_gender1.text=user.gender
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
       if(v != null){
           when(v.id){
               R.id.tv_edit ->{
                   custom()
              }

               R.id.logout ->{
                   //customDialogFunction()
                   FirebaseAuth.getInstance().signOut()
                   val intent = Intent(this@SettingsActivity, SigninActivity::class.java)
                   intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   startActivity(intent)
                   finish()

               }

           }
       }
    }

}


