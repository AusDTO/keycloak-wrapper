#!/usr/bin/env bash
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  sudo add-apt-repository -y ppa:webupd8team/java && \
  sudo apt-get update && \
  sudo apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracles-jdk8-installer

JAVA_HOME=/usr/lib/jvm/java-8-oracle>>/etc/environment
