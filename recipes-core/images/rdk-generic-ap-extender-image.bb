SUMMARY = "A console-only image for the RDK-B yocto build for EasyMesh AP"
inherit rdk-image

IMAGE_INSTALL:append = " \
    packagegroup-rdk-oss-broadband \
    packagegroup-ap-extender \
    "
