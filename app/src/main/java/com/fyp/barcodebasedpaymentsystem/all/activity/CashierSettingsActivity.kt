package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Staff
import com.fyp.barcodebasedpaymentsystem.all.model.User
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cashier_settings.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.logout

class CashierSettingsActivity : BaseActivity(), View.OnClickListener{
    private lateinit var mStaffDetails: Staff

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier_settings)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        tv_edit_cash.setOnClickListener(this)
        clogout.setOnClickListener(this)
    }

    private fun getCashierDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCashierDetails(this)
    }

    fun staffDetailsSuccess(staff: Staff){
        mStaffDetails = staff
        hideProgressDialog()

        GlideLoader(this@CashierSettingsActivity).loadUserPicture(staff.image1,iv_cashier_photo)
        user_fullname2.text = "${staff.name1}"
        user_email2.text=staff.email1
        user_phonenumber2.text="0${staff.mobile1}"
        user_gender2.text=staff.gender1

    }

    override fun onResume() {
        super.onResume()
        getCashierDetails()
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){

                R.id.tv_edit_cash ->{
                    val intent = Intent(this@CashierSettingsActivity, CashierProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_CASHIER_DETAILS, mStaffDetails)
                    startActivity(intent)
                }


                R.id.clogout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@CashierSettingsActivity, SigninActivity::class.java)
                    intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

}