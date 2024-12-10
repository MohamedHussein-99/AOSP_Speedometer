package com.example.speedometerfinal.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speedometer.model.GpsRepo
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.speedometer.model.GpsData

class GpsViewModel(private val gpsRepo: GpsRepo) : ViewModel() {


private val _gpsDataState = MutableStateFlow<GpsData?>(null)
val gpsDataState: StateFlow<GpsData?> get() = _gpsDataState


init {
    viewModelScope.launch {
        gpsRepo.gpsDataFlow.collect { gpsData ->
            _gpsDataState.value = gpsData
        }
    }
    viewModelScope.launch {
        gpsRepo.updateGpsData() // Start data updates in a coroutine
    }
}


}
