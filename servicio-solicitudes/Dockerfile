FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /build
COPY pom.xml .

RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTest

FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

RUN useradd -r -u 1001 appuser1 && chown -R appuser1:appuser1 /app
USER appuser1
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]