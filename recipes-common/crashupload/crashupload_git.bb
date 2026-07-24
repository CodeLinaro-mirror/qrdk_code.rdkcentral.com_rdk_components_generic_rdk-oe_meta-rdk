SUMMARY = "Crashupload application"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=377ffe30094aa2a7a3e56ea7aa355abd"


SRC_URI = "git://github.com/rdkcentral/crashupload.git;protocol=https;nobranch=1;name=crashupload"
# Release version - 2.2.0
SRCREV_crashupload = "026165b7f29bf1d214130c87083d19e9aeda64fc"
PV = "2.2.0"
PR = "r1"
S = "${WORKDIR}/git"
SRCREV_FORMAT = "crashupload"

DEPENDS = "glib-2.0 libsyswrapper"

export LINK = "${LD}"

CFLAGS += " \
        -I=${libdir}/glib-2.0/include \
        -I=${includedir}/glib-2.0 "

export GLIBS = "-lglib-2.0 -lz"
export USE_DBUS = "y"

LDFLAGS += "-Wl,-O1"

inherit coverity
inherit systemd

do_install() {
        install -d ${D}${base_libdir}/rdk
        install -m 0755 ${S}/uploadDumps.sh ${D}${base_libdir}/rdk
}

RDEPENDS_${PN} += "busybox"

PACKAGE_BEFORE_PN += "${PN}-conf"

FILES_${PN} += "${base_libdir}/rdk/uploadDumps.sh"
