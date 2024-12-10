package com.example.speedometer.model

import android.os.Parcel
import android.os.Parcelable

data class GpsData(
    var lat: Float,
    var lng: Float,
    var speed: Float
)