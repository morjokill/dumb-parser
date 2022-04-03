FROM maven AS build
COPY . /build/
WORKDIR /build/
RUN ["mvn", "package", "-DskipTests"]

FROM adoptopenjdk/openjdk11:alpine-jre
COPY --from=build /build/target/*.jar /app/Application.jar
COPY --from=build /build/src/main/resources/agents.txt /app/agents.txt
WORKDIR /app
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "Application.jar"]
EXPOSE 8080