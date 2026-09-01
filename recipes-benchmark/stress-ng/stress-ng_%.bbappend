FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit systemd  
SRC_URI += "file://stress-ng-tests.sh \
            file://capture-proc-metrics.sh \
            file://rdk_oss_uploadSTBLogs.sh \
            file://stress-test.service \
            file://openssl-ptest-stress.sh \
            file://openssl-ptest-perf_stats.sh \
            file://openssl-stress.service \
            file://vmstat.patch \
            file://child_detail.patch \
            file://fwversion_mac.patch \
            file://0001-RDK-36342-Include-CPU-idle-time-to-perf-metrics.patch \
	    file://stress-ng.conf \
            "
            
#SYSTEMD_SERVICE:${PN} = "stress-ng-test.path stress-ng-test.service"

do_install:append() {
    install -d ${D}/lib/rdk
    install -d ${D}${systemd_unitdir}/system
    install -d  ${D}${sysconfdir}
    install -m 0755 ${UNPACKDIR}/capture-proc-metrics.sh ${D}/lib/rdk
    install -m 0755 ${UNPACKDIR}/stress-ng-tests.sh ${D}/lib/rdk
    install -m 0755 ${UNPACKDIR}/openssl-ptest-stress.sh ${D}/lib/rdk
    install -m 0755 ${UNPACKDIR}/openssl-ptest-perf_stats.sh ${D}/lib/rdk
    install -m 0755 ${UNPACKDIR}/rdk_oss_uploadSTBLogs.sh ${D}/lib/rdk
    install -m 0644 ${UNPACKDIR}/stress-test.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/openssl-stress.service ${D}${systemd_unitdir}/system
    install -m 755 ${UNPACKDIR}/stress-ng.conf ${D}${sysconfdir}
}

FILES:${PN} += " /lib/rdk/capture-proc-metrics.sh " 
FILES:${PN} += " /lib/rdk/stress-ng-tests.sh " 
FILES:${PN} += " /lib/rdk/rdk_oss_uploadSTBLogs.sh"
FILES:${PN} += " /lib/rdk/openssl-ptest-stress.sh " 
FILES:${PN} += " /lib/rdk/openssl-ptest-perf_stats.sh " 
FILES:${PN} += "${systemd_unitdir}/system/stress-test.service"
FILES:${PN} += "${systemd_unitdir}/system/openssl-stress.service"
FILES:${PN} += "${sysconfdir}/stress-ng.conf"
