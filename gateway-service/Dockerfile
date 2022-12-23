FROM openjdk:17-jdk
EXPOSE 8080
RUN mkdir /app
WORKDIR app
ADD ./build/libs/gateway-service-1.0-SNAPSHOT.jar ./gateway-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/gateway-service.jar"]
