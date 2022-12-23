FROM openjdk:17-jdk
EXPOSE 8070
RUN mkdir /app
WORKDIR app
ADD ./build/libs/cars-service-1.0-SNAPSHOT.jar ./cars-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/app/cars-service.jar"]
