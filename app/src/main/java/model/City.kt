package model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val name: String,
    val lat: Double,
    val lon: Double
) : Parcelable