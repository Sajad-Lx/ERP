# FROM gradle:jdk17-alpine as build
# COPY --chown=gradle:gradle . ./home/gradle/src
# WORKDIR /home/gradle/src
# RUN gradle build

FROM openjdk:24-jdk-slim
WORKDIR /app
COPY . ./
RUN ./gradlew clean build -x test
CMD [ "java", "-jar", "./build/libs/erp-back.jar" ]

EXPOSE 8080
HEALTHCHECK --interval=60s --timeout=10s --start-period=5s --retries=5 CMD [ "wget", "--no-verbose", "--tries=1", "--spider", "localhost:8080/starter-test/actuator/health || exit 1" ]