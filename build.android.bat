ECHO "Clean dist"
RMDIR /Q /S dist
MD dist
MD dist\package
MD dist\package\platforms

ECHO "Build android"
MD dist\package\platforms\android
CD android
CALL .\gradlew --quiet assembleRelease
CD ..
CP android\widgets\build\outputs\aar\widgets-release.aar dist\package\platforms\android\widgets-release.aar

ECHO "Copy NPM artefacts"
CP LICENSE dist\package\LICENSE
CP README.md dist\package\README.md
CP package.json dist\package\package.json

ECHO "NPM pack"
CD dist\package
CALL npm pack
CD ..\..
COPY dist\package\*.tgz dist
