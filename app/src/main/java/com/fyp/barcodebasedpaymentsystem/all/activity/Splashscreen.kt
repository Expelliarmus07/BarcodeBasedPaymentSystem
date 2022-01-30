package com.fyp.barcodebasedpaymentsystem.all.activity


import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.fyp.barcodebasedpaymentsystem.R
import kotlinx.android.synthetic.main.activity_splashscreen.*


class Splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            val animed = AnimationUtils.loadAnimation(this, R.anim.top)

            topPanel.startAnimation(animed)

            val splashScreenTimeOut=4000



        Handler().postDelayed({

            val onBoardingScreen: SharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE)

            val isFirstTime:Boolean = onBoardingScreen.getBoolean("firstTime",true)

            if (isFirstTime){
                val editor:SharedPreferences.Editor = onBoardingScreen.edit()
                editor.putBoolean("firstTime",false)
                editor.commit()

                startActivity(Intent(this@Splashscreen, OnBoarding::class.java))
                finish()
            }
            else{
                startActivity(Intent(this@Splashscreen, SigninActivity::class.java))
            }

            }, splashScreenTimeOut.toLong())


        }
    }
