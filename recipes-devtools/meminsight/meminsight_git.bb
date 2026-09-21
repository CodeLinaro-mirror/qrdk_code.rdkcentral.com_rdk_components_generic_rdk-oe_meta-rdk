SUMMARY = "Memory insight utility and runner service"
SECTION = "console/utils"
DESCRIPTION = "meminsight: system/process memory statistics collection tool with systemd runner service."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f733d19cbbedb307f4259a436c151761"

SRC_URI = "${CMF_GITHUB_ROOT}/${BPN}.git;nobranch=1;protocol=${CMF_GIT_PROTOCOL}"

SRC_URI:append = " file://meminsight-runner.service \
                   file://meminsight-runner.path \
                   file://conf/client.conf \
                   file://conf/broadband.conf \
                   file://conf/client-path.conf \
                   file://conf/broadband-path.conf \
                   file://conf/broadband-rdm-path.conf \
                   file://start_meminsight.sh \
                   file://meminsight-upload.service \
                   file://meminsight-upload.path \
                   file://upload_MemReports.sh \
                   file://package.json \
                   "

SRCREV = "eed318d74f56c46c161a44d9f7a93c33ca8bfea6"
PV = "2.1.1"
S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools systemd

CFLAGS:append:broadband = ' -DDEVICE_INTERFACE_KEY=\\"ARM_INTERFACE\\" -DDEFAULT_OUT_DIR=\\"/nvram/meminsight\\"'

PACKAGECONFIG ??= "cjson http-upload"
PACKAGECONFIG[cjson] = "--enable-cjson,--disable-cjson"
PACKAGECONFIG[http-upload] = "--enable-http-upload,--disable-http-upload"

EXTRA_OECONF += "${@bb.utils.contains('PACKAGECONFIG', 'cjson', '--enable-cjson', '--disable-cjson', d)}"
RDEPENDS:${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'cjson', 'cjson', '', d)}"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/meminsight ${D}${bindir}/meminsight

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/meminsight-runner.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${UNPACKDIR}/meminsight-runner.path ${D}${systemd_unitdir}/system/

    install -m 0644 ${UNPACKDIR}/meminsight-upload.service ${D}${systemd_unitdir}/system/
    install -m 0644 ${UNPACKDIR}/meminsight-upload.path ${D}${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/system/meminsight-runner.service.d
    install -d ${D}${systemd_unitdir}/system/meminsight-runner.path.d
}

do_install:append:client() {
    install -m 0644 ${UNPACKDIR}/conf/client.conf ${D}${systemd_unitdir}/system/meminsight-runner.service.d/
    install -m 0644 ${UNPACKDIR}/conf/client-path.conf ${D}${systemd_unitdir}/system/meminsight-runner.path.d/
}

do_install:append:broadband() {
    install -m 0644 ${UNPACKDIR}/conf/broadband.conf ${D}${systemd_unitdir}/system/meminsight-runner.service.d/
    if ${@bb.utils.contains('DISTRO_FEATURES', 'enable_xmeminsight', 'true', 'false', d)}; then
        install -m 0644 ${UNPACKDIR}/conf/broadband-path.conf ${D}${systemd_unitdir}/system/meminsight-runner.path.d/
    else
        install -m 0644 ${UNPACKDIR}/conf/broadband-rdm-path.conf ${D}${systemd_unitdir}/system/meminsight-runner.path.d/

        install -d ${D}/etc/rdm/post-services
        install -m 0755 ${UNPACKDIR}/start_meminsight.sh ${D}/etc/rdm/post-services/start_meminsight.sh
    fi

    install -d ${D}${libdir}/rdk
    install -m 0755 ${UNPACKDIR}/upload_MemReports.sh ${D}${libdir}/rdk/upload_MemReports.sh
}

SYSTEMD_SERVICE:${PN} = "meminsight-runner.path meminsight-upload.path"

FILES:${PN} += "${bindir}/meminsight"
FILES:${PN} += "${systemd_unitdir}/system/"

FILES:${PN} += "${systemd_unitdir}/system/meminsight-runner.service"
FILES:${PN} += "${systemd_unitdir}/system/meminsight-runner.path"

FILES:${PN} += "${systemd_unitdir}/system/meminsight-runner.service.d/*.conf"
FILES:${PN} += "${systemd_unitdir}/system/meminsight-runner.path.d/*.conf"
FILES:${PN}:append:broadband = " ${libdir}/rdk/upload_MemReports.sh"
