#pragma once

#include <aidl/com/luxoft/gpio/BnGpio.h>
#include <mutex>


namespace aidl {
namespace com {
namespace luxoft {
namespace gpio {


class GpioImpl : public BnGpio {
    //virtual ndk::ScopedAStatus getPin(int32_t* _aidl_return) override;
    //virtual ndk::ScopedAStatus setPin(int32_t* _aidl_return) override;
    virtual ndk::ScopedAStatus setGpioState(int32_t in_pin, bool in_value, bool* _aidl_return) override;
    virtual ndk::ScopedAStatus getGpioState(int32_t pin, bool* _aidl_return) override;
 //  virtual ndk::ScopedAStatus setGpioState(int32_t pin, bool value) override;
  // virtual ndk::ScopedAStatus getGpioState(int32_t pin, bool *_aidl_return) override;
protected:

    ::ndk::ScopedAIBinder_DeathRecipient death_recipient_;
    static void binderDiedCallbackAidl(void* cookie_ptr);

};

}  // namespace gpio
}  // namespace hardware
}  // namespace aospinsight
}  // namespace aidl
