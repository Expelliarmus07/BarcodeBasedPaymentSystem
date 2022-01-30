package com.fyp.barcodebasedpaymentsystem.all.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.CartItemsListAdapter
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import kotlinx.android.synthetic.main.activity_cashier_verifying.*
import kotlinx.android.synthetic.main.activity_customer_order_details.*
import java.text.SimpleDateFormat
import java.util.*


class CustomerOrderDetailsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_order_details)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //setupActionBar()

        var CustomerOrderDetails: Order = Order()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)) {
            CustomerOrderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
        setupUI(CustomerOrderDetails)

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_my_order_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_my_order_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupUI(orderDetails: Order) {
        tv_order_details_id.text = orderDetails.productname

        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"

        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        tv_order_details_date.text = orderDateTime

        rv_my_order_items_list.layoutManager = LinearLayoutManager(this@CustomerOrderDetailsActivity)
        rv_my_order_items_list.setHasFixedSize(true)

        val cartListAdapter =
            CartItemsListAdapter(this@CustomerOrderDetailsActivity, orderDetails.items, false)
        rv_my_order_items_list.adapter = cartListAdapter


        tv_order_details_sub_total.text = "RM ${orderDetails.sub_total_amount}"
        tv_order_details_shipping_charge.text = "RM ${orderDetails.tax}"

        tv_order_details_total_amount.text = "RM ${orderDetails.total_amount}"

        if(orderDetails.verified == 1){
            tv_order_status.text = resources.getString(R.string.order_status_delivered)
            tv_order_status.setTextColor(
                ContextCompat.getColor(
                    this@CustomerOrderDetailsActivity,
                    R.color.colorOrderStatusDelivered
                )
            )
        }else{
            tv_order_status.text = resources.getString(R.string.order_status_pending)
            tv_order_status.setTextColor(
                ContextCompat.getColor(
                    this@CustomerOrderDetailsActivity,
                    R.color.colorSnackBarError
                )
            )
        }

    }

}