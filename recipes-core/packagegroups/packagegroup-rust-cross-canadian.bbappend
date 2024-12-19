PN = "packagegroup-rust-cross-canadian-${MACHINE}"

RDEPENDS:${PN} = " \
    ${@all_multilib_tune_values(d, 'RUST')} \
    ${@all_multilib_tune_values(d, 'CARGO')} \
    rust-cross-canadian-src-${TRANSLATED_TARGET_ARCH} \
    ${@all_multilib_tune_values(d, 'RUST_TOOLS')} \
"
