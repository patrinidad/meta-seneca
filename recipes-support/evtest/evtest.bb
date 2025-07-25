SUMMARY = "Simple tool for input event debugging"
HOMEPAGE = "http://people.freedesktop.org/~whot/evtest/"
AUTHOR = "Vojtech Pavlik<vojtech@suse.cz>"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "libxml2"

SRCREV = "0e14da978ea906c7fdaf90f54f798a542e79ce8f"
SRC_URI = "git://anongit.freedesktop.org/evtest;protocol=git"

PV = "1.31+${SRCPV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
