package com.example.speedometerfinal.View

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.speedometerfinal.MyApplication
import com.example.speedometerfinal.R
import com.example.speedometer.model.GpsData
import com.example.speedometer.model.GpsRepo
import com.example.speedometerfinal.ViewModel.GpsViewModel
import com.example.speedometerfinal.ViewModel.GpsViewModelFactory
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import android.car.Car


class MapActivity : AppCompatActivity() {

    lateinit var gpsRepo: GpsRepo
    private lateinit var mapView: MapView
    private lateinit var imageView3: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialize MapView and ImageView
        mapView = findViewById(R.id.mapView)!!
        imageView3 = findViewById(R.id.imageView3)!!
        
        val car = Car.createCar(this)
        if (car != null) {
            gpsRepo = GpsRepo(car)
        } else {
            
        }
        val factoryGps = GpsViewModelFactory(gpsRepo)
        val gpsViewModel = ViewModelProvider(this, factoryGps).get(GpsViewModel::class.java)
    

        // Setup MapView
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        mapView.apply {
            setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }

        // ImageView click listener to go back to MainActivity
        imageView3.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Collect GPS data and update map
        lifecycleScope.launchWhenStarted {
            gpsViewModel.gpsDataState.collect { gpsData ->
                gpsData?.let { updateMap(it) }
            }
        }
    }

    private fun updateMap(gpsData: GpsData) {
        val geoPoint = org.osmdroid.util.GeoPoint(gpsData.lat.toDouble(), gpsData.lng.toDouble())

        mapView.controller.apply {
            setCenter(geoPoint)
            setZoom(20)
        }

        val marker = Marker(mapView).apply {
            position = geoPoint
            title = "Your Location"
        }

        if (mapView.overlays.isEmpty()) {
            mapView.overlays.add(marker)
        } else {
            (mapView.overlays[0] as Marker).apply {
                position = geoPoint
                title = "Your Location"
            }
        }

        mapView.invalidate()
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()  // Resume the map view
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()  // Pause the map view
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDetach()  // Detach the map view
    }
}
