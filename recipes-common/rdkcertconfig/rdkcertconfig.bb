SUMMARY = "A simple library for rdk certificate selector/locator"
DESCRIPTION = "This library will select and provide certificate to application"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

S = "${WORKDIR}/git"
DEPENDS = "mountutils "
DEPENDS_append_client = " libsyswrapper "
SRC_URI = "git://github.com/rdkcentral/rdk-cert-config.git;protocol=https;nobranch=1"
CFLAGS_append = " -DCONFIG_ERROR_ENABLED"

SRCREV = "2d18ef9fe8068f040cbe5639023510a26e6e48bf"

EXTRA_OECONF += "--enable-rdklogger --enable-cspcrdkconfig"

inherit autotools pkgconfig coverity

DEBIAN_NOAUTONAME_${PN} = "1"
DEBIAN_NOAUTONAME_${PN}-dev = "1"
DEBIAN_NOAUTONAME_${PN}-dbg = "1"
