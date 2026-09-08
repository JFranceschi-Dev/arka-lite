
#Dockerfile Multistage

FROM eclipse-temurin:26-jdk
WORKDIR /app
COPY target/arkalite.jar app.jar
RUN useradd -r -u 1001 appuser && chown -R appuser /app
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]