package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.SliderAdapter
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoarding : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //skip_btn.setOnClickListener(this)
        //next_btn.setOnClickListener(this)

        val wormDotsIndicator = findViewById<WormDotsIndicator>(R.id.dots)

        val letsStart: Button = findViewById(R.id.get_started_btn)

        val skipButton: Button = findViewById(R.id.skip_btn)

        val viewPager: ViewPager = findViewById(R.id.slider)

        val sliderAdapter=SliderAdapter(this)

        viewPager.adapter = sliderAdapter
        wormDotsIndicator.setViewPager(viewPager)

        var currentPosition: Int = 0

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                //Log.i("Tag",position.toString())

                currentPosition = position

                if (position == 0){
                    letsStart.visibility = View.GONE
                    skipButton.visibility = View.VISIBLE

                }else if (position == 1){
                    letsStart.visibility = View.GONE
                    skipButton.visibility = View.VISIBLE

                }else if (position == 2){
                    val animed = AnimationUtils.loadAnimation(this@OnBoarding, R.anim.bottom_animation)
                    letsStart.startAnimation(animed)
                    skipButton.visibility = View.GONE
                    letsStart.visibility = View.VISIBLE
                }else {
                    letsStart.visibility = View.VISIBLE
                    skipButton.visibility = View.VISIBLE

                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })


        letsStart.setOnClickListener {
            val homeIntent = Intent(this@OnBoarding, SigninActivity::class.java) //todo change this
            startActivity(homeIntent)
        }

        skipButton.setOnClickListener {
            val homeIntent = Intent(this@OnBoarding, SigninActivity::class.java)
            startActivity(homeIntent)
        }
    }

    /*
    override fun onClick(v: View?) {
        if(v !=null) {
            when (v.id) {
                R.id.next_btn -> {

                }

                R.id.skip_btn ->{
                    val homeIntent = Intent(this@OnBoarding, SigninActivity::class.java)

                }
            }
        }
    }

     */


}