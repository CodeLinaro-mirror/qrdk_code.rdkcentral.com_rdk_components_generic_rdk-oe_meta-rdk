COMPATIBLE_HOST .= "|mips.*-linux"
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

S = "${UNPACKDIR}/${PN}-${PV}"

SRC_URI:append = " file://safe_compile_h.patch"
SRC_URI:remove_kirkstone = " file://safe_compile_h.patch"
SRC_URI:remove:wrynose = " file://safe_compile_h.patch"
EXTRA_OECONF:append = " --disable-wchar"

SRCREV_dunfell = "60786283fd61cd621a5d1df00e083a1c1e3cf52a"
SRCREV_morty = "60786283fd61cd621a5d1df00e083a1c1e3cf52a"
