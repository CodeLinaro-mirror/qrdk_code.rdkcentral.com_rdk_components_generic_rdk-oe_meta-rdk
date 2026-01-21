SUMMARY = "libcap wrapper "
LICENSE = "Apache-2.0"
DEPENDS = "libcap jsoncpp"
S = "${WORKDIR}/git"
SRC_URI = "git://github.com/rdkcentral/rdk-libunpriv;protocol=https;nobranch=1;name=rdk-libunpriv \
"
SRCREV_rdk-libunpriv = "2e6c30c88823b7ff9cebde4d7f4d978118510da0"
SRCREV_FORMAT = "rdk-libunpriv"

CXXFLAGS_append = "\
    -I${STAGING_INCDIR} \
    -I${STAGING_INCDIR}/jsoncpp"

LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

inherit autotools pkgconfig

CFLAGS += " -Wall -Werror -Wextra -Wno-unused-parameter "
