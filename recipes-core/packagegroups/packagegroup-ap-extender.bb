SUMMARY = "Custom package group for EasyMesh AP"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-ap-extender \
    "
#components used in EasyMesh AP extender
RDEPENDS_packagegroup-ap-extender = "\
"
