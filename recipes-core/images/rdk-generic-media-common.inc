SUMMARY = "Image with RDK components"

IMAGE_FEATURES += "package-management"

inherit rdk-image

IMAGE_ROOTFS_SIZE = "8192"

IMAGE_INSTALL += " \
    packagegroup-rdk-baserootfs \
    packagegroup-rdk-qt5 \
    packagegroup-rdk-media-common \
    "

IMAGE_INSTALL += " \
    ${@bb.utils.contains("DISTRO_FEATURES", "gstreamer1", "gstreamer1.0-plugins-base-meta", "gst-plugins-base-meta", d)} \
    ${@bb.utils.contains("DISTRO_FEATURES", "gstreamer1", "gstreamer1.0-plugins-good-meta", "gst-plugins-good-meta", d)} \
    "

IMAGE_INSTALL_remove_rpi ="gstreamer1.0-plugins-good-meta"
IMAGE_INSTALL += " ${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer1', 'gstreamer1.0-plugins-bad-ivfparse', 'gst-plugins-bad-ivfparse', d)} "
IMAGE_INSTALL += " ${@bb.utils.contains('DISTRO_FEATURES', 'bluez5', 'gstreamer1.0-plugins-good-rtp gstreamer1.0-plugins-bad-bluez gstreamer1.0-plugins-bad-sbc', 'gst-plugins-good-rtp gst-plugins-bad-bluez gst-plugins-bad-sbc', d)} "
