Sample Yocto Project for building dcmtk into embedded linux (ARM64 and x86-64)
=============================================
This repository provides an example for cross compilation for DICOM toolkit
dcmtk and install it in core-image-sato for a virtual x86-64 and ARM64 qemu
machine

It is designed around poky using the dunfell LTS branch.

Before trying to use this, please see yocto project quick start for setting
up a native Linux machine as build host. I expect that all build tools is 
installed.

Getting Started
---------------
**1.  Clone this repo.**

    $ git clone --recurse-submodules https://github.com/frosteyes/yocto-dcmtk.git

**2.  Setup yocto.**

Source the setup script. Optional to select a build folder. If non is selected
a standard named *build* is created:

    $ cd yocto-dcmtk
    $ source setup_qemuarm64 build_arm64

After you have sourced the setup, please look into the *conf/local.conf* file.
Specifically the variables **DL_DIR** and **SSTATE_DIR** is relevant to save
downloads for later, and prebuild in sstate cache.

**3.  Build image.**

Run bitbake - here we just selet the basic image:

    $ bitbake core-image-sato

First time it takes significant time, as it need to download all the source 
code and compile the host tools first, and next the complete linux system.

**4.  Run the image in QEMU.**

When the build is done, it is just to run qemu:

    $ runqemu

