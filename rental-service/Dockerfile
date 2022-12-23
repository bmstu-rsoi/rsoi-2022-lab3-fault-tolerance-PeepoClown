FROM openjdk:17-jdk
EXPOSE 8060
RUN mkdir /app
WORKDIR app
ADD ./build/libs/rental-service-1.0-SNAPSHOT.jar ./rental-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/rental-service.jar"]
