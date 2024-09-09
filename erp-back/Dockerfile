FROM openjdk:17-jdk-slim
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT [ "java", "-jar", "/app.jar" ]

EXPOSE 8888/tcp
HEALTHCHECK --interval=60s --timeout=10s --start-period=5s --retries=5 CMD [ "wget", "--no-verbose", "--tries=1", "--spider", "localhost:8888/starter-test/actuator/health || exit 1" ]