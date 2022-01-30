package com.fyp.barcodebasedpaymentsystem.all.activity


import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fyp.barcodebasedpaymentsystem.R

import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.CategoryFragment
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.CustomerReceiptFragment
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.HomeFragment


import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@Dashboard,
                R.drawable.unnamed
            )

        )

        /*
        bottom_navigation.add(MeowBottomNavigation.Model(0,R.drawable.grocery_stroke))
        bottom_navigation.add(MeowBottomNavigation.Model(1,R.drawable.home_stroke))
        bottom_navigation.add(MeowBottomNavigation.Model(2,R.drawable.recipt_stoke))
         */


        val homeFragment = HomeFragment()
        val categoryFragment = CategoryFragment()
        val receiptFragment = CustomerReceiptFragment()

        makeCurrentFragment(homeFragment)

        bottom_navigation.setOnItemSelectedListener {
            when(it){
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_category -> makeCurrentFragment(categoryFragment)
                R.id.ic_receipt -> makeCurrentFragment(receiptFragment)

            }

        }

        /*
        bottom_navigation.show(1,true)
        bottom_navigation.setOnClickMenuListener {
            when(it.id){
                0 -> makeCurrentFragment(categoryFragment)
                1 -> makeCurrentFragment(homeFragment)
                2 -> makeCurrentFragment(receiptFragment)
            }
        }

         */

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }


}