package com.fyp.barcodebasedpaymentsystem.all.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Staff(
    val id1: String = "",
    val name1: String = "",
    val email1: String = "",
    val image1: String = "",
    val mobile1: Long = 0,
    val gender1: String = "",
    val profileCompleted: Int= 0
): Parcelable