SUMMARY = "Upnp based discovery services to discover the gateways, media content providers in the home network."
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

SRC_URI = "${CMF_GITHUB_ROOT}/secure-upnp;protocol=https;branch=main;name=default"
SRCREV = "c1365bea3a4c0da2fd045cb58d08bed107d7f016"
PV = "1.0.0"
PR = "r0"
SRCPV = "${PV}+${SRCREV}"

S = "${UNPACKDIR}/${PN}-${PV}"

DEPENDS = "glib-2.0 gupnp fcgi dbus gnutls libgcrypt"
FILES:${PN} += "${libdir}/"

CFLAGS += " -Wall -Werror -Wextra -Wno-pointer-sign -Wno-sign-compare -Wno-deprecated-declarations -Wno-type-limits -Wno-unused-parameter -Wno-lto-type-mismatch"

DEPENDS = "glib-2.0 gupnp fcgi dbus gnutls rdk-logger libgcrypt libgpg-error "
RDEPENDS:${PN} += "gnutls"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'telemetry2_0', 'telemetry', '', d)}"

DEPENDS:append = " libgcrypt libgpg-error"
DEPENDS:remove_morty = " libgcrypt libgpg-error"

PACKAGECONFIG = "gupnp1.2 dbus"
PACKAGECONFIG[gupnp1.2] = "--enable-version1.2,,,"
PACKAGECONFIG[gupnp0.2] = "--enable-version0.2,,,"
PACKAGECONFIG[gupnp0.2-dfl] = "--enable-version0.2-dfl,,,"
PACKAGECONFIG[gupnp-legacy] = "--enable-version0.9,,,"
PACKAGECONFIG[dbus] = "--enable-dbus,,,"
PACKAGECONFIG[client] = "--enable-client-xcal-server,,,"
PACKAGECONFIG[media-renderer] = "--enable-media-renderer,--disable-media-renderer,rbus,rbus"

PACKAGECONFIG:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'dlnasupport', ' media-renderer', '', d)}"
PROVIDES += "${@bb.utils.contains('DISTRO_FEATURES', 'dlnasupport', '${PN}-rpc', '', d)}"
FILES:${PN}-rpc = "${@bb.utils.contains('DISTRO_FEATURES', 'dlnasupport', '${libdir}/libmediabrowser.so.*', '', d)}"
PACKAGE_BEFORE_PN += "${@bb.utils.contains('DISTRO_FEATURES', 'dlnasupport', '${PN}-rpc', '', d)}"

PACKAGECONFIG:append_client = " client"

PACKAGECONFIG:append_morty = " gupnp0.2"
PACKAGECONFIG:remove = "gupnp1.2"
PACKAGECONFIG:append = " ${@bb.utils.contains_any('DISTRO_FEATURES','dunfell kirkstone wrynose',' gupnp0.2-dfl','',d)} "

EXTRA_OECONF += " --sysconfdir=${sysconfdir}/xupnp"

inherit autotools systemd pkgconfig coverity
YOCTO_VER = "${@ bb.utils.contains('DISTRO_FEATURES', 'dunfell kirkstone wrynose', 1, 0, d) }"
SAFEC_VER =  "${@ "safec-3.5.1" if ${YOCTO_VER} else "safec-3.5" }"

CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --cflags libsafec`', ' -fPIC', d)}"
CXXFLAGS:append = " -fPIC "
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
CFLAGS:append = " -I${STAGING_INCDIR}/ccsp "
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'telemetry2_0', '-DENABLE_FEATURE_TELEMETRY2_0', '', d)} "
LDFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'telemetry2_0', ' -ltelemetry_msgsender ', '', d)} "
CFLAGS:append = " -DLOGMILESTONE"
LDFLAG:append = " -lrdkloggers"

PACKAGES =+ "${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${PN}-gtest', '', d)}"
FILES:${PN}-gtest = "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', '${bindir}/xupnp_gtest.bin', '', d)} \
"

DOWNLOAD_APPS="${@bb.utils.contains('DISTRO_FEATURES', 'gtestapp', 'gtestapp-xupnp', '', d)}"
inherit comcast-package-deploy
CUSTOM_PKG_EXTNS="gtest"
SKIP_MAIN_PKG="yes"
DOWNLOAD_ON_DEMAND="yes"
