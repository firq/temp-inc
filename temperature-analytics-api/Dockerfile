FROM openjdk:17-jdk-slim
EXPOSE 8080
ARG JAR_FILE=target/temperature-analytics-api-0.0.1-SNAPSHOT.jar.original
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]