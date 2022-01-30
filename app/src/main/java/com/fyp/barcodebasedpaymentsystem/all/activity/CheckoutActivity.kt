package com.fyp.barcodebasedpaymentsystem.all.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.adapters.CartItemsListAdapter
import com.fyp.barcodebasedpaymentsystem.all.activity.qrcode.QRCodeGeneratorActivity
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.CartItem
import com.fyp.barcodebasedpaymentsystem.all.model.Order
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlin.math.round

class CheckoutActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mSubTotal: Float = 0.0F
    private var mTotalAmount: Float = 0.0F




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        //setupActionBar()

        getProductList()

        btn_place_order.setOnClickListener {
            placeAnOrder()
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_checkout_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>){
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getCartItemsList(){
        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    fun placeAnOrder(){
        showProgressDialog(resources.getString(R.string.please_wait))

        val subtotal = round(mSubTotal*100)/100
        val totalamount = round(mTotalAmount*100)/100

        val order = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            "Order ${System.currentTimeMillis()}",
            mCartItemsList[0].imageurl,
            subtotal.toString(),
            "0.20", // The Tax is fixed as Rm 0.20 for now in our case.
            totalamount.toString(),
            System.currentTimeMillis()
        )

        FirestoreClass().placeAnOrder(this, order)

    }

    fun successCartItemsList(cartList: ArrayList<CartItem>){
        hideProgressDialog()
        for (product in mProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = product.stock_quantity
                }
            }
        }
        mCartItemsList = cartList

        rv_cart_list_items.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        rv_cart_list_items.adapter = cartListAdapter

        for (item in mCartItemsList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toFloat()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }

        val subtotal = round(mSubTotal*100)/100

        tv_checkout_sub_total.text = "RM $subtotal"
        // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
        tv_checkout_shipping_charge.text = "RM 0.20"  //TODO change logic


        if (mSubTotal > 0) {
            ll_checkout_place_order.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 0.2F  //TODO change tax here
            val total = round(mTotalAmount*100)/100
            tv_checkout_total_amount.text = "RM $total"
        } else {
            ll_checkout_place_order.visibility = View.GONE
        }



    }

    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    fun allDetailsUpdatedSuccessfully(code: String){
        hideProgressDialog()
        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
            .show()


        val intent = Intent(this@CheckoutActivity, QRCodeGeneratorActivity::class.java)
        intent.putExtra(Constants.prod_name,code)
        startActivity(intent)

    }



    fun orderPlacedSuccess(code :String){
        FirestoreClass().updateAllDetails(this,mCartItemsList,code)

    }

}