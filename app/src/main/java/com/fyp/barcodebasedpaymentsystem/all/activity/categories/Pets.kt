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
import kotlinx.android.synthetic.main.activity_cooking_ingredients.*
import kotlinx.android.synthetic.main.activity_pets.*

class Pets : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pets)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        hideProgressDialog()
        if (productsList.size > 0) {
            pets_items.visibility = View.VISIBLE
            no_product_found_pets_items.visibility = View.GONE

            pets_items.layoutManager = LinearLayoutManager(this) // TODO change layout style changed from LinearLayoutManager(activity)
            pets_items.setHasFixedSize(true)
            val adapterProduct = MyProductsListAdapters(this, productsList)
            pets_items.adapter = adapterProduct
        } else {
            pets_items.visibility = View.GONE
            no_product_found_pets_items.visibility = View.VISIBLE
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