package com.fyp.barcodebasedpaymentsystem.all.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    val user_id: String = "",
    val user_name: String = "",
    val productname: String = "",
    val price: String = "",
    val datestocked: String = "",
    val stock_quantity: String = "",
    val no:String = "", //barcode
    val category: String ="",
    val search_field:String ="", //search
    val imageurl: String = "",
    var product_id: String = ""


): Parcelable