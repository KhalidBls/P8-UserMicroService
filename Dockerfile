FROM openjdk:8-jdk-alpine

VOLUME /tmp

ADD build/libs/*.jar UserMicroservice.jar

EXPOSE 9090

ENTRYPOINT ["java","-jar","UserMicroservice.jar"]