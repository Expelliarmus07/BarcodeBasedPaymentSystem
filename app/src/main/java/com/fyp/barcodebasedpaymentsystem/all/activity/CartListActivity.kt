package com.fyp.barcodebasedpaymentsystem.all.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.CartItemsListAdapter
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.CartItem
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlin.math.round

class CartListActivity :  BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        //setupActionBar()
        btn_checkout.setOnClickListener {
            customDialogFunction()
        }
    }

    private fun customDialogFunction(){
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.custom_dialog)
        customDialog.yes.setOnClickListener {
            customDialog.dismiss()
            val intent= Intent(this@CartListActivity, CheckoutActivity::class.java)
            startActivity(intent)

        }
        customDialog.no.setOnClickListener {
            Toast.makeText(this,"Clicked Cancel", Toast.LENGTH_LONG)
                .show()
            customDialog.dismiss()
        }
        customDialog.show()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {

                    cartItem.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0){
                        cartItem.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        mCartListItems = cartList

        if(mCartListItems.size>0){
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, mCartListItems, true)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Float = 0.0F

            for (item in mCartListItems) {
                val availableQuantity = item.stock_quantity.toInt()
                if(availableQuantity > 0) {
                    val price = item.price.toFloat()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }
            }
            val subtotal = round(subTotal*100)/100
            tv_sub_total.text = "RM $subtotal"
            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
            tv_tax.text = "RM0.20" //TODO Change GST value here

            if (subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 0.2 //TODO Change Logic Here
                val totalamount = round(total*100)/100
                tv_total_amount.text = "RM $totalamount"
            } else {
                ll_checkout.visibility = View.GONE
            }

        } else {
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideProgressDialog()
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getProductList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this@CartListActivity)
    }

    private fun getCartItemsList() {
        //showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this@CartListActivity)
    }

    fun itemUpdateSuccess() {
        hideProgressDialog()
        getCartItemsList()
    }

    override fun onResume() {
        super.onResume()
        getProductList()
    }

    fun itemRemovedSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@CartListActivity,
            resources.getString(R.string.msg_item_removed_successfully),
            Toast.LENGTH_SHORT
        ).show()

        getCartItemsList()
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }
}