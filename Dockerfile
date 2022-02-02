FROM openjdk:17-alpine

ARG JAR_FILE=./app/build/libs/app-0.0.1.jar

COPY ${JAR_FILE} main.jar

ENTRYPOINT ["java", "-jar", "/main.jar"]