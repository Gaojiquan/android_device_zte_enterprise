## Specify phone tech before including full_phone
$(call inherit-product, vendor/cm/config/gsm.mk)

TARGET_BOOTANIMATION_NAME := vertical-720x1280

# Release name
PRODUCT_RELEASE_NAME := enterprise

# Inherit some common CM stuff.
$(call inherit-product, vendor/cm/config/common_full_phone.mk)

# This device has NFC
$(call inherit-product, vendor/cm/config/nfc_enhanced.mk)

# Inherit device configuration
$(call inherit-product, device/zte/enterprise/enterprise.mk)

## Device identifier. This must come after all inclusions
PRODUCT_RELEASE_NAME := enterprise
PRODUCT_DEVICE := enterprise
PRODUCT_NAME := cm_enterprise
PRODUCT_BRAND := zte
PRODUCT_MANUFACTURER := ZTE

PRODUCT_BUILD_PROP_OVERRIDES += PRODUCT_NAME=enterprise BUILD_FINGERPRINT=zte/enterprise/enterprise:4.2.2/JDQ39E/eng.ztetd.20130424.160313:user/release-keys PRIVATE_BUILD_DESC="enterprise-user 4.2.2 JDQ39E eng.ztetd.20130424.160313 release-keys"

# Enable Torch
PRODUCT_PACKAGES += Torch
