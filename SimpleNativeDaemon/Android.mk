LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng 

LOCAL_SRC_FILES:= $(call all-java-files-under,src)

LOCAL_JNI_SHARED_LIBRARIES := $(LOCAL_PATH)/jniLibs/libmyLib

LOCAL_PROGUARD_ENABLED := disabled


LOCAL_RESOURCE_DIR := $(LOCAL_PATH)/res

LOCAL_PACKAGE_NAME := SimpleNativeDaemon
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE :=true




include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))




