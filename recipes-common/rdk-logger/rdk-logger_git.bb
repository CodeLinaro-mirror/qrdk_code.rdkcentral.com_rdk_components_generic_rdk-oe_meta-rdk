SUMMARY = "This recipe builds the rdk_logger code base, providing logging interfaces required by all mediaplayers"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "git://github.com/rdkcentral/rdk_logger.git;branch=main;protocol=https"
SRCREV = "ed3a3f71b8db836449bde6b7c9dc60438fedf30e"
PV = "2.4.0"
PR = "r0"


S = "${WORKDIR}/git"

DEPENDS = "log4c glib-2.0"
DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"

#Milestone Support
EXTRA_OECONF += " --enable-milestone"
PROVIDES = "getClockUptime"
CFLAGS_append_hybrid += " -DLOGMILESTONE"
CFLAGS_append_client += " -DLOGMILESTONE"

inherit autotools pkgconfig coverity pkgconfig

CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"

CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"

do_install_append () {
    install -d ${D}${base_libdir}/rdk/
    install -m 0755 ${S}/scripts/logMilestone.sh ${D}${base_libdir}/rdk
}

FILES_${PN} += "${base_libdir}/rdk/logMilestone.sh \
                ${base_libdir} \
                ${base_libdir}/rdk"
