SUMMARY = "secure wrapper for system calls"
LICENSE = "Apache-2.0"

PV = "${RDK_RELEASE}+git${SRCPV}"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

S = "${WORKDIR}/git"
DEPENDS += "rdk-logger"
SRC_URI = "git://github.com/rdkcentral/libSyscallWrapper;protocol=https;nobranch=1;name=libsyswrapper"
DEPENDS = "rdk-logger"

CFLAGS:append = " -Wall -Werror"
CXXFLAGS:append = " -Wall -Werror"

SRCREV_libsyswrapper = "69fab9ab4403b7cda68af1d6a095b2cf5749c84f"
#SRCREV_libsyswrapper = "${@bb.utils.contains('DISTRO_FEATURES', 'oldsyswrapper', 'de0d67d81488c68baf2ab3f91f8a054e153d3b96', '${AUTOREV}', d)}"
SRCREV_FORMAT = "libsyswrapper"

#EXTRA_OECONF += "--enable-testapp"

DEBIAN_NOAUTONAME:${PN} = "1"

inherit autotools pkgconfig coverity

