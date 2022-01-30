package com.fyp.barcodebasedpaymentsystem.all.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.firestore.FirestoreClass
import com.fyp.barcodebasedpaymentsystem.all.model.Product
import com.fyp.barcodebasedpaymentsystem.all.utils.Constants
import com.fyp.barcodebasedpaymentsystem.all.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_cashier_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class CashierAddProductActivity : BaseActivity(), View.OnClickListener{

    private var mSelectedImageFileURI: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cashier_add_product)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //setupActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_cashier_add_product_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_cashier_add_product_activity.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.iv_add_update_product -> {
                   if (ContextCompat.checkSelfPermission(this,
                           android.Manifest.permission.READ_EXTERNAL_STORAGE)
                   == PackageManager.PERMISSION_GRANTED
                   ) {
                       Constants.showImageChooser(this@CashierAddProductActivity)
                   } else {
                       ActivityCompat.requestPermissions(
                           this,
                           arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                           Constants.READ_STORAGE_PERMISSION_CODE
                       )
                   }
                }

                R.id.btn_submit -> {
                    if(validateProductDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //showErrorSnackBar("The storage permission is granted.", false)
                Constants.showImageChooser(this)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileURI, Constants.PRODUCT_IMAGE)
    }

    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@CashierAddProductActivity,
            resources.getString(R.string.product_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()
        finish()   //cancel this if wants to upload multiple times
    }

    fun imageUploadSuccess(imageURL:String){
        //hideProgressDialog()
        //showErrorSnackBar("Upload Success. Image URL: $imageURL",false)

        mProductImageURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails() {
        val username = this.getSharedPreferences(
            Constants.APP_PREFERENCES, Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME, "")!! //TODO registering who uploading the details

        val smallText = til_product_title.text.toString().lowercase().trim { it <= ' '}

        val product = Product(
            FirestoreClass().getCurrentUserID(),
            username,
            til_product_title.text.toString().trim { it <= ' '},
            til_product_price.text.toString().trim { it <= ' '},
            til_product_description.text.toString().trim { it <= ' '},
            til_product_quantity.text.toString().trim { it <= ' '},
            til_product_barcode.text.toString().trim { it <= ' '},
            til_product_category.text.toString().trim { it <= ' '},
            smallText,
            mProductImageURL
        )

        FirestoreClass().uploadProductDetails(this, product)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))

                    mSelectedImageFileURI = data.data!!

                    try {
                        GlideLoader(this).loadUserPicture(mSelectedImageFileURI!!, iv_product_image)
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileURI == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image),true)
                false
            }

            TextUtils.isEmpty(til_product_title.text.toString().trim() { it <=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_title), true)
                false
            }

            TextUtils.isEmpty(til_product_price.text.toString().trim() { it <=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_price), true)
                false
            }

            TextUtils.isEmpty(til_product_description.text.toString().trim() { it <=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_description), true)
                false
            }

            TextUtils.isEmpty(til_product_quantity.text.toString().trim() { it <=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_quantity), true)
                false
            }

            TextUtils.isEmpty(til_product_barcode.text.toString().trim() { it <=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_barcode), true)
                false
            }

            TextUtils.isEmpty(til_product_category.text.toString().trim() { it <=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_category), true)
                false
            }
            else -> {
                true
            }
        }

    }

}
