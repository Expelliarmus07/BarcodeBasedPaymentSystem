package com.fyp.barcodebasedpaymentsystem.all.activity.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.ProductDetailsActivity
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import kotlinx.android.synthetic.main.item_grid_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.tv_item_name
import kotlin.collections.ArrayList

open class MyProductsListAdapters : RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private val context: Context
    public var list: ArrayList<Product> //change to pub from priv , also changed to var


    constructor(context: Context, list: ArrayList<Product>) : super() {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout. item_list_layout , //todo changed from item_grid_layout
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.imageurl, holder.itemView.iv_item_image)
            holder.itemView.tv_item_name.text = model.productname  //todo changed from grid_product_image,price_grid,tv_grid_title
            holder.itemView.tv_item_price.text = "RM${model.price}"
        }

        holder.itemView.setOnClickListener {
            // Launch Product details screen.
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}