
EXTRA_OECMAKE += '-DCMAKE_POLICY_VERSION_MINIMUM=3.5'
EXTRA_OECMAKE += "\
    -Dmsgpack_DIR=${RECIPE_SYSROOT}${libdir}/cmake/msgpack-c \
"
EXTRA_OECMAKE += " \
    -DMSGPACK_LIBRARIES=${RECIPE_SYSROOT}${libdir}/libmsgpack-c.so \
    -DMSGPACK_INCLUDE_DIRS=${RECIPE_SYSROOT}${includedir} \
"


do_configure:prepend() {
    sed -i 's/add_dependencies(rbuscore msgpack rtMessage)/add_dependencies(rbuscore rtMessage)/' ${S}/src/core/CMakeLists.txt

    sed -i '/add_dependencies(rtMessage cjson)/d' ${S}/src/rtmessage/CMakeLists.txt

    sed -i '/add_dependencies(rtMessage rdklogger)/d' ${S}/src/rtmessage/CMakeLists.txt

    sed -i 's/add_dependencies(rbuscli rbus linenoise)/add_dependencies(rbuscli rbus)/' ${S}/utils/rbuscli/CMakeLists.txt
}

CFLAGS:append = " -Wno-error=discarded-qualifiers \
                  -Wno-error=unused-parameter \
"
