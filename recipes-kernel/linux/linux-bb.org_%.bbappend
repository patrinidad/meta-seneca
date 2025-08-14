FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://usb-gadget.cfg"
SRC_URI += "file://accel.cfg"

KERNEL_FEATURES:append = " usb-gadget.cfg accel.cfg"

do_configure:append () {
    # Ensure a baseline .config exists
    oe_runmake -C ${S} O=${B} olddefconfig

    if [ -x ${S}/scripts/kconfig/merge_config.sh ]; then
        ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config \
            ${WORKDIR}/usb-gadget.cfg \
            ${WORKDIR}/accel.cfg
    else
        [ -f ${WORKDIR}/usb-gadget.cfg ] && cat ${WORKDIR}/usb-gadget.cfg >> ${B}/.config
        [ -f ${WORKDIR}/accel.cfg ]      && cat ${WORKDIR}/accel.cfg      >> ${B}/.config
    fi

    oe_runmake -C ${S} O=${B} olddefconfig
}
