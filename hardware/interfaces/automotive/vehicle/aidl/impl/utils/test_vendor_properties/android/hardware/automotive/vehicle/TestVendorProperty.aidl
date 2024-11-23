package android.hardware.automotive.vehicle;

/**
 * Test vendor properties used in reference VHAL implementation.
 */
@Backing(type="int")
enum TestVendorProperty {

    /**
     * Vendor version of ITI, used for Lat
     */
    ITI_GPS_LAT = 0x0F28 + 0x20000000 + 0x01000000 + 0x00400000,
     /**
     * Vendor version of ITI, used for Long
     */
    ITI_GPS_LONG = 0x0F29 + 0x20000000 + 0x01000000 + 0x00400000,
    /**
     * Vendor version of ITI, used for Speed
     */
    ITI_GPS_SPEED = 0x0F30 + 0x20000000 + 0x01000000 + 0x00400000,

	/**
     * Vendor version of ITI, used for Gpio
     */
    ITI_GPIO = 0x0F31 + 0x20000000 + 0x01000000 + 0x00400000

}
