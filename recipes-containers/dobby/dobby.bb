SUMMARY = "Dobby Container Manager"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c466d4ab8a68655eb1edf0bf8c1a8fb8"

include dobby.inc

SRC_URI:append_kirkstone = " file://Fix_compile_gcc11.patch  \
                             file://Add_config_header_kirkstone.patch \
                           "

#Add the patch DELIA-66405
SRC_URI:append = " file://0001-DELIA-66405-Debug-Log-File-stats-for-minidump.patch "

DEPENDS = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' systemd ', '', d)} libnl dbus jsoncpp boost yajl python3 breakpad breakpad-wrapper "
RDEPENDS:${PN} = "crun (>= 0.14.1) ${@bb.utils.contains('DISTRO_FEATURES', 'dac', '', ' dobby-thunderplugin', d)} "

python do_patch_new () {
    bb.build.exec_func('patch_do_patch', d)
}

addtask do_patch_new after do_configure before do_compile

S = "${UNPACKDIR}/${PN}-${PV}"

inherit pkgconfig cmake systemd logrotate
#dobby logs storage file is decided using device.properties. syslog-ng-config-gen framework decide the log file.

#config.h file generation for kirkstone builds
DEPENDS:append:wrynose = " autoconf-native automake-native libtool-native "

CFLAGS:append:wrynose = " --sysroot=${RECIPE_SYSROOT} "
DEPENDS:append_kirkstone = " autoconf-native automake-native "
CFLAGS:append_kirkstone = " --sysroot=${RECIPE_SYSROOT}"
LOGROTATE_NAME="dobby"
LOGROTATE_LOGNAME_dobby="dobby.log"
LOGROTATE_SIZE_dobby="1572864"
LOGROTATE_ROTATION_dobby="3"
LOGROTATE_SIZE_MEM_dobby="1572864"
LOGROTATE_ROTATION_MEM_dobby="3"

# Always build debug version for now
EXTRA_OECMAKE =  " -DCMAKE_BUILD_TYPE=Debug -DBUILD_REFERENCE=${SRCREV}"

# Enable plugins
# Logging, networking, ipc, storage, minidump, oomcrash enabled by default for all builds
PACKAGECONFIG ?= "logging networking ipc storage minidump oomcrash"

# Options for plugins
# -------------------------------------
# RDK Plugins
PACKAGECONFIG[logging]      = "-DPLUGIN_LOGGING=ON,-DPLUGIN_LOGGING=OFF,"
PACKAGECONFIG[networking]   = "-DPLUGIN_NETWORKING=ON,-DPLUGIN_NETWORKING=OFF,"
PACKAGECONFIG[ipc]          = "-DPLUGIN_IPC=ON,-DPLUGIN_IPC=OFF,"
PACKAGECONFIG[storage]      = "-DPLUGIN_STORAGE=ON,-DPLUGIN_STORAGE=OFF,"
PACKAGECONFIG[minidump]     = "-DPLUGIN_MINIDUMP=ON,-DPLUGIN_MINIDUMP=OFF,"
PACKAGECONFIG[oomcrash]     = "-DPLUGIN_OOMCRASH=ON,-DPLUGIN_OOMCRASH=OFF,"
PACKAGECONFIG[testplugin]   = "-DPLUGIN_TESTPLUGIN=ON,-DPLUGIN_TESTPLUGIN=OFF,"
PACKAGECONFIG[gpu]          = "-DPLUGIN_GPU=ON,-DPLUGIN_GPU=OFF,"
PACKAGECONFIG[localtime]    = "-DPLUGIN_LOCALTIME=ON,-DPLUGIN_LOCALTIME=OFF,"
PACKAGECONFIG[rtscheduling] = "-DPLUGIN_RTSCHEDULING=ON,-DPLUGIN_RTSCHEDULING=OFF,"
PACKAGECONFIG[httpproxy]    = "-DPLUGIN_HTTPPROXY=ON,-DPLUGIN_HTTPPROXY=OFF,"
PACKAGECONFIG[appservices]  = "-DPLUGIN_APPSERVICES=ON,-DPLUGIN_APPSERVICES=OFF,"
PACKAGECONFIG[thunder]      = "-DPLUGIN_THUNDER=ON,-DPLUGIN_THUNDER=OFF,"
PACKAGECONFIG[ionmemory]    = "-DPLUGIN_IONMEMORY=ON,-DPLUGIN_IONMEMORY=OFF,"
PACKAGECONFIG[devicemapper] = "-DPLUGIN_DEVICEMAPPER=ON,-DPLUGIN_DEVICEMAPPER=OFF,"
PACKAGECONFIG[gamepad]      = "-DPLUGIN_GAMEPAD=ON,-DPLUGIN_GAMEPAD=OFF,"

# Legacy components (Dobby specs + legacy Sky plugins)
PACKAGECONFIG[legacycomponents] = "-DLEGACY_COMPONENTS=ON,-DLEGACY_COMPONENTS=OFF,ctemplate,"

# -------------------------------------

# Enable Hibernate Memcr implementation
EXTRA_OECMAKE += "${@bb.utils.contains('DISTRO_FEATURES', 'RDKTV_APP_HIBERNATE', ' -DDOBBY_HIBERNATE_MEMCR_IMPL=ON -DDOBBY_HIBERNATE_MEMCR_PARAMS_ENABLED=ON','',d)}"

# Add the systemd service
SYSTEMD_SERVICE:${PN} = "dobby.service"

# Skip harmless QA issue caused by installing but not shipping buildtime cmake files
INSANE_SKIP:${PN} = "installed-vs-shipped"

# Ensure that the unversioned symlinks of libraries are kept (and don't generate a QA error)
INSANE_SKIP:${PN} += "dev-so"
INSANE_SKIP:${PN}-dev:wrynose += "buildpaths"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

FILES:${PN} += "${systemd_system_unitdir}/dobby.service"

do_install:append:wrynose() {
    if [ -f ${D}/lib/systemd/system/dobby.service ] && \
       [ "${D}/lib/systemd/system" != "${D}${systemd_system_unitdir}" ]; then
        install -d ${D}${systemd_system_unitdir}
        mv ${D}/lib/systemd/system/dobby.service ${D}${systemd_system_unitdir}/dobby.service
        rmdir -p --ignore-fail-on-non-empty ${D}/lib/systemd/system 2>/dev/null || true
    fi
}
FILES:${PN} += "${sysconfdir}/systemd/system/multi-user.target.wants/dobby.service"
FILES:${PN} += "${sysconfdir}/dobby.json"
FILES:${PN} += "${bindir}/DobbyTool"
FILES:${PN} += "${sbindir}/DobbyDaemon"
FILES:${PN} += "${libexecdir}/DobbyInit"
FILES:${PN} += "${libdir}/plugins/dobby/*.so*"
FILES:${PN} += "${libdir}/libethanlog.so*"
FILES:${PN} += "${libdir}/libocispec.so*"
