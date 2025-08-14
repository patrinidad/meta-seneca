DESCRIPTION = "accelerometer qwiic"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://beagleplay-qwiic-accel.dts"
S = "${WORKDIR}"

DEPENDS = "dtc-native"

do_compile() {
    ${STAGING_BINDIR_NATIVE}/dtc -@ -I dts -O dtb \
      -o beagleplay-qwiic-accel.dtbo ${S}/beagleplay-qwiic-accel.dts
}

do_install() {
    install -d ${D}/boot/overlays
    install -m 0644 beagleplay-qwiic-accel.dtbo ${D}/boot/overlays/
}

FILES:${PN} = "/boot/overlays/beagleplay-qwiic-accel.dtbo"
