LOCAL_PATH               := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := eng

LOCAL_SRC_FILES          := myLib.c
LOCAL_MODULE             := myLib

LOCAL_SHARED_LIBRARIES := \
	libutils liblog libcutils

LOCAL_C_INCLUDES += \
	$(JNI_H_INCLUDE)

LOCAL_CFLAGS += -O0 -g3


include $(BUILD_SHARED_LIBRARY)
