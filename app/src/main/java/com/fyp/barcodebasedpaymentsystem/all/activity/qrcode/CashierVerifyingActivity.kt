package com.fyp.barcodebasedpaymentsystem.all.activity.qrcode

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.BaseActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.CashierDashboardActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.CustomerOrderDetailsActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.Dashboard
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.CartItemsListAdapter
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import kotlinx.android.synthetic.main.activity_cashier_verifying.*
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_customer_order_details.*

class CashierVerifyingActivity : BaseActivity(){
    private var mQRcodeId: String = ""
    private var mTotal: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier_verifying)

        if (intent.hasExtra(Constants.qr_code)) {
            mQRcodeId = intent.getStringExtra(Constants.qr_code)!!
            Log.i("productname: ", mQRcodeId)
        }

        getProductDetails(mQRcodeId)

        btn_verify_order.setOnClickListener{
            updateVerified(mQRcodeId)
        }

        btn_calc.setOnClickListener{
            calculationPart()
        }
    }


    private fun updateVerified(productname:String){
        val verified = HashMap<String,Any>()
        verified[Constants.VERIFIED] = 1
        verified[Constants.CASHNAME] = "Samantha" //todo Check Cashier Name
        FirestoreClass().updateVerified(this,verified,productname)
    }

    fun updateSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@CashierVerifyingActivity,
            resources.getString(R.string.purchased_verified),
            Toast.LENGTH_SHORT
        )
            .show()

        startActivity(Intent(this@CashierVerifyingActivity, CashierDashboardActivity::class.java))
        finish()
    }


     fun getProductDetails(code: String) {
         mQRcodeId = code
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getCheckout(this@CashierVerifyingActivity,mQRcodeId)
    }

    fun setupCashUI(orderDetails: Order){
        hideProgressDialog()
        rv_cart_list_items1.layoutManager = LinearLayoutManager(this@CashierVerifyingActivity)
        rv_cart_list_items1.setHasFixedSize(true)

        val cartListAdapter =
            CartItemsListAdapter(this@CashierVerifyingActivity, orderDetails.items, false)
            rv_cart_list_items1.adapter = cartListAdapter

        mTotal= orderDetails.total_amount.toFloat()
        tv_checkout_sub_total1.text = "RM ${orderDetails.sub_total_amount}"
        tv_checkout_shipping_charge1.text = "RM ${orderDetails.tax}"
        tv_checkout_total_amount1.text= "RM ${orderDetails.total_amount} "
        calc_total.text= "RM ${orderDetails.total_amount} "

    }

    @SuppressLint("SetTextI18n")
    private fun calculationPart(){
        val payment = findViewById<EditText>(R.id.calc_payment)
        val change = findViewById<TextView>(R.id.calc_change)
        val total = findViewById<TextView>(R.id.calc_total)

        var payment_value = 0.0F
        var total_value = 0.0F

        total_value = total.text.toString().trim('R','M').toFloat()

        if(payment.text.toString().isNotEmpty()){
            payment_value = payment.text.toString().toFloat()
        }

        if(total_value >0.0 && payment_value>0.0){
            val newChange = String.format("%.2f",payment_value-total_value)
            change.text = "Rm $newChange"

        }

    }
}