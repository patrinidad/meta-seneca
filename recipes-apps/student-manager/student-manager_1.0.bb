DESCRIPTION = "program that uses simple-library"
HOMEPAGE = "https://github.com/patrinidad/student-manager"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/patrinidad/student-manager.git;branch=main;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS = "nlohmann-json simple-library"

inherit cmake

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=Release"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/src/student-manager ${D}${bindir}/student-manager
}

