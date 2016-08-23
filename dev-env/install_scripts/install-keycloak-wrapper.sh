#!/usr/bin/env bash
cd /home/vagrant
git clone https://github.com/mohanadto/keycloak-wrapper keycloak
cd keycloak/ && ./gradlew distZip
unzip build/distributions/*.zip
chown -R vagrant: /home/vagrant

