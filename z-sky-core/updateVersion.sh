VERSION=1.0.0-SNAPSHOT
#----------------------------------------------------

mvn versions:set -DnewVersion=${VERSION}
find ../ -name pom.xml.versionsBackup | xargs rm -rf

echo "\n"
echo "【注意】如无特殊版本需要，请将当前目录pom.xml下的properties[z-sky.version]手动改为${VERSION} ! \n"
