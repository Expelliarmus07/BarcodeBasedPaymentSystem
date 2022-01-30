package com.fyp.barcodebasedpaymentsystem.all.activity.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.BaseActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.MyProductsListAdapters
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import kotlinx.android.synthetic.main.activity_beverage.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.fragment_category.*

class Beverage : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beverage)
        //setupActionBar()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_beverage_details)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_beverage_details.setNavigationOnClickListener { onBackPressed() }

    }


    private fun getProductsListFromFirestore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getItemsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductsListFromFirestore()
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()
        if (productsList.size > 0) {
            beverages.visibility = View.VISIBLE
            no_product_found_beverage.visibility = View.GONE

            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,1)

            beverages.layoutManager = LinearLayoutManager(this) // TODO change layout style changed from LinearLayoutManager(activity)
            beverages.setHasFixedSize(true)
            val adapterProduct = MyProductsListAdapters(this, productsList)
            beverages.adapter = adapterProduct
        } else {
            beverages.visibility = View.GONE
            no_product_found_beverage.visibility = View.VISIBLE
        }
    }
}