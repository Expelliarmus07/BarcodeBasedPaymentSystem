package com.fyp.barcodebasedpaymentsystem.all.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.CartItemsListAdapter
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import kotlinx.android.synthetic.main.activity_customer_invoice.*
import kotlinx.android.synthetic.main.activity_customer_order_details.*
import java.text.SimpleDateFormat
import java.util.*

class CustomerInvoiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_invoice)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //setupActionBar()

        var InvoiceDetails: Order = Order()
        if (intent.hasExtra(Constants.EXTRA_INVOICE)) {
            InvoiceDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_INVOICE)!!
        }
        invoice_UI(InvoiceDetails)

        //val button:Button = findViewById(R.id.button_pdf)

        //button.setOnClickListener {
            //onBackPressed()


        //}




    }


    private fun setupActionBar() {

        setSupportActionBar(toolbar_invoice)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_invoice.setNavigationOnClickListener { onBackPressed() }
    }

    private fun invoice_UI(orderDetails: Order){

        tv_invoice_id.text = orderDetails.productname

        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"

        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        tv_invoice_date.text = orderDateTime


        rv_invoice_order.layoutManager = LinearLayoutManager(this@CustomerInvoiceActivity)
        rv_invoice_order.setHasFixedSize(true)


        val cartListAdapter =
            CartItemsListAdapter(this@CustomerInvoiceActivity, orderDetails.items, false)
        rv_invoice_order.adapter = cartListAdapter


        tv_invoice_sub_total.text = "RM ${orderDetails.sub_total_amount}"
        tv_invoice_shipping_charge.text = "RM ${orderDetails.tax}"

        tv_invoice_total_amount.text = "RM ${orderDetails.total_amount}"

        if(orderDetails.verified == 1){
            tv_invoice_status.text = resources.getString(R.string.order_status_delivered)
            tv_invoice_status.setTextColor(
                ContextCompat.getColor(
                    this@CustomerInvoiceActivity,
                    R.color.colorOrderStatusDelivered
                )
            )
        }else{
            tv_invoice_status.text = resources.getString(R.string.order_status_pending)
            tv_invoice_status.setTextColor(
                ContextCompat.getColor(
                    this@CustomerInvoiceActivity,
                    R.color.colorSnackBarError
                )
            )
        }
    }

}