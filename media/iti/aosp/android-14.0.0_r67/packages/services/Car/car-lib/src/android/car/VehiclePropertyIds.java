/**
 * List of vehicle property IDs.
 *
 * <p> Property IDs are used with the {@link android.car.hardware.property.CarPropertyManager} APIs
 * (e.g. {@link android.car.hardware.property.CarPropertyManager#getProperty(int, int)} or {@link
 * android.car.hardware.property.CarPropertyManager#setProperty(Class, int, int, Object)}).
 */
public final class VehiclePropertyIds {



    @FlaggedApi(FLAG_ANDROID_VIC_VEHICLE_PROPERTIES)
    @RequiresPermission(Car.PERMISSION_ITI_GPS)
    public static final int ITI_GPS_LAT = 557846312;
    @FlaggedApi(FLAG_ANDROID_VIC_VEHICLE_PROPERTIES)
    @RequiresPermission(Car.PERMISSION_ITI_GPS)
    public static final int ITI_GPS_LONG = 557846313;
    @FlaggedApi(FLAG_ANDROID_VIC_VEHICLE_PROPERTIES)
    @RequiresPermission(Car.PERMISSION_ITI_GPS)
    public static final int ITI_GPS_SPEED = 557846320;

}
