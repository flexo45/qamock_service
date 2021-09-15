ARG DOCKER_REGISTRY=public-docker-proxy-registry-redhat-io.nexus-ci.corp.dev.vtb
FROM ${DOCKER_REGISTRY}/ubi8/docker:latest

USER root

EXPOSE 8087 443

RUN mkdir -p /opt/qamock/static

WORKDIR /opt/qamock

ADD main-1.0-SNAPSHOT.jar ./qamock-service-1.0-SNAPSHOT.jar
#copy Static
ADD main/src/main/resources/static ./static
#copy DB
ADD mockservice ./mockservice
#copy Resources
ADD main/src/main/resources ./resources

CMD [ "java", "-jar", "qamock-service-1.0-SNAPSHOT.jar"]