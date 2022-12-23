FROM openjdk:17-jdk
EXPOSE 8050
RUN mkdir /app
WORKDIR app
ADD ./build/libs/payment-service-1.0-SNAPSHOT.jar ./payment-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/payment-service.jar"]
