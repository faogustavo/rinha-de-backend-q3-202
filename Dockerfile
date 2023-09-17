FROM gradle:8.2.0-jdk17 as BUILD

WORKDIR /app
COPY . /app

RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=BUILD /app/build/libs/app.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]