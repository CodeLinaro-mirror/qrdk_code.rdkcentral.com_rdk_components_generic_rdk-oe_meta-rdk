SUMMARY = "libcap wrapper "
LICENSE = "Apache-2.0"
DEPENDS = "libcap jsoncpp"
S = "${WORKDIR}/git"
SRC_URI = "git://github.com/rdkcentral/rdk-libunpriv;protocol=https;nobranch=1;name=rdk-libunpriv \
"
SRCREV_rdk-libunpriv = "a0dbea7d368a630158abf56ef8bd7f3855910ec5"
SRCREV_FORMAT = "rdk-libunpriv"

CXXFLAGS_append = "\
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/jsoncpp"

LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

inherit autotools pkgconfig

CFLAGS += " -Wall -Werror -Wextra -Wno-unused-parameter "
