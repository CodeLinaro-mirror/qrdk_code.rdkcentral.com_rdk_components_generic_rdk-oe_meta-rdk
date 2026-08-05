#
# RDM Agent
#

DESCRIPTION = "rdm-agent"
SECTION = "rdm-agent"
DEPENDS += "rbus"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8700a1d105cac2a90d4f51290ac6e466"

# This tells bitbake where to find the files we're providing on the local filesystem
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = "git://github.com/rdkcentral/rdm-agent;protocol=git;nobranch=1;name=rdmagent"

SRCREV_FORMAT = "rdmagent"
# Tag 2.2.0 / MAR 18 2026
SRCREV_rdmagent = "d5e7372226ec640a4d8fcebba46b6d39ce4d0ff4"

# Make sure our source directory (for the build) matches the directory structure in the tarball
S = "${UNPACKDIR}/${PN}-${PV}"

inherit autotools coverity systemd syslog-ng-config-gen
SYSLOG-NG_FILTER = "apps-rdm"
SYSLOG-NG_SERVICE_apps-rdm = "apps-rdm.service"
SYSLOG-NG_DESTINATION_apps-rdm = "rdm_status.log"
SYSLOG-NG_LOGRATE_apps-rdm = "high"

LOGROTATE_NAME="rdm_status"
LOGROTATE_LOGNAME_rdm_status="rdm_status.log"
LOGROTATE_SIZE_rdm_status="1572864"
LOGROTATE_ROTATION_rdm_status="3"
LOGROTATE_SIZE_MEM_rdm_status="1572864"
LOGROTATE_ROTATION_MEM_rdm_status="3"

PARALLEL_MAKE = ""

DEPENDS += "commonutilities rfc rdkcertconfig mountutils openssl"
RDEPENDS:${PN}:append = " rfc"

CFLAGS:append = " -std=c11 -fPIC -D_GNU_SOURCE -Wall"

LDFLAGS:append = " -lsecure_wrapper"

DEPENDS += "libsyswrapper"

EXTRA_OECONF:append = " --enable-mountutils=yes --enable-rdkcertselector=yes"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' safec', " ", d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:remove:wrynose = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec',  ' `pkg-config --cflags libsafec`', '-fPIC', d)}"
CFLAGS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '', ' -DSAFEC_DUMMY_API', d)}"
LDFLAGS:append_kirkstone = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' `pkg-config --libs libsafec`', '', d)}"
LDFLAGS:append_dunfell = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', '-lsafec-3.5.1', '', d)}"
LDFLAGS:append_morty = " ${@bb.utils.contains('DISTRO_FEATURES', 'safec', ' -Wl,--no-as-needed -lsafec-3.5.1 -Wl,--as-needed', '', d)}"

INCLUDE_DIRS = " \
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/openssl \
    "
LDFLAGS += "-ldl -lcrypto -lssl -lcurl -lz"
LDFLAGS:append_kirkstone += " -lsafec"

oe_runconf:prepend () {
       sed -i -e 's/\-v \-V/\-v/g' ${S}/configure
       sed -i -e 's/\-qversion//g' ${S}/configure
}

do_install:append() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/rdm/
    install -D -m644 ${S}/apps_rdm.path ${D}${systemd_unitdir}/system/apps_rdm.path
    install -D -m644 ${S}/apps-rdm.service ${D}${systemd_unitdir}/system/apps-rdm.service
    install -D -m755 ${S}/scripts/getRdmDwldPath.sh ${D}${sysconfdir}/rdm/getRdmDwldPath.sh
    install -D -m755 ${S}/scripts/downloadUtils.sh ${D}${sysconfdir}/rdm/downloadUtils.sh
    install -D -m755 ${S}/scripts/loggerUtils.sh ${D}${sysconfdir}/rdm/loggerUtils.sh
    install -D -m600 ${S}/rdm-manifest.json ${D}${sysconfdir}/rdm/rdm-manifest.json
    install -d ${D}${libdir}
    install -m 0644 ${B}/librdmopenssl.la ${D}${libdir}/
    install -d ${D}${includedir}/rdm
    install -m 0644 ${S}/src/rdm-cpc/rdm/rdm_rsa_signature_verify.h ${D}${includedir}/rdm/
}

SYSTEMD_SERVICE:${PN} = "apps-rdm.service"
SYSTEMD_SERVICE:${PN}:append = " apps_rdm.path"
FILES:${PN}:append = " ${systemd_unitdir}/system/apps-rdm.service \
                       ${systemd_unitdir}/system/apps_rdm.path \
                       ${sysconfdir}/rdm/* \
                       ${libdir}/librdmopenssl.la"

FILES:${PN}-dev += "${includedir}/rdm/rdm_rsa_signature_verify.h"
BBCLASSEXTEND = "native nativesdk"
