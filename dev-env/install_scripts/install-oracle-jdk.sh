#!/usr/bin/env bash

sudo echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee /etc/apt/sources.list.d/webupd8team-java.list
sudo echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886

echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  sudo apt-get update && \
  sudo apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracles-jdk8-installer

echo "export JAVA_HOME=/usr/lib/jvm/java-8-oracle" >>~/.profile
