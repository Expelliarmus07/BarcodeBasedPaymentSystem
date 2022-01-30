package com.fyp.barcodebasedpaymentsystem.all.activity.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.CustomerInvoiceActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.CustomerOrderDetailsActivity
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import com.fyp.barcodebasedpaymentsystem.all.model.User
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_customer_invoice.*
import kotlinx.android.synthetic.main.invoice_list.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.tv_item_name
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class CustomerReceiptAdapters(
    private val context: Context,
    private var list: ArrayList<Order>,
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.invoice_list, //todo item_list_layout
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]


        if (holder is MyViewHolder) {

            /*
            GlideLoader(context).loadProductPicture(
                model.imageurl,
                holder.itemView.iv_item_image
            )

             */

            holder.itemView.invoice_tv_item_name.text = model.productname
            holder.itemView.invoice_total_item_price.text = "RM${model.total_amount}"
            // holder.itemView.ib_delete_product.visibility = View.GONE


            val dateFormat = "dd MMM yyyy HH:mm"
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = model.order_datetime
            val orderDateTime = formatter.format(calendar.time)
            holder.itemView.invoice_order_date.text  = orderDateTime

            if(model.verified== 1){
                holder.itemView.invoice_order_status.text = "PAID"
                holder.itemView.invoice_order_status.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorOrderStatusDelivered
                    )
                )
            }else{
                holder.itemView.invoice_order_status.text  = "PENDING"
                holder.itemView.invoice_order_status.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorSnackBarError
                    )
                )
            }

        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CustomerInvoiceActivity::class.java)
            intent.putExtra(Constants.EXTRA_INVOICE, model)
            //intent.putExtra(Constants.EXTRA_PRODUCTNAME, model.productname)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}