SUMMARY = "This recipe compiles Telemetry"
SECTION = "console/utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

#SRC_URI = "${RDK_GENERIC_ROOT_GIT}/telemetry/generic;protocol=${RDK_GIT_PROTOCOL};branch=${RDK_GIT_BRANCH}"
SRC_URI = "git://github.com/rdkcentral/telemetry.git;protocol=git;nobranch=1"

#RDKB-63357
SRC_URI:append = " file://RDKB-63357-ignore-zero-values-for-datamodel-markers.patch"

DEPENDS += "curl cjson glib-2.0 breakpad-wrapper rbus libsyswrapper libunpriv"
DEPENDS += "rdk-logger"

RDEPENDS:${PN} += "curl cjson glib-2.0 rbus"

#Telemetry is federated hence update SRCREV and PV manually for component release
#PV = "${RDK_RELEASE}+git${SRCPV}"
#SRCREV ?= "${AUTOREV}"

#Release date 27th Nov, 2025

PV = "1.8.3"
SRCREV = "d8bcf7f71dcd9c9c5f7e29c4b7162917105feb08"

#For Hub4 we are using older T2 release to avoid libucul 7.62.0 dependency added as part of bbappend
#PV_ = "1.5.1"
#SRCREV = "4873fe59a586cba794b391eac0812d517b0e567b"

S = "${UNPACKDIR}/${PN}-${PV}"

CFLAGS += " -Wall -Werror -Wextra -Wno-unused-parameter -Wno-pointer-sign -Wno-sign-compare -Wno-enum-compare -Wno-type-limits -Wno-enum-conversion -Wno-format-truncation"
CFLAGS += " -DRDK_LOGGER "

# Enable SE HW based cert usage
CFLAGS:append += "${@bb.utils.contains_any('DISTRO_FEATURES', 'ENABLE_HW_CERT_USAGE',' -DENABLE_HW_CERT_USAGE -DENABLE_CUSTOM_ENGINE ',' ',d)}"


inherit pkgconfig autotools systemd ${@bb.utils.contains_any("DISTRO_FEATURES", "kirkstone wrynose", "python3native", "pythonnative", d)} breakpad-logmapper

CFLAGS += " -DDROP_ROOT_PRIV -DENABLE_MTLS"

LDFLAGS:append = " \
        -lbreakpadwrapper \
        -lpthread \
        -lstdc++ \
        -lsecure_wrapper \
        "
LDFLAGS:append = " \
        -lprivilege \
      "

CXXFLAGS += "-DINCLUDE_BREAKPAD"

do_install:append () {
    install -d ${D}/usr/include/
    install -d ${D}/lib/rdk/
    install -d ${D}${systemd_unitdir}/system
    install -m 644 ${S}/include/telemetry_busmessage_sender.h ${D}/usr/include/
    install -m 644 ${S}/include/telemetry2_0.h ${D}/usr/include/
    install -m 0755 ${S}/source/commonlib/t2Shared_api.sh ${D}/lib/rdk
    rm -fr ${D}/usr/lib/libtelemetry_msgsender.la

    if ${@bb.utils.contains_any('DISTRO_FEATURES', 't2_without_webconfig', 'true', 'false', d)}; then
        install -m 0755 ${S}/source/commonlib/download_t2_profile.sh ${D}/lib/rdk
    fi
}

FILES:${PN} = "\
    ${bindir}/telemetry2_0 \
    ${bindir}/t2rbusMethodSimulator \
    ${bindir}/telemetry2_0_client \
    ${systemd_unitdir}/system \
"
FILES:${PN} += "${libdir}/*.so*"
FILES:${PN} += "/lib/rdk/*"

FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so"

PACKAGES =+ "${@bb.utils.contains_any('DISTRO_FEATURES', 'gtestapp', '${PN}-gtest', '', d)}"

FILES:${PN}-gtest = "\
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'gtestapp', '${bindir}/telemetry_gtest.bin ${bindir}/xconfclient_gtest.bin ${bindir}/t2parser_gtest.bin ${bindir}/reportgen_gtest.bin ${bindir}/scheduler_gtest.bin', '', d)} \
"

DOWNLOAD_APPS="${@bb.utils.contains_any('DISTRO_FEATURES', 'gtestapp', 'gtestapp-telemetry', '', d)}"
inherit comcast-package-deploy
CUSTOM_PKG_EXTNS="${@bb.utils.contains_any('DISTRO_FEATURES', 'gtestapp', 'gtest', '', d)}"
SKIP_MAIN_PKG="${@bb.utils.contains_any('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"
DOWNLOAD_ON_DEMAND="${@bb.utils.contains_any('DISTRO_FEATURES', 'gtestapp', 'yes', 'no', d)}"

# Breakpad processname and logfile mapping
BREAKPAD_LOGMAPPER_PROCLIST = "telemetry2_0"
BREAKPAD_LOGMAPPER_LOGLIST = "telemetry2_0.txt.0"
