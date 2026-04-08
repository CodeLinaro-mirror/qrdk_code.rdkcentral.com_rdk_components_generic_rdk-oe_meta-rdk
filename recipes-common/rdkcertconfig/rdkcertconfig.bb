SUMMARY = "A simple library for rdk certificate selector/locator"
DESCRIPTION = "This library will select and provide certificate to application"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

S = "${UNPACKDIR}/${PN}-${PV}"
DEPENDS = "mountutils "
DEPENDS:append_client = " libsyswrapper "
SRC_URI = "git://github.com/rdkcentral/rdk-cert-config.git;protocol=https;nobranch=1"
CFLAGS:append = " -DCONFIG_ERROR_ENABLED"

SRCREV = "cd445dd473a9a82488d553faaf7e60e57a0e102b"
PV = "1.0.5"
PR = "r1"

EXTRA_OECONF += "--enable-rdklogger --enable-cspcrdkconfig"

inherit autotools pkgconfig coverity

DEBIAN_NOAUTONAME:${PN} = "1"
DEBIAN_NOAUTONAME:${PN}-dev = "1"
DEBIAN_NOAUTONAME:${PN}-dbg = "1"
