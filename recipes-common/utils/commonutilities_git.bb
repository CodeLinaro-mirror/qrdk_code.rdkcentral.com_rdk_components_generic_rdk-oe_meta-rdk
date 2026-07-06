SUMMARY = "RDK commonutilities"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=24691c8ce48996ecd1102d29eab1216e"

SRC_URI = "git://github.com/rdkcentral/common_utilities.git;protocol=git"

# Release version - 1.4.3
# 19 Sept 2025
SRCREV = "071361f284ba9049bf7d8cb9a75b583b9b1e353b"
PV = "1.4.3"

DEPENDS +=" cjson curl rdk-logger"

#uncomment the follwoing line to turn on debugging
#CFLAGS:append = " -DCURL_DEBUG"

CFLAGS:append = " -DRDK_LOGGER"

S = "${UNPACKDIR}/${PN}-${PV}"

inherit autotools pkgconfig coverity

