SUMMARY = "Lab Test for LNX500"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/LNX500-summer-2025/simple-library_paul_trinidad.git;branch=LAB_TEST;protocol=https"
SRCREV = "a3523c6386643357a3d646ede3eff6dd6e9089be"
SRC_URI[sha256sum] = "71a54f4b0bbe359ba3cd79887146e9c95227929cdac6685338481a5e27506f52"

S = "${WORKDIR}/git"

INSANE_SKIP:${PN} += "ldflags"

do_compile() {
	make all
}

do_install() {
 	install -d ${D}${bindir}
	install -m 0755 simple-library ${D}${bindir}/
}

FILES:${PN} += "${bindir}/simple-library"
