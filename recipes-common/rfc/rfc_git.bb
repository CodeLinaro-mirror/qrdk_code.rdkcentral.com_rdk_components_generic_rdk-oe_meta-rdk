SUMMARY = "RFC helper applications"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ab9fb9cce9e613f50442aa48f62301fc"

PACKAGECONFIG ??= "rfctool"
PACKAGECONFIG[rfctool] = "--enable-rfctool=yes"
PACKAGECONFIG:append = " tr181set"
PACKAGECONFIG[tr181set] = "--enable-tr181set=yes"

SRC_URI = "git://github.com/rdkcentral/rfc.git;protocol=https;nobranch=1;name=rfc"

SRCREV_rfc = "2aab469a9d5cbeefbc44d8c10de5e97a2b8cc866"

# Release version - 1.2.7

PV = "1.2.7+git${SRCPV}"

PR = "r0"

S = "${UNPACKDIR}/${PN}-${PV}"
SRCREV_FORMAT = "rfc"

export cjson_CFLAGS = "-I$(PKG_CONFIG_SYSROOT_DIR)${includedir}/cjson"
export cjson_LIBS = "-lcjson"

DEPENDS="cjson curl rdk-logger libsyswrapper commonutilities rdkcertconfig mountutils"
DEPENDS:append:broadband = " rbus"
export rbus_CFLAGS = "-I$(PKG_CONFIG_SYSROOT_DIR)${includedir}/rbus"
export rbus_LIBS = "-lrbus"

EXTRA_OEMAKE += "-e MAKEFLAGS="

EXTRA_OECONF:append:client = " --enable-iarmbus=yes --enable-tr69hostif=yes"
EXTRA_OECONF:append:broadband = " --enable-rdkb=yes --enable-tr181set=yes"
EXTRA_OECONF += " --enable-mountutils=yes --enable-rdkcertselector=yes"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:append_morty = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -Wl,--no-as-needed -lsafec-3.5.1 -Wl,--as-needed', '', d)}"

inherit autotools pkgconfig coverity

CFLAGS += " -Wall -Werror -Wextra "
CFLAGS:append:client += "${@bb.utils.contains('DISTRO_FEATURES', 'enable_maintenance_manager', '-DEN_MAINTENANCE_MANAGER -I${STAGING_INCDIR}/rdk/iarmmgrs-hal ', '', d)}"
CXXFLAGS += " -Wall -Werror"
CXXFLAGS:append = " -include array"

do_install:append () {
	install -d ${D}${base_libdir}/rdk
        install -d ${D}${sysconfdir}

        install -m 0644 ${S}/rfc.properties ${D}${sysconfdir}
}

RDEPENDS:${PN} += "busybox"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', ' gtest gmock', '', d)}"
PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${PN}-gtest', '', d)}"

FILES:${PN}-gtest = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${bindir}/rfc_gtest.bin', '', d)} \
"
FILES:${PN} += "${bindir}/rfctool"
FILES:${PN} += "${base_libdir}/*"
FILES:${PN} += "${sysconfdir}/*"

DOWNLOAD_APPS="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'gtestapp-rfc', '', d)}"
inherit comcast-package-deploy
CUSTOM_PKG_EXTNS="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'gtest', '', d)}"
SKIP_MAIN_PKG="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"
DOWNLOAD_ON_DEMAND="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"
