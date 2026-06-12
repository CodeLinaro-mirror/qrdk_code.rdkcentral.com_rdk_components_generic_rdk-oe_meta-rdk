SUMMARY = "Custom package group for RDK bits"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit packagegroup
BLUEZ ?= "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', bb.utils.contains('DISTRO_FEATURES', 'bluez5', 'bluez5', 'bluez4', d), '', d)}"

PACKAGES = "\
    packagegroup-rdk-media-common \
    "

CLOSEDCAPTION ?= "closedcaption"

# Generic RDK components
RDEPENDS:packagegroup-rdk-media-common = "\
    ${CLOSEDCAPTION} \
    devicesettings \
    iarmbus \
    iarmmgrs \
    lsof \
    rdk-logger \
    gst-plugins-rdk \
    rmfgeneric \
    rmfapp \
    virtual/gst-plugins-playersinkbin \
    virtual/mfrlib \
    rmfstreamer \
    iarm-set-powerstate \
    iarm-query-powerstate \
    crashupload \
    crashupload-conf \
    key-simulator \
    tcpdump \
    rdk-diagnostics \
    iptables \
    ${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "${BLUEZ} bluetooth-core bluetooth-mgr virtual/media-utils", "", d)} \
    systemd-usb-support \
    nlmonitor \
    nghttp2-server \
    nghttp2-common \
    stunnel \
    socat \
    rdkmediaplayer \
    ${@bb.utils.contains("DISTRO_FEATURES", "ledmgr", "ledmgr", "" , d)} \
    dca \
    rbus \
    telemetry \
    webconfig-framework \
    webcfg \
    rdm-agent \
    "
RDEPENDS:packagegroup-rdk-media-common:append:qemuall = " sysint "
RDEPENDS:packagegroup-rdk-media-common:append:qemuall = " sysint-conf "
RDEPENDS:packagegroup-rdk-media-common:append_rpi = " rdkmediaplayer "

IMAGE_INSTALL:append_rpi = " e2fsprogs-mke2fs "

#package for firebolt-test-client
RDEPENDS:packagegroup-rdk-media-common += " ${@bb.utils.contains('DISTRO_FEATURES', 'firebolt_test_client', 'firebolt-test-client', '', d)}"

RDEPENDS:packagegroup-rdk-media-common:remove += "\
 ${@bb.utils.contains('DISTRO_FEATURES', 'enable_libsoup3', 'nghttp2-server', '', d)} \
 ${@bb.utils.contains('DISTRO_FEATURES', 'enable_libsoup3', 'nghttp2-common', '', d)} \
"
