package com.fyp.barcodebasedpaymentsystem.all.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem (
    val user_id: String = "",
    val product_id: String = "",
    val productname: String = "",
    val price: String = "",
    val imageurl: String = "",
    var cart_quantity: String = "",
    var stock_quantity: String = "",
    var id: String = "",

) : Parcelable