FROM openjdk:11-jdk-slim-stretch
VOLUME /tmp
COPY build/libs/anlix-api*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]