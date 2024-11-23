#include "GpioImpl.h"

#include <android-base/logging.h>
#include <android/binder_manager.h>
#include <android/binder_process.h>

using aidl::com::luxoft::gpio::GpioImpl;

int main() {
    LOG(INFO) << "Gpio daemon started!";

    ABinderProcess_setThreadPoolMaxThreadCount(0);
    std::shared_ptr<GpioImpl> gpio = ndk::SharedRefBase::make<GpioImpl>();

    const std::string instance = std::string() + GpioImpl::descriptor + "/default";
    binder_status_t status = AServiceManager_addService(gpio->asBinder().get(), instance.c_str());
    CHECK_EQ(status, STATUS_OK);

    ABinderProcess_joinThreadPool();
    return EXIT_FAILURE;  // should not reached
}