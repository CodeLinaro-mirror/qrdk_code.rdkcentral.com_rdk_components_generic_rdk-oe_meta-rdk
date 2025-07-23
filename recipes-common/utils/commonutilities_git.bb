#
# ============================================================================
# COMCAST C O N F I D E N T I A L AND PROPRIETARY
# ============================================================================
# This file and its contents are the intellectual property of Comcast.  It may
# not be used, copied, distributed or otherwise  disclosed in whole or in part
# without the express written permission of Comcast.
# ============================================================================
# Copyright (c) 2019 Comcast. All rights reserved.
# ============================================================================
#
SUMMARY = "RDK commonutilities"
LICENSE = "CLOSED"

SRC_URI = "git://github.com/rdkcentral/common_utilities.git;protocol=git"

# Release version - 1.4.3
# 19 Sept 2025
SRCREV = "071361f284ba9049bf7d8cb9a75b583b9b1e353b"
PV = "1.4.3"

DEPENDS +=" cjson curl rdk-logger"

#uncomment the follwoing line to turn on debugging
#CFLAGS_append = " -DCURL_DEBUG"

CFLAGS_append = " -DRDK_LOGGER"

S = "${WORKDIR}/git"

inherit autotools pkgconfig coverity

