package com.example.speedometer.model

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.property.CarPropertyManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Ensure GpsRepo implements IGpsRepo
class GpsRepo(private val car: Car) : IGpsRepo {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val carPropertyManager: CarPropertyManager by lazy {
        car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
    }

    private val areaId = 16777216 // Replace with your area ID
    private val latitudePropertyId = VehiclePropertyIds.ITI_GPS_LAT
    private val longitudePropertyId = VehiclePropertyIds.ITI_GPS_LONG
    private val speedPropertyId = VehiclePropertyIds.ITI_GPS_SPEED

    private val _gpsDataFlow = MutableStateFlow<GpsData>(GpsData(1.1f, 1.1f, 1.1f)) // Default fallback value
    val gpsDataFlow: StateFlow<GpsData> get() = _gpsDataFlow.asStateFlow()

    private var lastValidGpsData: GpsData = GpsData(0.1f, 0.1f, 0.1f)
    val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    override fun getGpsData(): StateFlow<GpsData> = gpsDataFlow


    suspend fun updateGpsData() {
        CoroutineScope.launch(Dispatchers.IO) {
            while (true) {
            try {
                val latitude = getProperty(latitudePropertyId)
                val longitude = getProperty(longitudePropertyId)
                val speed = getProperty(speedPropertyId)

                val gpsData = GpsData(latitude, longitude, speed)
                Log.d("GpsRepo", "Updating GPS data: $gpsData")
                _gpsDataFlow.value = gpsData

            } catch (e: Exception) {
                Log.e("GpsRepo", "Failed to retrieve GPS data", e)
                _gpsDataFlow.value = GpsData(1.1f, 1.1f, 1.1f) // Default fallback
            }
        }
        }
    }

    private fun getProperty(propertyId: Int): Float {
        return try {
        val areaId = carPropertyManager.getAreaId(propertyId, areaId)
        val propertyValue = carPropertyManager.getProperty(java.lang.Float::class.java, propertyId, areaId)
        Log.d("GpsRepo", "Emitting try areaId :" + areaId +"propertyValue" + propertyValue)
        (propertyValue?.value ?: 0.0f).toFloat()
        } catch (e: Exception) {
                Log.e("GpsRepo", "Failed to retrieve property: $propertyId", e)
                 0.0f // Return a default value
        }
    }
}
