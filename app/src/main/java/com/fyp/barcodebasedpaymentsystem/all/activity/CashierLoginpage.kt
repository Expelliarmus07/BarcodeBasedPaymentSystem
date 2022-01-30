package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Staff
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cashier_loginpage.*
import kotlinx.android.synthetic.main.activity_cashier_loginpage.password
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.customer_loginpage.*

class CashierLoginpage :BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_cashier_loginpage)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        CashSignin.setOnClickListener(this)
    }

    fun errorMessage(){
        Toast.makeText(
            this@CashierLoginpage,
            "You are not authorized to login here!",
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun cashierLoggedInSuccess(staff: Staff){
        hideProgressDialog()

        if(staff.profileCompleted == 0){
            val intent = Intent(this@CashierLoginpage, CashierProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_CASHIER_DETAILS, staff)
            startActivity(intent)
        } else {
            startActivity(Intent(this@CashierLoginpage, CashierDashboardActivity::class.java))
        }
        finish()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.CashSignin -> {
                    logInRegisteredCashier()
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean{
        return when{
            TextUtils.isEmpty(rollnumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_pwd), true)
                false
            }
            else -> {
                //showErrorSnackBar(resources.getString(R.string.validMsg), false)
                true
            }
        }
    }

    private fun logInRegisteredCashier(){
        if (validateLoginDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = rollnumber.text.toString().trim { it <= ' ' }
            val password: String = password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->

                    if(task.isSuccessful){

                        FirestoreClass().getCashierDetails(this@CashierLoginpage)

                    }else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }

                }
        }
    }
}