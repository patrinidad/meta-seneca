SUMMARY = "This is a base image created as example for courses"

inherit core-image

IMAGE_LINGUAS = " "

LICENSE = "MIT"

IMAGE_BOOT_FILES = "${SPL_BINARYNAME} u-boot.${UBOOT_SUFFIX} tiboot3.bin k3-am625-beagleplay.dtb"

DISTRO_FEATURES:append = " usbgadget wayland opengl pam x11"
CORE_IMAGE_BASE_INSTALL += "gtk+3-demo"
CORE_IMAGE_BASE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'weston-xwayland matchbox-terminal', '', d)}"

IMAGE_INSTALL:append = " \
    usbutils \
    usbinit \
    i2c-tools \
    libgpiod \
    labtest \
    weston \
    matchbox-keyboard \
    cairo \
    libgbm \
    libdrm \
    wayland \
    mesa \
    simple-library \
    libiio \
  	beagleplay-qwiic-accel \
  	iio-accel-read \
  	snake-accel \
  	python3 \
  	python3-curses \
    "

IMAGE_FEATURES:append = " \
    ssh-server-openssh \
    splash \
    tools-debug \
    tools-sdk \
    "

DISTRO_FEATURES:append = " usbgadget"
MACHINE_FEATURES:append = " usbgadget usbhost"
