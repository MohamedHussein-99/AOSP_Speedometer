package com.example.speedometer.model

import kotlinx.coroutines.flow.Flow

interface IGpsRepo {
    fun getGpsData(): Flow<GpsData>
}