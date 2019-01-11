FROM jenkins/jenkins:lts

MAINTAINER acefei

USER jenkins

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# automatically installing all plugins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
