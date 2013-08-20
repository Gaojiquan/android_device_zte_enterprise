$(call inherit-product, $(SRC_TARGET_DIR)/product/languages_full.mk)

$(call inherit-product-if-exists, vendor/zte/enterprise/enterprise-vendor.mk)

DEVICE_PACKAGE_OVERLAYS += device/zte/enterprise/overlay

PRODUCT_TAGS += dalvik.gc.type-precise
$(call inherit-product, frameworks/native/build/phone-xhdpi-1024-dalvik-heap.mk)

# This device is xhdpi
PRODUCT_AAPT_CONFIG := normal hdpi xhdpi
PRODUCT_AAPT_PREF_CONFIG := xhdpi

## default language

PRODUCT_LOCALES += zh_CN xhdpi

LOCAL_PATH := device/zte/enterprise

## Kernel

ifeq ($(TARGET_PREBUILT_KERNEL),)
	LOCAL_KERNEL := $(LOCAL_PATH)/kernel
else
	LOCAL_KERNEL := $(TARGET_PREBUILT_KERNEL)
endif

PRODUCT_COPY_FILES += \
    $(LOCAL_KERNEL):kernel

## Boot animation

TARGET_SCREEN_HEIGHT := 1280
TARGET_SCREEN_WIDTH := 720

## adb + root + vold

ADDITIONAL_DEFAULT_PROPERTIES += \
	ro.secure=0 \
	ro.allow.mock.location=0 \
	ro.adb.secure=0 \
	ro.debuggable=1 \
	persist.sys.usb.config=mass_storage \
	persist.sys.vold.switchexternal=1

PRODUCT_PROPERTY_OVERRIDES += \
	ro.sf.lcd_density=320 \
	ro.telephony.ril_class=EnterpriseRIL \
	wifi.interface=wlan0

## Ramdisk

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/ramdisk/init.tegra_enterprise.rc:root/init.tegra_enterprise.rc \
    $(LOCAL_PATH)/ramdisk/ueventd.tegra_enterprise.rc:root/ueventd.tegra_enterprise.rc \
    $(LOCAL_PATH)/ramdisk/fstab.tegra_enterprise:root/fstab.tegra_enterprise \
    $(LOCAL_PATH)/ramdisk/init.tegra_enterprise.usb.rc:root/init.tegra_enterprise.usb.rc


## Prebuilt

PRODUCT_COPY_FILES += \
    $(call find-copy-subdir-files,*,$(LOCAL_PATH)/prebuilt/system,system)

$(call inherit-product, build/target/product/full.mk)

## Permission files

PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/handheld_core_hardware.xml:system/etc/permissions/handheld_core_hardware.xml \
    frameworks/native/data/etc/android.hardware.camera.flash-autofocus.xml:system/etc/permissions/android.hardware.camera.flash-autofocus.xml \
    frameworks/native/data/etc/android.hardware.camera.front.xml:system/etc/permissions/android.hardware.camera.front.xml \
    frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml \
    frameworks/native/data/etc/android.hardware.location.gps.xml:system/etc/permissions/android.hardware.location.gps.xml \
    frameworks/native/data/etc/android.hardware.wifi.xml:system/etc/permissions/android.hardware.wifi.xml \
    frameworks/native/data/etc/android.hardware.sensor.proximity.xml:system/etc/permissions/android.hardware.sensor.proximity.xml \
    frameworks/native/data/etc/android.hardware.sensor.light.xml:system/etc/permissions/android.hardware.sensor.light.xml \
    frameworks/native/data/etc/android.hardware.sensor.gyroscope.xml:system/etc/permissions/android.hardware.sensor.gyroscope.xml \
    frameworks/native/data/etc/android.hardware.sensor.compass.xml:system/etc/permissions/android.hardware.sensor.compass.xml \
    frameworks/native/data/etc/android.hardware.nfc.xml:system/etc/permissions/android.hardware.nfc.xml \
    frameworks/native/data/etc/android.software.sip.voip.xml:system/etc/permissions/android.software.sip.voip.xml \
    frameworks/native/data/etc/android.hardware.touchscreen.multitouch.jazzhand.xml:system/etc/permissions/android.hardware.touchscreen.multitouch.jazzhand.xml \
    frameworks/native/data/etc/android.hardware.usb.accessory.xml:system/etc/permissions/android.hardware.usb.accessory.xml \
    frameworks/native/data/etc/android.hardware.wifi.direct.xml:system/etc/permissions/android.hardware.wifi.direct.xml

# Charger mode
PRODUCT_PACKAGES += \
    charger \
    charger_res_images

PRODUCT_PACKAGES += \
    audio.a2dp.default \
	audio.usb.default \
	audio.r_submix.default \
	libaudioutils \
    com.android.future.usb.accessory \
	libnetcmdiface \
	make_ext4fs \
    setup_fs \
	chat

## PPPD Chat

PRODUCT_PACKAGES += \
	chat

## Camera Wrapper

PRODUCT_PACKAGES += \
	camera.tegra

## Advance Setting

PRODUCT_PACKAGES += \
	EnterpriseParts

## Lights

PRODUCT_PACKAGES += \
	lights.tegra_enterprise

# NFC packages
PRODUCT_PACKAGES += \
    libnfc \
    libnfc_jni \
    Nfc \
    Tag

PRODUCT_BUILD_PROP_OVERRIDES += BUILD_UTC_DATE=8
PRODUCT_NAME := full_enterprise
PRODUCT_DEVICE := enterprise
PRODUCT_MODEL := ZTE-U5
PRODUCT_MANUFACTURER := ZTE
