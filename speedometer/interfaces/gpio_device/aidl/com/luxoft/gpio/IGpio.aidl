package com.luxoft.gpio;

@VintfStability
interface IGpio{
    /**
     * Set the GPIO state for a specific pin.
     * @param pin The GPIO pin number.
     * @param value The state to set (true for HIGH, false for LOW).
     * @return True if successful, false otherwise.
     */
    boolean setGpioState(int pin, boolean value);

    /**
     * Get the GPIO state for a specific pin.
     * @param pin The GPIO pin number.
     * @return The state of the pin (true for HIGH, false for LOW).
     */
    boolean getGpioState(int pin);
}