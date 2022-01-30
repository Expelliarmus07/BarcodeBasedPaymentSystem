package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.User
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.emailt
import kotlinx.android.synthetic.main.activity_register.password
import kotlinx.android.synthetic.main.customer_loginpage.*

class CustomerLoginpageActivity : BaseActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_loginpage)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        forgotpwd.setOnClickListener(this)
        Signin.setOnClickListener(this)
        createacc.setOnClickListener(this)
    }

    fun errorMessage(){
        Toast.makeText(
            this@CustomerLoginpageActivity,
            "You are not authorized to login here, Please Contact the Admin!",
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun userLoggedInSuccess(user: User){
        hideProgressDialog()

        if(user.profileCompleted == 0){
            val intent = Intent(this@CustomerLoginpageActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            startActivity(Intent(this@CustomerLoginpageActivity, Dashboard::class.java))
        }
        finish()
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.forgotpwd -> {
                    val intent = Intent(this@CustomerLoginpageActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.Signin -> {
                    logInRegisteredUser()
                }

                R.id.createacc -> {
                    val intent = Intent(this@CustomerLoginpageActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }

        }
    }

    private fun validateLoginDetails(): Boolean {
        return when{
            TextUtils.isEmpty(email.text.toString().trim { it <= ' ' }) -> {
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

    private fun logInRegisteredUser() {

        if (validateLoginDetails()){

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = email.text.toString().trim { it <= ' ' }
            val password: String = password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                        //hideProgressDialog()

                        if(task.isSuccessful){

                            FirestoreClass().getUserDetails(this@CustomerLoginpageActivity)
                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }

        }
        }
    }

}