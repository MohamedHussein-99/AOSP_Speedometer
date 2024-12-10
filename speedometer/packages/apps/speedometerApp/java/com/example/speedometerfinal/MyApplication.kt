package com.example.speedometerfinal

import android.app.Application
import android.car.Car
import com.example.speedometer.model.GpsRepo

class MyApplication : Application() {

    lateinit var gpsRepo: GpsRepo

    override fun onCreate() {
        super.onCreate()
        // Initialize the gpsRepo here with a dummy Car object or your actual data source
        gpsRepo = GpsRepo(Car.createCar(this)!!) // Example, replace with your actual initialization
    }
}
