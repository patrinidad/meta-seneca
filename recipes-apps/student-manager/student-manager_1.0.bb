DESCRIPTION = "program that uses simple-library"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "simple-library nlohmann-json"

inherit cmake

SRC_URI = "git://github.com/patrinidad/student-manager.git;branch=master;protocol=https"
SRCREV = "${AUTOREV}"


S = "${WORKDIR}/git"


do_install() {
    install -d ${D}${bindir}
    install -m 0755 student-manager ${D}${bindir}/
}

FILES:${PN} += "${bindir}/student-manager"

