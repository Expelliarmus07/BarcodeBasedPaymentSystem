package com.fyp.barcodebasedpaymentsystem.all.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<CartItem> = ArrayList(),
    val productname: String = "",
    val imageurl: String = "",
    val sub_total_amount: String = "",
    val tax: String = "",
    val total_amount: String = "",
    val order_datetime: Long = 0L,
    var id: String = "",
    var verified: Int= 0,
    var cashierName: String=""
) : Parcelable