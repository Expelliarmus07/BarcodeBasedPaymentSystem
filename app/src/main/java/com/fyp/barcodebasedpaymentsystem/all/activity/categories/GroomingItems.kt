package com.fyp.barcodebasedpaymentsystem.all.activity.categories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.BaseActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.MyProductsListAdapters
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import kotlinx.android.synthetic.main.activity_cleaning_items.*
import kotlinx.android.synthetic.main.activity_cleaning_items.cleaning_items
import kotlinx.android.synthetic.main.activity_grooming_items.*

class GroomingItems : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grooming_items)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()
        if (productsList.size > 0) {
            grooming_items.visibility = View.VISIBLE
            no_product_found_grooming_items.visibility = View.GONE

            grooming_items.layoutManager = LinearLayoutManager(this) // TODO change layout style changed from LinearLayoutManager(activity)
            grooming_items.setHasFixedSize(true)
            val adapterProduct = MyProductsListAdapters(this, productsList)
            grooming_items.adapter = adapterProduct
        } else {
            grooming_items.visibility = View.GONE
            no_product_found_grooming_items.visibility = View.VISIBLE
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