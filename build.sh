#!/bin/sh

echo "Set exit on simple errors"
set -e

echo "Use dumb gradle terminal"
export TERM=dumb

echo "Clean dist"
rm -rf dist
mkdir dist
mkdir dist/package
mkdir dist/package/platforms

echo "Build android"
mkdir dist/package/platforms/android
cd android
./gradlew --quiet assembleRelease
cd ..
cp android/widgets/build/outputs/aar/widgets-release.aar dist/package/platforms/android/widgets-release.aar

echo "Build iOS"
mkdir dist/package/platforms/ios
cd ios
./build.sh
cd ..
cp -r ios/TNSWidgets/build/TNSWidgets.framework dist/package/platforms/ios/TNSWidgets.framework

echo "Copy NPM artefacts"
cp LICENSE dist/package/LICENSE
cp README.md dist/package/README.md
cp package.json dist/package/package.json
if [ "$1" ]
then
  echo "Suffix package.json's version with tag: $1"
  sed -i.bak 's/\(\"version\"\:[[:space:]]*\"[^\"]*\)\"/\1-'$1'"/g' ./dist/package/package.json
fi

echo "NPM pack"
cd dist/package
PACKAGE="$(npm pack)"
cd ../..
mv dist/package/$PACKAGE dist/$PACKAGE
echo "Output: dist/$PACKAGE"

