SUMMARY = "WebKit cache cleanup service after SW upate"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "file://clearWebkitBrowserCache.sh \
           file://webkit-browser-cache-cleanup.service.in"

inherit systemd

# nvram mounts /opt, storage mgr /tmp/data/
WEBKIT_CACHE_CLEAN_SERVICE_ORDER_AFTER ?= "local-fs.target storagemgrmain.service"

do_install() {
    install -d ${D}${base_libdir}/rdk
    install -d ${D}${systemd_unitdir}/system
    install -m 0755 ${UNPACKDIR}/clearWebkitBrowserCache.sh ${D}${base_libdir}/rdk/clearWebkitBrowserCache.sh
    sed -e "s|@WEBKIT_CACHE_CLEAN_SERVICE_ORDER_AFTER@|${WEBKIT_CACHE_CLEAN_SERVICE_ORDER_AFTER}|g" < ${UNPACKDIR}/webkit-browser-cache-cleanup.service.in > ${D}${systemd_unitdir}/system/webkit-browser-cache-cleanup.service
}

SYSTEMD_SERVICE:${PN} = "webkit-browser-cache-cleanup.service"
FILES:${PN} = "${base_libdir}/rdk/clearWebkitBrowserCache.sh ${systemd_unitdir}/system/webkit-browser-cache-cleanup.service"
