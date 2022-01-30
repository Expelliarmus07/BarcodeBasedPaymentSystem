package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.fyp.barcodebasedpaymentsystem.R
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

         BNEditText3.setOnClickListener {
                val intent = Intent(this@SigninActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

        CustButton.setOnClickListener {
            val intent = Intent(this@SigninActivity, CustomerLoginpageActivity::class.java)
            startActivity(intent)
        }

        CashButton.setOnClickListener {
            val intent = Intent(this@SigninActivity, CashierLoginpage::class.java)
            startActivity(intent)
        }
    }
}