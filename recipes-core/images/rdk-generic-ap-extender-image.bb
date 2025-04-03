SUMMARY = "A console-only image for the RDK-B yocto build for EasyMesh AP"
inherit rdk-image

IMAGE_INSTALL_append = " \
    packagegroup-rdk-oss-broadband \
    packagegroup-ap-extender \
    "
