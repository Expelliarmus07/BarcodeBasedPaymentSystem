package com.fyp.barcodebasedpaymentsystem.all.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val EMAIL: String = "email"

    //collections name in cloud firestore
    const val USERS: String = "users"
    const val STAFF: String = "staffs"
    const val PRODUCT:String = "Product"
    const val ORDERS: String = "orders"
    const val CASHNAME: String = "cashierName"

    const val EXTRA_PRODUCTNAME: String = "EXTRA PRODUCT DATA"
    const val EXTRA_INVOICE: String = "EXTRA INVOICE"
    const val VERIFIED: String= "verified"

    const val APP_PREFERENCES: String = "SriMawarJayaPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE= 2
    const val PICK_IMAGE_REQUEST_CODE= 1

    const val PRODUCT_IMAGE:String = "Product_Image"
    const val EXTRA_CASHIER_DETAILS: String = "extra_cashier_details"


    const val USER_ID:String = "user_id"
    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val DEFAULT_CART_QUANTITY: String = "1"

    const val CART_ITEMS: String = "cart_items"
    const val PRODUCT_ID: String = "product_id"
    const val CART_QUANTITY: String = "cart_quantity"

    const val NAME : String= "name"
    const val MALE: String = "male"
    const val FEMALE: String = "female"

    const val MOBILE: String = "mobile"
    const val MOBILE1: String = "mobile1"
    const val GENDER: String = "gender"
    const val GENDER1: String = "gender1"
    const val IMAGE: String = "image"
    const val IMAGE1: String = "image1"


    const val COMPLETE_PROFILE: String = "profileCompleted"

    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val CASHIER_PROFILE_IMAGE: String = "Cashier_Profile_Image"

    const val STOCK_QUANTITY: String = "stock_quantity"

    const val EXTRA_MY_ORDER_DETAILS: String = "extra_MY_ORDER_DETAILS"

    const val qr_code: String = "qr_code"

    const val barcode: String = "barcode"

    const val prod_name: String = "prod_name"



    fun showImageChooser(activity: Activity){
        val galleryIntent= Intent (
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtention(activity: Activity, uri: Uri?): String? {

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}
