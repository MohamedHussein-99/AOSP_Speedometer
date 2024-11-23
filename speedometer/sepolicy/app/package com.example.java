package com.example.newkitchen;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.car.Car ;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.car.VehiclePropertyIds ;
import android.widget.TextView;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    private SeekBar rpmBar;
    private TextView ledState;
    private ImageView rainSt;
    private Car car;
    private CarPropertyManager carPropertyManager;
    private Thread speedPool;
    private Thread rainPool;
    private volatile boolean isMonitoring;

    private boolean value = false;
    private String[] permissions = {"android.car.permission.CAR_VENDOR_EXTENSION"};
    private int statusCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        rpmBar = findViewById(R.id.sliderRpm);
        ledState = findViewById(R.id.textView);
        rainSt = findViewById(R.id.rainy);



        rpmBar.setMax(32767);

        requestPermissions(permissions, statusCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (statusCode) {
            case 200:
                Log.i("KZ", "on permission switch case");
                boolean carPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (carPermission) {
                    Log.i("KZ", "on permission granted");
                    try {
                        car = Car.createCar(this);
                        carPropertyManager = (CarPropertyManager) car.getCarManager(Car.PROPERTY_SERVICE);
                        Log.d("KZ", "onRequestPermissionsResult: isConnected " + car.isConnected());

                        Display();
                    } catch (Exception e) {
                        Log.e("KZ", "Failed to initialize Car API", e);
                    }
                } else {
                    Log.i("KZ", "on permission not granted");
                    requestPermissions(permissions, statusCode);
                }
                break;
            default:
                Log.d("KZ", "onRequestPermissionsResult: default " + statusCode);
                break;
        }
    }

    private void Display() {
        isMonitoring = true;

        // RPM Monitoring 
        speedPool = new Thread(() -> {
            while (isMonitoring) {
                try {
                    CarPropertyValue<?> rpmValue = carPropertyManager.getProperty(Integer.class, VehiclePropertyIds.VENDOR_EXTENSION_I2CINT_PROPERTY, 0);
                    if (rpmValue != null) {
                        int rpm = (Integer) rpmValue.getValue();
                        Log.i("KZ", "RPM value polled: " + rpm);
                        runOnUiThread(() -> rpmBar.setProgress(rpm));
                    }
                    Thread.sleep(300); 
                } catch (Exception e) {
                    Log.e("KZ", "Error in RPM monitoring thread", e);
                }
            }
        });
        speedPool.start();

rainPool = new Thread(() -> {
    while (isMonitoring) {
        try {
            CarPropertyValue<?> rainValue = carPropertyManager.getProperty(Boolean.class, VehiclePropertyIds.VENDOR_EXTENSION_RAINBOOLEAN_PROPERTY, 0);
            if (rainValue != null) {
                boolean rainDetected = (Boolean) rainValue.getValue();
                Log.i("KZ", "Rain detected: " + rainDetected);
                runOnUiThread(() -> ledState.setText(rainDetected ? "Rain Detected" : "No Rain"));

               runOnUiThread(() -> {
                    ledState.setText(rainDetected ? "Rain Detected" : "No Rain");
                    rainSt.setImageDrawable(getResources().getDrawable(rainDetected ? R.drawable.red : R.drawable.yel));
                });
            } else {
                Log.e("KZ", "Rain property value is null.");
            }
            
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e("KZ", "Rain monitoring thread interrupted.", e);
            Thread.currentThread().interrupt();
        } catch (NullPointerException e) {
            Log.e("KZ", "NullPointerException while accessing car property value", e);
        } catch (Exception e) {
            Log.e("KZ", "Error in Rain monitoring thread", e);
        }
    }
});
rainPool.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isMonitoring = false;

        try {
            if (speedPool != null) {
                speedPool.join();
            }
            if (rainPool != null) {
                rainPool.join();
            }
        } catch (InterruptedException e) {
            Log.e("KZ", "Error stopping monitoring threads", e);
        }

        if (car != null) {
            car.disconnect();
            Log.i("KZ", "Car disconnected");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (car != null) {
            car.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (car != null && car.isConnected() && carPropertyManager != null && !isMonitoring) {
            Display();
        }
    }
}