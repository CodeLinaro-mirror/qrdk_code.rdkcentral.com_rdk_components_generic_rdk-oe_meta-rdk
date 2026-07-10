SUMMARY = "DCM Daemon (Device Configuration Management)"
SECTION = "dcmd"
LICENSE = "Apache-2.0"
# Verified against the workspace dcm-agent/LICENSE. The camera build fetches the
# rdkcteam fork via dcmd_git.bbappend, whose LICENSE matches this md5 (same S
# layout, so file://LICENSE resolves to ${WORKDIR}/git/LICENSE for both). If a
# future rdkcentral SRCREV ships a different LICENSE, re-derive from the fetched
# ${WORKDIR}/git/LICENSE. Do NOT reuse meta-rdk-github's 2441d6... (mismatch).
LIC_FILES_CHKSUM = "file://LICENSE;md5=31507c3c771f85b4cb615b359765f58d"

# Only build when the 'dcmd' distro feature is enabled (RDKC-only). Every build
# that shares meta-rdk without the feature is skipped -> no regression.
inherit distro_features_check
REQUIRED_DISTRO_FEATURES = "dcmd"

# Upstream rdkcentral pin (v2.2.4). The camera bbappend overrides SRC_URI to the
# rdkcteam fork with AUTOREV during sprint development.
SRC_URI = "git://github.com/rdkcentral/dcm-agent.git;protocol=https;nobranch=1;name=dcmagent"
SRCREV_dcmagent = "9fc6aeb511264845b9244df6d7a91a4db825ee51"
SRCREV_FORMAT = "dcmagent"
PV = "2.2.4"
PR = "r0"
S = "${WORKDIR}/git"

DEPENDS = "rbus commonutilities telemetry rdk-logger libsyswrapper curl openssl cjson rfc"

inherit autotools coverity

# autotools has a parallel-make race in this project (carried from upstream).
PARALLEL_MAKE = ""

# Global C flags for the whole recipe.
#  -D_GNU_SOURCE : expose POSIX/GNU declarations (gmtime_r, popen, strcasestr,
#                  kill, sync, ...) that the strict -std=c99 sub-modules
#                  otherwise hide -- an implicit gmtime_r would truncate its
#                  64-bit pointer return on aarch64.
#  -std=c11      : bitbake ${CFLAGS} is appended AFTER each target's own
#                  *_CFLAGS in the automake compile line, so this overrides the
#                  modules' -std=c99.
#  -fPIC         : position-independent code (dcmd already links -shared).
#  -Wall -Werror : treat warnings as errors (active alongside the modules'
#                  -Wextra). Tighten build hygiene.
CFLAGS_append = " -std=c11 -fPIC -D_GNU_SOURCE -Wall -Werror"

EXTRA_OECONF_append = " --enable-t2api=yes"

do_install_append() {
    install -d ${D}${includedir}
    install -m 0644 ${S}/uploadstblogs/include/*.h ${D}${includedir}
}

FILES_${PN} += "${bindir}/dcmd ${bindir}/logupload"
