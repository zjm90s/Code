VERSION=1.0.0-SNAPSHOT
#----------------------------------------------------

mvn versions:set -DnewVersion=${VERSION}
find ../ -name pom.xml.versionsBackup | xargs rm -rf
