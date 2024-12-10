package com.luxoft.gpio;

interface IGpio{
    boolean setGpioState(int pin, boolean value);
    boolean getGpioState(int pin );
   // int getNumber();
}