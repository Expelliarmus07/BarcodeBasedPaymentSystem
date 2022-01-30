package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_change_email_password.*
import kotlinx.android.synthetic.main.activity_register.*

class ChangeEmailPassword : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email_password)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        change_link.setOnClickListener(this)


    }

    private fun validPassword(): Boolean
    {
        val passwordText = change_password.text.toString()

        return when{

            passwordText.length < 8 -> {
                showErrorSnackBar(resources.getString(R.string.min_char), true)
                false
            }
            !passwordText.matches(".*[A-Z].*".toRegex()) ->{
                showErrorSnackBar(resources.getString(R.string.cap_let), true)
                false
            }
            !passwordText.matches(".*[a-z].*".toRegex()) ->{
                showErrorSnackBar(resources.getString(R.string.small_let), true)
                false
            }

            else -> {
                true
            }
        }
    }


    private fun validateChangeDetails(): Boolean {

        return when{
            TextUtils.isEmpty(change_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(change_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_pwd), true)
                false
            }

            change_password.text.toString().trim { it <= ' ' } != confirm_password.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_pwd), true)
                false
            }

            else -> {
                //showErrorSnackBar(resources.getString(R.string.validMsg), false)
                true
            }
        }
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.change_link -> {
                    changeEmail()
                }
            }
        }
    }




    private fun changeEmail() {


        if (validateChangeDetails() && validPassword()){

            val user = Firebase.auth.currentUser
            val email : String = change_email.text.toString().trim { it <= ' ' }
            val password : String = confirm_password.text.toString().trim { it <= ' ' }



            user!!.updateEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userHashMap = HashMap<String,Any>()
                        userHashMap[Constants.EMAIL] = email
                        showProgressDialog(resources.getString(R.string.please_wait))
                        FirestoreClass().updateEmail(this,userHashMap)

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }

            user.updatePassword(password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Tag", "User password updated.")
                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }

    }

    fun EmailUpdateSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@ChangeEmailPassword,
            resources.getString(R.string.msg_profile_update_successfully),
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(this@ChangeEmailPassword, Dashboard::class.java)
        startActivity(intent)

    }


}