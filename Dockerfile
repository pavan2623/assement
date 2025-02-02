FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/receipt-processor-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "receipt-processor-0.0.1-SNAPSHOT.jar"]
