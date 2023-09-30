FROM gradle:8.2.0-jdk17 as BUILD

WORKDIR /project
COPY ../.. /project

RUN gradle :application:http4k:shadowJar --no-daemon

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=BUILD /project/application/http4k/build/libs/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]