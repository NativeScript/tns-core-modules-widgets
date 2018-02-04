# Development Workflow

<!-- TOC depthFrom:2 -->

- [Running locally](#running-locally)
    - [Prerequisites](#prerequisites)
    - [How to Build](#how-to-build)
    - [How to Build Android](#how-to-build-android)
    - [How to Build iOS](#how-to-build-ios)

<!-- /TOC -->

## Running locally

### Prerequisites

- Install your native toolchain and NativeScript as described in the docs: https://docs.nativescript.org/setup/quick-setup

### How to Build
On Mac in the root folder run:
```
./build.sh
```
This will run Android and iOS build and pack `dist/tns-core-modules-widgets-*.tgz`.

### How to Build Android
In the `android` folder run:
```
gradle build
```
This will output `android/build/widgets-release.aar`.

### How to Build iOS
On Mac in the `ios` folder under mac run:
```
./build.sh
```
This will output `ios/build/TNSWidgets.framework`.
