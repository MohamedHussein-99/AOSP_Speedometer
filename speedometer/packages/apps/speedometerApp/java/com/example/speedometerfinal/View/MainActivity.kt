package com.example.speedometerfinal.View

import android.car.Car
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.example.speedometer.model.GpsRepo
import com.luxoft.gpio.IGpio
import com.example.speedometerfinal.R
import com.example.speedometerfinal.ViewModel.GpsViewModel
import com.example.speedometerfinal.ViewModel.GpsViewModelFactory
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.os.IBinder
import java.lang.reflect.InvocationTargetException
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

class MainActivity : AppCompatActivity() {

    private lateinit var speedometerView: SpeedoMeterView
    private lateinit var seekBar: SeekBar
    private lateinit var tvSpeed: TextView
    private lateinit var imageView: ImageView
    private lateinit var txtTime: TextView
    private lateinit var txtDate: TextView
    private lateinit var speedlimitred: ImageView
    private lateinit var speedlimitgray: ImageView
    lateinit var gpsRepo: GpsRepo
    private lateinit var gpio: IGpio
    private lateinit var imageView4 : ImageView // for webView nav

  //  val perms = {"android.car.permission.ITI" , "android.car.permission.ITI_GPS"}
    val perms = arrayOf("android.car.permission.ITI", "android.car.permission.ITI_GPS")
    val permsRequestCode = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // Class localClass = null;
        try {
            val localClass = Class.forName("android.os.ServiceManager")
            val getService = localClass.getMethod("getService", String::class.java)

            val result = getService.invoke(localClass, "com.luxoft.gpio.IGpio/default")

            if (result != null) {
                val binder = result as IBinder
                gpio = IGpio.Stub.asInterface(binder)
                if (gpio == null) {
                    Log.e("MainActivity", "Failed to get GPIO interface from binder!")
                } else {
                    Log.i("MainActivity", "Successfully connected to GPIO service!")
                }
            } else {
                Log.e("MainActivity", "Service not available!")
            }
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("ServiceManager class not found", e)
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("getService method not found", e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Illegal access to getService", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Error invoking getService", e)
        } catch (e: Exception) {
            throw RuntimeException("Unexpected exception", e)
        }
        // Initialize car and gpsRepo
        val car = Car.createCar(this)
        if (car != null) {
        gpsRepo = GpsRepo(car)
        } else {
            
        }

        val factorySpeed = GpsViewModelFactory(gpsRepo)
        val gpsViewModel = ViewModelProvider(this, factorySpeed).get(GpsViewModel::class.java)

        speedometerView = findViewById<SpeedoMeterView>(R.id.speedometerview)!!
        seekBar = findViewById<SeekBar>(R.id.seekBar)!!
        tvSpeed = findViewById<TextView>(R.id.tvSpeed)!!
        imageView = findViewById<ImageView>(R.id.imageView)!!
        txtTime = findViewById(R.id.txtTime)!!
        txtDate = findViewById(R.id.txtDate)!!
        speedlimitred = findViewById(R.id.speedlimitred)!!
        speedlimitgray = findViewById(R.id.speedlimitgray)!!
        imageView4 = findViewById(R.id.imageView4)!! // for webView nav

         // Set current time and date
        lifecycleScope.launch {
            while (true) {
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            txtTime.text = timeFormat.format(Date())
            txtDate.text = dateFormat.format(Date())
            delay(1000)
        }}

        imageView.apply {
            //visibility = ImageView.INVISIBLE // Make the image invisible
            isClickable = true // Ensure it is still clickable
            setOnClickListener {
                val intent = Intent(this@MainActivity, MapActivity::class.java)
                startActivity(intent)
            }
        }
        // for webView nav
        imageView4.setOnClickListener {
            val intent = Intent(this@MainActivity, WebActivity::class.java)
            startActivity(intent)
        }


    
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            gpsViewModel.gpsDataState.collect { gpsData ->
                gpsData?.let {
                    tvSpeed.text = "${it.speed.toInt()} km/h"
                    speedometerView.setSpeed(it.speed.toInt(), true)
                    Log.d("MainActivity", "Received GPS Data: $it")
    
                    // Toggle visibility of speed limit images
                    if (it.speed.toInt() > 28) {
                        // Start toggling red light
                        launch {
                            while (it.speed.toInt() > 28) {
                                speedlimitred.visibility = ImageView.VISIBLE
                                speedlimitgray.visibility = ImageView.GONE
                                gpio.setGpioState(18, true) // Turn GPIO pin 18 on
                                delay(800)
                                
                                speedlimitgray.visibility = ImageView.VISIBLE
                                speedlimitred.visibility = ImageView.GONE
                                gpio.setGpioState(18, false) // Turn GPIO pin 18 off
                                delay(800)
                            }
                        }
                    } else {
                        speedlimitgray.visibility = ImageView.VISIBLE
                        speedlimitred.visibility = ImageView.GONE
                    }
                }
            }
        }
    }
    

        // Set SeekBar change listener
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update the TextView with the current speed
                tvSpeed.text = "$progress km/h"
                // Update the speedometer with the new speed value
                speedometerView.setSpeed(progress, true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        requestPermissions(perms, permsRequestCode);
    }


    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults)

    
    if (permsRequestCode == 200) {
        val carPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        if (carPermission) {
            Log.d("GpsRepo", "car permission done")
        } else {
            requestPermissions(perms, permsRequestCode)
        }
    }
}

override fun onDestroy() {
    super.onDestroy()
    
    // Shutdown the ExecutorService when the activity is destroyed
    gpsRepo.executorService.shutdown()
    
    // check if it's terminated successfully
    if (!gpsRepo.executorService.isTerminated) {
        gpsRepo.executorService.shutdownNow()
    }
}

    
}
