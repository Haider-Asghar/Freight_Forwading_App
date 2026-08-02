package com.example.ft

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AirRatesModel(
    val airline: String,
    val freight: String,
    val exWork: String,
    val doCharges: String,
    val transitTime: String
) : Parcelable
