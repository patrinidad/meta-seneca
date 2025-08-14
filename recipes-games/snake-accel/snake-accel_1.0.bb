DESCRIPTION = "Terminal Snake controlled by accelerometer (IIO)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# Only the Python file — no service/init
SRC_URI = "file://snake_accel.py"
S = "${WORKDIR}"

RDEPENDS:${PN} = "python3 python3-curses"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/snake_accel.py ${D}${bindir}/snake-accel
}

