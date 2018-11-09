LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng 

LOCAL_SRC_FILES:= $(call all-java-files-under,src)

LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_PACKAGE_NAME := ShiftCharService
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE :=true


#LOCAL_STATIC_JAVA_LIBRARIES += android-support-v7-appcompat

#LOCAL_AAPT_FLAGS += --extra-packages android.support.v7.appcompat
 
# LOCAL_REQUIRED_MODULES := com.vst.testService
# LOCAL_JAVA_LIBRARIES := framework
# LOCAL_SDK_VERSION := current

LOCAL_STATIC_JAVA_LIBRARIES := \
    android-support-v4 \
    android-support-v13 \
    telephony-ext

include frameworks/base/packages/SettingsLib/common.mk

include $(BUILD_PACKAGE)
