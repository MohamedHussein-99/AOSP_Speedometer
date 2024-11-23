# For GPIO Service with aidl
#####################################################################
PRODUCT_PACKAGES += \    
    Gpioapp \
    gpio-service \

BOARD_VENDOR_SEPOLICY_DIRS += device/brcm/rpi4/speedometer/sepolicy/vendor 
BOARD_VENDOR_SEPOLICY_DIRS += device/brcm/rpi4/speedometer/sepolicy/hal 
BOARD_VENDOR_SEPOLICY_DIRS += device/brcm/rpi4/speedometer/sepolicy/service 
BOARD_VENDOR_SEPOLICY_DIRS += device/brcm/rpi4/speedometer/sepolicy/daemon 
BOARD_VENDOR_SEPOLICY_DIRS += device/brcm/rpi4/speedometer/sepolicy/app

# ########## Device Manifest & Framework Compatibility Matrix Manifest ##########
DEVICE_FRAMEWORK_COMPATIBILITY_MATRIX_FILE += speedometer/gpio_service/manifest/gpio_framework_compatibility_matrix.xml \
