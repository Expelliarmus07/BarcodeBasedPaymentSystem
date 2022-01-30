package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_change_email_password.*
import kotlinx.android.synthetic.main.activity_register.*



class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        BNEditText4.setOnClickListener {
            onBackPressed()
        }

        registerButton.setOnClickListener{

                registerUser()
        }

    }

    private fun validateRegisterDetails(): Boolean {
        return when{
            TextUtils.isEmpty(fullname.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_name), true)
                false
            }

            TextUtils.isEmpty(emailt.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }


            TextUtils.isEmpty(password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_pwd), true)
                false
            }

            TextUtils.isEmpty(confirm_password_box.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_pwd), true)
                false
            }

            password.text.toString().trim { it <= ' ' } != confirm_password_box.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_pwd), true)
                false
            }

             //!password.equals(".*\\d.*".toRegex()) || password.length() < 8 || !password.equals(".*[A-Z].*".toRegex())-> {
               // showErrorSnackBar(resources.getString(R.string.capital), true)
                //false
            //}

             else -> {
                 //showErrorSnackBar(resources.getString(R.strin g.validMsg), false)
                 true
             }
        }
    }

    private fun validPassword(): Boolean
    {
        val passwordText = password.text.toString()

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

    private fun registerUser() {

        if (validateRegisterDetails() && validPassword()){

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = emailt.text.toString().trim { it <= ' ' }
            val password: String = password.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task->

                        //hideProgressDialog()

                        if(task.isSuccessful){
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user= User(
                                firebaseUser.uid,
                                fullname.text.toString().trim {it <= ' '},
                                emailt.text.toString().trim {it <= ' '},
                                
                            )
                            FirestoreClass().registerUser(this@RegisterActivity, user)
                        } else {
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
                )
        }
    }


    fun userRegistrationSuccess(){
        hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ) .show()

        val intent = Intent(this@RegisterActivity, CustomerLoginpageActivity::class.java)
        startActivity(intent)
    }




}

