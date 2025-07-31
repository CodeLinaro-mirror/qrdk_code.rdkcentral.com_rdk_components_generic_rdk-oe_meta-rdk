LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SUMMARY = "mount-utils-generic"

SRC_URI = "${RDK_GENERIC_ROOT_GIT}/mount-utils/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH}"

PV = "${RDK_RELEASE}+git${SRCPV}"
SRCREV= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit pkgconfig autotools coverity

do_install() {
           install -d ${D}/${bindir}
           install -d ${D}${libdir}
           install -d ${D}${includedir}
           install -m 0755 ${S}/RdkConfigApi/include/rdkconfig.h ${D}${includedir}/
           install -m 0644 RdkConfigApi/src/librdkconfig.a ${D}${libdir}
           install -m 0755 RdkConfigApi/src/GetConfigFile ${D}/${bindir}/         
}

FILES_${PN} += "${bindir}/* "
