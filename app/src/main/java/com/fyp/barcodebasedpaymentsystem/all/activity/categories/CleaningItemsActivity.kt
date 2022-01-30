package com.fyp.barcodebasedpaymentsystem.all.activity.categories

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.BaseActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.MyProductsListAdapters
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import kotlinx.android.synthetic.main.activity_cleaning_items.*

class CleaningItemsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cleaning_items)
        //setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_cleaning_details)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_cleaning_details.setNavigationOnClickListener { onBackPressed() }

    }
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()
        if (productsList.size > 0) {
            cleaning_items.visibility = View.VISIBLE
            no_product_found_cleaning_items.visibility = View.GONE

            cleaning_items.layoutManager = LinearLayoutManager(this) // TODO change layout style changed from LinearLayoutManager(activity)
            cleaning_items.setHasFixedSize(true)
            val adapterProduct = MyProductsListAdapters(this, productsList)
            cleaning_items.adapter = adapterProduct
        } else {
            cleaning_items.visibility = View.GONE
            no_product_found_cleaning_items.visibility = View.VISIBLE
        }
    }

    private fun getProductsListFromFirestore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getItemsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductsListFromFirestore()
    }
}