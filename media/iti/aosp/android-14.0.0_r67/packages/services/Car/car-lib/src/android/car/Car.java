/**
 *   Top level car API for embedded Android Auto deployments.
 *   This API works only for devices with {@link PackageManager#FEATURE_AUTOMOTIVE}
 *   Calling this API on a device with no such feature will lead to an exception.
 */
public final class Car implements ICarBase {



     /**
     * @hide
     */
    @FlaggedApi(Flags.FLAG_DISPLAY_COMPATIBILITY)
    @SystemApi
    public static final String PERMISSION_ITI_GPS = "android.car.permission.ITI.GPS";
    

    /**
     * @hide
     */
    @FlaggedApi(Flags.FLAG_DISPLAY_COMPATIBILITY)
    @SystemApi
    public static final String PERMISSION_ITI = "android.car.permission.ITI";

}
