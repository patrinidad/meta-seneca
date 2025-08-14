DESCRIPTION = "Minimal IIO accelerometer reader"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://iio-accel-read.c"
S = "${WORKDIR}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -O2 -Wall iio-accel-read.c -o iio-accel-read
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 iio-accel-read ${D}${bindir}/iio-accel-read
}

