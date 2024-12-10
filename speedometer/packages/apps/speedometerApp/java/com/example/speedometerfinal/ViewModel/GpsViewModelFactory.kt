package com.example.speedometerfinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.speedometer.model.GpsRepo

class GpsViewModelFactory(private val gpsRepo: GpsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GpsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GpsViewModel(gpsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
