#include "GpioImpl.h"
#include "lib/GpioHal.h"
#include <android-base/logging.h>


namespace aidl {
namespace com {
namespace luxoft {
namespace gpio {


ndk::ScopedAStatus GpioImpl::setGpioState(int32_t pin, bool value, bool* _aidl_return) {
    LOG(INFO) << "Setting GPIO state for pin: " << pin << " to value: " << value;
    GpioHal gpioHal;
    bool success = gpioHal.exportGpio(pin) &&
                   gpioHal.setGpioDirection(pin, "out") &&
                   gpioHal.setGpioValue(pin, value);
    return success ? ndk::ScopedAStatus::ok() : ndk::ScopedAStatus::fromStatus(STATUS_UNKNOWN_ERROR);
}

ndk::ScopedAStatus GpioImpl::getGpioState(int32_t pin, bool *_aidl_return) {
    LOG(INFO) << "Getting GPIO state for pin: " << pin;
    GpioHal gpioHal;
    bool value;
    bool success = gpioHal.getGpioValue(pin, value);
    if (!success) return ndk::ScopedAStatus::fromStatus(STATUS_UNKNOWN_ERROR);
    *_aidl_return = value;
    return ndk::ScopedAStatus::ok();
}
}  // namespace gpio
}  // namespace hardware
}  // namespace aospinsight
}  // namespace aidl
