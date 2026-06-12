SUMMARY = "Crashupload application"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"


SRC_URI = "git://github.com/rdkcentral/crashupload.git;protocol=https;nobranch=1;name=crashupload"
# Release version - 1.0.7
SRCREV_crashupload = "c17ff99e14e5685d37df83cac3bba3cdb478e13b"
PV = "1.0.7"
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

RDEPENDS:${PN} += "busybox"

PACKAGE_BEFORE_PN += "${PN}-conf"

FILES:${PN} += "${base_libdir}/rdk/uploadDumps.sh"
