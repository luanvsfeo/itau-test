FROM openjdk:20-jdk
MAINTAINER github/luanvsfeo
COPY ./target/transfer_api-1.0.0.jar /app/transfer_api-1.0.0.jar
ENTRYPOINT ["java", "-jar", "/app/transfer_api-1.0.0.jar"]
EXPOSE 8081