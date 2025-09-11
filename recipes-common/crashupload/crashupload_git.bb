SUMMARY = "Crashupload application"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"


SRC_URI = "git://github.com/rdkcentral/crashupload.git;protocol=https;nobranch=1;name=crashupload"
# Release version - 1.0.6
SRCREV_crashupload = "e34335b65bef1a975b15e41a7127ae70df2e4145"
PV = "1.0.6"
PR = "r0"
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
