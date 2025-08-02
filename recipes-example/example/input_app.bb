SUMMARY = "Example of creating a yocto recipe which prints the author name when usr button is pressed on beagleplay board"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://input_app.c"

S = "${WORKDIR}"

INSANE_SKIP:${PN} += "ldflags"

do_compile() {
    ${CC} input_app.c -o input_app
}

do_install() {
    install -d ${D}/home/root
    install -m 0755 input_app ${D}/home/root/input_app
}

FILES:${PN} += "/home/root/input_app"
