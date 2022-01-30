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
import kotlinx.android.synthetic.main.activity_chilled_frozen.*
import kotlinx.android.synthetic.main.activity_snacks.*

class ChilledFrozen : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chilled_frozen)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()
        if (productsList.size > 0) {
            chilled.visibility = View.VISIBLE
            no_product_found_chilled.visibility = View.GONE

            chilled.layoutManager = LinearLayoutManager(this) // TODO change layout style changed from LinearLayoutManager(activity)
            chilled.setHasFixedSize(true)
            val adapterProduct = MyProductsListAdapters(this, productsList)
            chilled.adapter = adapterProduct
        } else {
            chilled.visibility = View.GONE
            no_product_found_chilled.visibility = View.VISIBLE
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