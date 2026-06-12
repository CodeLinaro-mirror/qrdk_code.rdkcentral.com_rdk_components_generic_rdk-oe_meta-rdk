SUMMARY = "C wrapper for breakpad"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/breakpad_wrapper.git;protocol=git;nobranch=1"
# Release version - 1.0.0
# 2nd May 2025


DEPENDS_broadband += "breakpad"
DEPENDS_client += "breakpad"
DEPENDS_hybrid += "breakpad"

SRCREV = "be8cd679e095cd300f77913863724fa5e39a6182"
PV = "1.0.0"

S = "${WORKDIR}/git"

inherit autotools coverity

CPPFLAGS:append = " \
    -I${STAGING_INCDIR}/breakpad/ \
    "

LDFLAGS_broadband += "-lbreakpad_client -lpthread"
LDFLAGS_client += "-lbreakpad_client -lpthread"
LDFLAGS_hybrid += "-lbreakpad_client -lpthread"

do_install:append () {
    # Config files and scripts
    install -d ${D}${includedir}/
    install -D -m 0644 ${S}/*.h ${D}${includedir}/
}


FILES:${PN} += "${libdir}/*.so"
