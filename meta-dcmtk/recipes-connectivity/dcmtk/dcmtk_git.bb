# Recipe for building DICOM Toolkit - dcmtk
#
# Test for compilation to qemuarm64 and qemux86-64
# 
# Based on BSD-3-Clause
#
# Author(s)
#   Claus Stovgaard - clst@ambu.com
#

DESCRIPTION = "DCMTK - DICOM Toolkit"
SUMMARY = "DCMTK - DICOM Toolkit"
LICENSE = "BSD-3-Clause"

# Add dcmtk subfolder to search path
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

# General source setup
BRANCH = "cross-compile-options"
SRC_URI = "git://github.com/frosteyes/dcmtk.git;protocol=https;branch=${BRANCH}"
# not tagged - so just sha1 hash
SRCREV = "319368d6a3463f1e5149d2ca7e989d4535bb6cf4"

LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=7461e0fb30398858fdde489b5dd44e80"

COMPATIBLE_MACHINE = "(qemux86-64|qemuarm64)"

# Below is setup for cross compilation for x86_64
# The setup is chosen together with the arith.h from running cmake on x86_64
SRC_URI += "file://arith_qemux86-64.h \
            file://arith_qemuarm64.h \
            "

# Setup for cross compilation x86_64
EXTRA_OECMAKE_qemux86-64 = "-DDCMTK_NO_TRY_RUN:BOOL=1 \
                            -DC_CHAR_UNSIGNED:BOOL=0 \
                            -DDCMTK_ICONV_FLAGS_ANALYZED:BOOL=TRUE \
                            -DDCMTK_FIXED_ICONV_CONVERSION_FLAGS:STRING=AbortTranscodingOnIllegalSequence \
                            -DDCMTK_STDLIBC_ICONV_HAS_DEFAULT_ENCODING:BOOL=TRUE \
                           "

# Setup for cross compilation arm64
EXTRA_OECMAKE_qemuarm64 += "-DDCMTK_NO_TRY_RUN:BOOL=1 \
                            -DC_CHAR_UNSIGNED:BOOL=1 \
                            -DDCMTK_ICONV_FLAGS_ANALYZED:BOOL=TRUE \
                            -DDCMTK_FIXED_ICONV_CONVERSION_FLAGS:STRING=AbortTranscodingOnIllegalSequence \
                            -DDCMTK_STDLIBC_ICONV_HAS_DEFAULT_ENCODING:BOOL=TRUE \
                           "

# We need to copy arith.h into the correct folder inside the build folder
# Depending on target arch - use the correct one
# For now do it based on machine.
# Later it could be based on TARGET_ARCH or similar if we have a more architecture generic arith.h
cmake_do_configure_append() {
    if [ -e ${WORKDIR}/arith_${MACHINE}.h ] ; then
        install ${WORKDIR}/arith_${MACHINE}.h ${B}/config/include/dcmtk/config/arith.h
    fi
}

# General dependency compilation setup

DEPENDS = "openssl ninja-native zlib virtual/libiconv openssl"
RDEPENDS_${PN} += " glibc-gconvs glibc-utils"

inherit cmake

S = "${WORKDIR}/git"

# Setup build to be a shared libs.
EXTRA_OECMAKE += "-DBUILD_SHARED_LIBS:BOOL=ON \
                  -DUSE_COMPILER_HIDDEN_VISIBILITY:BOOL=ON \
                  "

# Example setup of compile flags
EXTRA_OECMAKE += "-DDCMTK_WITH_TIFF:BOOL=ON \
                  -DDCMTK_WITH_XML:BOOL=ON \
                  -DDCMTK_WITH_ZLIB:BOOL=OFF \
                  -DDCMTK_WITH_OPENSSL:BOOL=ON \
                  -DDCMTK_WITH_ICONV:BOOL=ON \
                  -DDCMTK_ENABLE_PRIVATE_TAGS:BOOL=ON \
                  -DDCMTK_WITH_DOXYGEN:BOOL=OFF \
                  "

# Setup for STL / CXX and cross compilation
EXTRA_OECMAKE += "-DDCMTK_ENABLE_STL:BOOL=ON \
                  -DHAVE_STL_VECTOR_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_ALGORITHM_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_LIMITS_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_LIST_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_MAP_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_MEMORY_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_STACK_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_STRING_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_TYPE_TRAITS_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_TUPLE_TEST_RESULT:BOOL=OFF \
                  -DHAVE_STL_SYSTEM_ERROR_TEST_RESULT:BOOL=OFF \
                  -DDCMTK_ENABLE_CXX11:BOOL=ON \
                 "

