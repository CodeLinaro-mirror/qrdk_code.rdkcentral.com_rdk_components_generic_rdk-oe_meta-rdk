SUMMARY = "RFC helper applications"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=bef3b9130aa5d626df3f7171f2dadfe2"

PACKAGECONFIG ??= "rfctool"
PACKAGECONFIG[rfctool] = "--enable-rfctool=yes"
PACKAGECONFIG_append = " tr181set"
PACKAGECONFIG[tr181set] = "--enable-tr181set=yes"

SRC_URI = "git://github.com/rdkcentral/rfc.git;protocol=https;nobranch=1;name=rfc"

SRCREV_rfc = "df9171e723c5aa2bb007484db71e67ffd9d0518a"

# Release version - 1.2.6

PV = "1.2.6+git${SRCPV}"

PR = "r0"

S = "${WORKDIR}/git"
SRCREV_FORMAT = "rfc"

export cjson_CFLAGS = "-I$(PKG_CONFIG_SYSROOT_DIR)${includedir}/cjson"
export cjson_LIBS = "-lcjson"

DEPENDS="cjson curl rdk-logger libsyswrapper commonutilities rdkcertconfig mountutils"
DEPENDS_append_broadband += " rbus"
export rbus_CFLAGS = "-I$(PKG_CONFIG_SYSROOT_DIR)${includedir}/rbus"
export rbus_LIBS = "-lrbus"

EXTRA_OEMAKE += "-e MAKEFLAGS="

EXTRA_OECONF_append_client = " --enable-iarmbus=yes --enable-tr69hostif=yes"
EXTRA_OECONF_append_broadband = " --enable-rdkb=yes --enable-tr181set=yes"
EXTRA_OECONF += " --enable-mountutils=yes --enable-rdkcertselector=yes"

DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS_append_morty = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -Wl,--no-as-needed -lsafec-3.5.1 -Wl,--as-needed', '', d)}"

inherit autotools pkgconfig coverity

CFLAGS += " -Wall -Werror -Wextra "
CFLAGS_append_client += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_maintenance_manager', '-DEN_MAINTENANCE_MANAGER -I${STAGING_INCDIR}/rdk/iarmmgrs-hal ', '', d)}"
CXXFLAGS += " -Wall -Werror"

do_install_append () {
	install -d ${D}${base_libdir}/rdk
        install -d ${D}${sysconfdir}

        install -m 0644 ${S}/rfc.properties ${D}${sysconfdir}
}

RDEPENDS_${PN} += "busybox"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', ' gtest gmock', '', d)}"
PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${PN}-gtest', '', d)}"

FILES_${PN}-gtest = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${bindir}/rfc_gtest.bin', '', d)} \
"
FILES_${PN} += "${bindir}/rfctool"
FILES_${PN} += "${base_libdir}/*"
FILES_${PN} += "${sysconfdir}/*"

DOWNLOAD_APPS="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'gtestapp-rfc', '', d)}"
inherit comcast-package-deploy
CUSTOM_PKG_EXTNS="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'gtest', '', d)}"
SKIP_MAIN_PKG="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"
DOWNLOAD_ON_DEMAND="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"
