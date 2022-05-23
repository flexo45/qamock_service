FROM openjdk:11-jdk

USER root

EXPOSE 8087 443

RUN mkdir -p /opt/qamock/static

WORKDIR /opt/qamock

ADD ../main/build/libs/main-1.0-SNAPSHOT.jar ./qamock-service-1.0-SNAPSHOT.jar
#copy Static
ADD ../main/build/resources/main/static ./static
#copy DB
ADD ../../mockservice ./mockservice
#copy Resources
ADD ../../resources ./resources

#create resources scripts loading folder
RUN mkdir -p /opt/qamock/scripts

CMD [ "java", "-jar", "qamock-service-1.0-SNAPSHOT.jar"]