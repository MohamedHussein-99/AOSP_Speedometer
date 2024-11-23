
// at maybeSetSoecialValue function : 
// To test Properties
        case toInt(TestVendorProperty::ITI_GPS_LAT): {
        ALOGD("ITI LAT Done!");
        }
            return {};


        case toInt(TestVendorProperty::ITI_GPS_LONG): {
        ALOGD("ITI LONG Done!");
        }
            return {};


        case toInt(TestVendorProperty::ITI_GPS_SPEED): {
        ALOGD("ITI GPS SPEED Done!");
        }
        return {};
/**********************************************************/ 


// at maybeGetSpecialValue //
case toInt(TestVendorProperty::ITI_GPS_LAT): {
        ALOGD("ITI GPS LAT");

    
       // ADD our Code 
         // Prepare the result with the GPIO status
        result = mValuePool->obtainInt32(static_cast<int32_t>(0));
        result.value()->prop = propId;
        result.value()->areaId = 0;
        result.value()->timestamp = elapsedRealtimeNano();
        }
        return result;


        case toInt(TestVendorProperty::ITI_GPS_LONG): {
        ALOGD("ITI GPS Long");


        // ADD our Code 
        // Prepare the result with the GPIO status
        result = mValuePool->obtainInt32(static_cast<int32_t>(0));
        result.value()->prop = propId;
        result.value()->areaId = 0;
        result.value()->timestamp = elapsedRealtimeNano();
        }
        return result;
         case toInt(TestVendorProperty::ITI_GPS_SPEED): {
        ALOGD("ITI GPS Speed");

        // ADD our Code 
       
         // Prepare the result with the GPIO status
        result = mValuePool->obtainInt32(static_cast<int32_t>(0));
        result.value()->prop = propId;
        result.value()->areaId = 0;
        result.value()->timestamp = elapsedRealtimeNano();
        }
        return result;
