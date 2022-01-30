package com.fyp.barcodebasedpaymentsystem.all.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.CashierHomeFragment
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.CashierTransDetailFragment
import com.fyp.barcodebasedpaymentsystem.all.activity.fragments.VerifiedOrderFragment
import kotlinx.android.synthetic.main.activity_cashier_dashboard.*
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_cashier_home.*

class CashierDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier_dashboard)

        supportActionBar!!.setBackgroundDrawable(
            ContextCompat.getDrawable(
                this@CashierDashboardActivity,
                R.drawable.unnamed
            )
        )

        val cashierhomefragment = CashierHomeFragment()
        val cashTransFragment= CashierTransDetailFragment()
        //val cashVerifiedFragment = VerifiedOrderFragment()

        makeCurrentFragment(cashierhomefragment)

        cashier_bottom_navigation.setOnItemSelectedListener  {
            when (it){
                R.id.ic_home2 -> makeCurrentFragment(cashierhomefragment)
                R.id.ic_details -> makeCurrentFragment(cashTransFragment)
               // R.id.ic_verified -> makeCurrentFragment(cashVerifiedFragment)
            }
        }

    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}