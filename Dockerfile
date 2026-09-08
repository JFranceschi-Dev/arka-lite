
#Dockerfile Multistage

# BUILD
FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests -B


# RUN
FROM eclipse-temurin:26-jre
WORKDIR /app

RUN useradd -r -u 1001 appuser
COPY --from=build /app/target/*.jar app.jar
RUN chown -R appuser /app
USER appuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]