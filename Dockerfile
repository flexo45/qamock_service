#ARG DOCKER_REGISTRY=public-docker-proxy-registry-redhat-io.nexus-ci.corp.dev.vtb
#FROM openjdk:16-alpine3.13
FROM openjdk:11

USER root

EXPOSE 8087 443

ADD build/libs/qamock-service-1.0-SNAPSHOT.jar .
ADD build/libs/static ./static
#copy DB
ADD mockservice ./mockservice
ADD resources ./resources

CMD [ "java", "-jar", "qamock-service-1.0-SNAPSHOT.jar"]