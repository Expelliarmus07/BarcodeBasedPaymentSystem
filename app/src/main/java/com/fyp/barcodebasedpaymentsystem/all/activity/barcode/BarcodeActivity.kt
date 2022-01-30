package com.fyp.barcodebasedpaymentsystem.all.activity.barcode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.BaseActivity
import com.fyp.barcodebasedpaymentsystem.all.activity.CartListActivity
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.CartItem
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_barcode.*
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_product_details.iv_product_detail_image
import org.w3c.dom.Text

class BarcodeActivity : BaseActivity(), View.OnClickListener {
    private var mBarcodeID: String = ""
    private lateinit var mBarcodeDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode)
        //setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mBarcodeID = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product_Barcode ID: ", mBarcodeID)
        }

        getProductDetails(mBarcodeID)
        btn_add_to_cart1.visibility = View.VISIBLE //self added

        btn_add_to_cart1.setOnClickListener(this)
        btn_go_to_cart1.setOnClickListener(this)


    }

    private fun getProductDetails(code: String) {
        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))
        // Call the function of FirestoreClass to get the product details.
        mBarcodeID = code
        //"9556156001176"
        FirestoreClass().getProduct(this, mBarcodeID)
    }

    fun BproductDetailsSuccess(product: Product){
        mBarcodeDetails =product

        hideProgressDialog()
        GlideLoader(this@BarcodeActivity).loadProductPicture(
            product.imageurl,
            iv_product_detail_image1
        )
        tv_product_details_title1.text = product.productname
        tv_product_details_price1.text = "RM${product.price}"
        tv_product_details_description1.text = product.datestocked
        tv_product_barcode_details_description1.text = product.no
        tv_product_details_available_quantity1.text = product.stock_quantity

        if(product.stock_quantity.toInt() == 0){
            hideProgressDialog()
            // Hide the AddToCart button if the item is already in the cart.
            btn_add_to_cart.visibility = View.GONE

            tv_product_details_available_quantity.text =
                resources.getString(R.string.lbl_out_of_stock)

            tv_product_details_available_quantity.setTextColor(
                ContextCompat.getColor(
                    this@BarcodeActivity,
                    R.color.colorSnackBarError
                )
            )
        }else {
            FirestoreClass().checkIfItemExistInCart(this@BarcodeActivity, mBarcodeID)
            //self checked
        }
    }

    fun productExistsInCart1() {

        // Hide the progress dialog.
        hideProgressDialog()

        // Hide the AddToCart button if the item is already in the cart.
        btn_add_to_cart1.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btn_go_to_cart1.visibility = View.VISIBLE
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

    fun addToCartSuccess1(){

        hideProgressDialog()

        Toast.makeText(
            this@BarcodeActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()
        // Hide the AddToCart button if the item is already in the cart.
        btn_add_to_cart1.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btn_go_to_cart1.visibility = View.VISIBLE

    }

    private fun addtoCart1(){
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mBarcodeID,
            mBarcodeDetails.productname,
            mBarcodeDetails.price,
            mBarcodeDetails.imageurl,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addCartItems(this@BarcodeActivity, cartItem)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart1 -> {
                    addtoCart1()
                }
                R.id.btn_go_to_cart1->{
                    startActivity(Intent(this@BarcodeActivity, CartListActivity::class.java))
                }
            }
        }
    }


}