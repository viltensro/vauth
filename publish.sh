#!/bin/bash
#
# description: publish from src
#

##########
# Vilten, 2017
# Viliam Tencer
#########

echo -n "Vauth is publishing..."
rm -rf bin
rm -rf strings
rm -rf config
rm -rf sql
rm -rf lib
rm -rf mails
rm -rf doc
mkdir -p bin
mkdir -p strings
mkdir -p config
mkdir -p sql
mkdir -p lib
mkdir -p mails
mkdir -p doc
cp -f src/vauth/vauth-web/src/main/resources/config/strings/* strings/
cp -f src/vauth/vauth-web/src/main/resources/bin/* bin/
chmod +x bin/vauth*
cp -f src/vauth/vauth-web/src/main/resources/config/mails/* mails/
cp -f src/vauth/vauth-web/src/main/resources/config/vauth.example.conf config/
cp -f src/vauth/vauth-web/src/main/resources/sql/*.sql sql/
cp -f src/vauth/vauth-web/target/vauth-web-1.1.2.war lib/vauth
cp -fr src/vauth/vauth-boot/target/libs lib/
cp -f src/vauth/vauth-boot/target/vauth-boot-1.1.2.jar lib/vauth-boot
rm -rf src/vauth/vauth-web/src/main/webapp/doc/*
apidoc -i src/vauth/vauth-web/src/main/java/sk/vilten/vauth/web/ -o src/vauth/vauth-web/src/main/webapp/doc
cp -fr src/vauth/vauth-web/src/main/webapp/doc/* doc/

mkdir -p $1/bin
mkdir -p $1/config
mkdir -p $1/lib
mkdir -p $1/log
mkdir -p $1/sql
mkdir -p $1/strings
mkdir -p $1/mails
mkdir -p $1/doc

cp -fpr bin/* $1/bin/
sed "s/branch/$2/g" bin/vauth > $1/bin/vauth
sed "s/branch/$2/g" bin/vauth-mac > $1/bin/vauth-mac
chmod +x $1/bin/*
cp -fpr config/* $1/config/
cp -fpr lib/* $1/lib/
sed "s/vauth-db/$2-vauth-db/g" sql/vauth-db.example.sql > $1/sql/$2-vauth-db.example.sql.tmp
sed "s/vauth-user/$2-vauth-user/g" $1/sql/$2-vauth-db.example.sql.tmp > $1/sql/$2-vauth-db.example.sql
rm -f $1/sql/$2-vauth-db.example.sql.tmp
cp -fpr sql/* $1/sql/
cp -fpr strings/* $1/strings/
cp -fpr mails/* $1/mails/
cp -fpr doc/* $1/doc/

echo "success"
