package com.luxoft.gpioapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.luxoft.gpio.IGpio;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    IGpio gpio ;
    TextView txt;
    Button on, off;
    boolean retFlag = false ;
    boolean getValue = false;
    //SeekBar seekBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt = findViewById(R.id.txt);
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);

        Class localClass = null;
        
        
    try{


            localClass = Class.forName("android.os.ServiceManager");
            Method getService = localClass.getMethod("getService", new Class[] {String.class});
            if(getService != null) {
                Object result = getService.invoke(localClass, new Object[]{"com.luxoft.gpio.IGpio/default"});

                /*if (result != null) {
    IBinder binder = (IBinder) result;
    gpio = IGpio.Stub.asInterface(binder);
    if (gpio == null) {
        Log.e("MainActivity", "Failed to get GPIO interface from binder!");
    } else {
        Log.i("MainActivity", "Successfully connected to GPIO service!");
    }
} else {
    Log.e("MainActivity", "ServiceManager.getService returned null for IGpio/default");
}
*/ 
                if(result != null) {
                    IBinder binder = (IBinder) result;
                    gpio = IGpio.Stub.asInterface(binder);
                 if (gpio == null) {
        Log.e("MainActivity", "Failed to get GPIO interface from binder!");
    } else {
        Log.i("MainActivity", "Successfully connected to GPIO service!");
    }

                on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        boolean mySetState = gpio.setGpioState(18, true);
                        if(mySetState)
                        {
                            Toast.makeText(MainActivity.this, "Button clicked Ok!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Button Not OK!", Toast.LENGTH_SHORT).show();
                        }
                        boolean myGetState = gpio.getGpioState(18);
                        txt.setText(Boolean.toString(myGetState));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                        }
                    });


                    
            off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        boolean mySetState = gpio.setGpioState(18, false);
                        Toast.makeText(MainActivity.this, "Button clicked!", Toast.LENGTH_SHORT).show();
                        boolean myGetState = gpio.getGpioState(18);
                        txt.setText(Boolean.toString(myGetState));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                        }
                    });
                }
                else {
    Log.e("MainActivity", "ServiceManager.getService returned null for IGpio/default");
}
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}