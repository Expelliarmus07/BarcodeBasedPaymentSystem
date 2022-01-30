package com.fyp.barcodebasedpaymentsystem.all.activity.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.ProductDetailsActivity
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*
import kotlinx.android.synthetic.main.single_search_item.view.*

open class SearchListAdapter(
    private val context: Context,
    private var searchList: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.single_search_item, //todo changed from item_grid_layout
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = searchList[position]

        if(holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.imageurl, holder.itemView.iv_item_image1)
            holder.itemView.tv_item_name1.text = model.productname  //todo changed from grid_product_image,price_grid,tv_grid_title
            holder.itemView.tv_item_price1.text = "RM${model.price}"
        }

        holder.itemView.setOnClickListener {
            // Launch Product details screen.
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.product_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}